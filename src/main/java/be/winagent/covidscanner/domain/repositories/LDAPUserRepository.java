package be.fkgent.election.domain.repositories;

import be.fkgent.election.domain.models.LDAPUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
