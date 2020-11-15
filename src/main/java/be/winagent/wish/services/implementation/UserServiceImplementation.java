package be.winagent.wish.services.implementation;

import be.winagent.wish.domain.models.Barcode;
import be.winagent.wish.domain.models.User;
import be.winagent.wish.domain.repositories.BarcodeRepository;
import be.winagent.wish.domain.repositories.UserRepository;
import be.winagent.wish.services.UserService;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

@AllArgsConstructor
@Service
public class UserServiceImplementation implements UserService {
    private final LDAPUserService ldapUserService;
    private final UserRepository userRepository;
    private final BarcodeRepository barcodeRepository;

    @Override
    public Optional<User> findOrCreateUserByUsername(String casname) {
        return userRepository.findByUsername(casname)
                .or(() ->
                        ldapUserService
                                .findByCasName(casname)
                                .map(userRepository::save)
                );
    }

    private Optional<String> buildBarcode(String barcodeData) {
        if (barcodeData.length() < 3) {
            return Optional.empty();
        }

        try {
            long data = Long.parseLong(barcodeData.substring(0, barcodeData.length() - 2));
            long check = Long.parseLong(barcodeData.substring(barcodeData.length() - 2));
            // data and check must be equal mod 97, but 0 is represented by 97.
            if ((data - 1) % 97 != check - 1) {
                return Optional.empty();
            }

            if (barcodeData.startsWith("0")) {
                barcodeData = "0" + barcodeData;
            }

            // A leading zero is missing in student barcodes, but not in employee barcodes.
            return Optional.of(barcodeData);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findOrCreateUserByBarcode(String barcode) {
        List<String> barcodeCandidates = new ArrayList<>();

        if (barcode.length() > 0) {

            // Barcodes that have a check digit appended.
            buildBarcode(barcode.substring(0, barcode.length() - 1)).ifPresent(barcodeCandidates::add);

            // Barcodes scanned with a scanner that consumes the check digit.
            buildBarcode(barcode).ifPresent(barcodeCandidates::add);

            try {
                // Card number
                String barcodeFromCardNumber = barcode + String.format("%02d", Long.parseLong(barcode) % 97);

                // For some reason another leading zero gets dropped from student ids.
                if (barcodeFromCardNumber.startsWith("0")) {
                    barcodeFromCardNumber = "0" + barcodeFromCardNumber;
                }

                buildBarcode(barcodeFromCardNumber).ifPresent(barcodeCandidates::add);
            } catch (NumberFormatException e) {
                // Ignore.
            }
        }

        return barcodeRepository.findAllByCodeIsIn(barcodeCandidates).stream()
                .findFirst()
                .map(Barcode::getUser)
                .or(() -> barcodeCandidates.stream()
                        .parallel()
                        .map((ldapBarcode) -> {
                            // This fixes an issue with the class loader in prod.
                            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
                            return ldapUserService.findByBarcode(ldapBarcode);
                        })
                        .flatMap(Optional::stream)
                        .findAny()
                        .map(this::mergeOrCreate)
                );
    }

    @Override
    public void deleteAll(Iterable<User> users) {
        this.userRepository.deleteAll(users);
    }

    private User mergeOrCreate(User user) {
        Optional<User> existing = userRepository.findByUsername(user.getUsername());
        if (existing.isEmpty()) {
            return userRepository.save(user);
        } else {
            User existingUser = existing.get();

            existingUser.setEmail(user.getEmail());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            Set<String> existingCodes = existingUser.getBarcodes().stream()
                    .map(Barcode::getCode)
                    .collect(Collectors.toSet());

            existingUser.getBarcodes().addAll(
                    user.getBarcodes()
                            .stream()
                            .map(Barcode::getCode)
                            .filter(not(existingCodes::contains))
                            .map((code) -> {
                                Barcode barcode = new Barcode();
                                barcode.setUser(existingUser);
                                barcode.setCode(code);
                                return barcode;
                            })
                            .collect(Collectors.toSet())
            );

            return userRepository.save(existingUser);
        }
    }

    @Override
    public Optional<User> find(Long id) {
        return userRepository.findById(id);
    }
}
