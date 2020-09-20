package be.winagent.covidscanner.domain.repositories;

import be.winagent.covidscanner.domain.models.LDAPUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author davdorpe
 * @see
 * @since
 */
@org.springframework.stereotype.Repository
public interface LDAPUserRepository extends CrudRepository<LDAPUser, String> {
    /**
     * Find a user in the LDAP by username (UGent casname)
     * @param username Username to query for
     * @return The found user.
     */
    Optional<LDAPUser> findLDAPUserByUsername(String username);
    List<LDAPUser> findAll();


    Optional<LDAPUser> findLDAPUserByBarcode(String barcode);
}
