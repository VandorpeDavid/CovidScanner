package be.winagent.covidscanner.services;

import be.winagent.covidscanner.domain.models.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findOrCreateUserByUsername(String casname);
    Optional<User> findOrCreateUserByBarcode(String barcode);
    Optional<User> find(Long id);

}
