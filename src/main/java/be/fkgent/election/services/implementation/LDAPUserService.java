package be.fkgent.election.services.implementation;

import be.fkgent.election.domain.models.Barcode;
import be.fkgent.election.domain.models.LDAPUser;
import be.fkgent.election.domain.models.User;
import be.fkgent.election.domain.repositories.LDAPUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author davdorpe
 * @since 0.0.1
 */
@CommonsLog
@Service
@AllArgsConstructor
public class LDAPUserService {
    private final LDAPUserRepository ldapUserRepository;

    /**
     * Queries UGent LDAP for user attributes corresponding to casName
     *
     * @param casName CAS name to query for
     * @return Populated User attribute. Optional.empty() if not found.
     */
    public Optional<User> findByCasName(String casName) {
        log.info(String.format("Querying LDAP for %s", casName));
        /*test("mveyck", "001909086294");
        test("rvdvyver", "001703894897");
        test("jiwillae", "001900272429");
        test("madforch", "001604346631");
        test("wdwettin", "001703599853");
        test("njovdnbo", "001606910083");*/

        return ldapUserRepository
                .findLDAPUserByUsername(casName)
                .map(this::ldapToUserModel);
    }

    /*public void test(String casname, String barcode) {
        LDAPUser user = ldapUserRepository
                .findLDAPUserByUsername(casname).orElseThrow();
        String barcode2 = "0" + barcode.substring(0, barcode.length()w - 1);

        System.out.printf("%s (%s) -> %s | %s %s%n", casname, barcode, user.getStudentID(), String.join(", ", user.getBarcode()), barcode2.equals(user.getBarcode().get(0)));
    }*/

    public Optional<User> findByBarcode(String barcode) {
        barcode = Barcode.NormalizeScannedCode(barcode);
        return ldapUserRepository
                .findLDAPUserByBarcode(barcode)
                .map(this::ldapToUserModel);
    }

    private User ldapToUserModel(LDAPUser ldapUser) {
        User user = new User();

        user.setEmail(ldapUser.getEmail());
        user.setLastName(ldapUser.getLastName());
        user.setFirstName(ldapUser.getFirstName());
        user.setUsername(ldapUser.getUsername());
        user.setBarcodes(
                ldapUser.getBarcode().stream()
                .map(code -> {
                    Barcode barcode = new Barcode();
                    barcode.setCode(code);
                    barcode.setUser(user);
                    return barcode;
                })
                .collect(Collectors.toSet())
        );
        return user;
    }
}
