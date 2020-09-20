package be.fkgent.election.services.implementation;

import be.fkgent.election.domain.models.Barcode;
import be.fkgent.election.domain.models.User;
import be.fkgent.election.domain.repositories.BarcodeRepository;
import be.fkgent.election.domain.repositories.UserRepository;
import be.fkgent.election.services.UserService;
import liquibase.pro.packaged.B;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@AllArgsConstructor
@Service
public class  UserServiceImplementation implements UserService {
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

    @Override
    public Optional<User> findOrCreateUserByBarcode(String barcode) {
        return barcodeRepository.findBarcodeByCode(Barcode.NormalizeScannedCode(barcode))
                .map(Barcode::getUser)
                .or(() -> ldapUserService
                                    .findByBarcode(barcode)
                                    .map(this::mergeOrCreate)
                );
    }

    private User mergeOrCreate(User user) {
        Optional<User> existing = userRepository.findByUsername(user.getUsername());
        if(existing.isEmpty()) {
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
