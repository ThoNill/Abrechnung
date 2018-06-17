package boundaryContext.abrechnung.repositories;

import org.springframework.data.repository.CrudRepository;

import boundaryContext.abrechnung.entities.KontoBewegung;

public interface KontoBewegungRepository extends
        CrudRepository<KontoBewegung, Long> {

}
