package be.winagent.wish.domain.repositories;

import be.winagent.wish.domain.models.Barcode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BarcodeRepository extends CrudRepository<Barcode, Long> {
    Optional<Barcode> findBarcodeByCode(String code);
    List<Barcode> findAllByCodeIsIn(Collection<String> barcodes);
}
