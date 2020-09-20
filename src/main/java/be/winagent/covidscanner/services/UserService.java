package be.fkgent.election.services;

import be.fkgent.election.domain.models.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findOrCreateUserByUsername(String casname);
    Optional<User> findOrCreateUserByBarcode(String barcode);
    Optional<User> find(Long id);

}
