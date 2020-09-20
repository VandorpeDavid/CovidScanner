package be.fkgent.election.domain.repositories;

import be.fkgent.election.domain.models.Barcode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BarcodeRepository extends CrudRepository<Barcode, Long> {
    Optional<Barcode> findBarcodeByCode(String code);
}
