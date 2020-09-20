package be.winagent.covidscanner.domain.repositories;

import be.winagent.covidscanner.domain.models.Barcode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BarcodeRepository extends CrudRepository<Barcode, Long> {
    Optional<Barcode> findBarcodeByCode(String code);
}
