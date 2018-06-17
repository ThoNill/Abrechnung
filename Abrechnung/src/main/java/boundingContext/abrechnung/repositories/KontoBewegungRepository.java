package boundingContext.abrechnung.repositories;

import org.springframework.data.repository.CrudRepository;

import boundingContext.abrechnung.entities.KontoBewegung;

public interface KontoBewegungRepository extends
        CrudRepository<KontoBewegung, Long> {

}
