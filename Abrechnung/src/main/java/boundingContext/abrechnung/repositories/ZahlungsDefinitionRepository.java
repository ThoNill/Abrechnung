package boundingContext.abrechnung.repositories;

import org.springframework.data.repository.CrudRepository;

import boundingContext.abrechnung.entities.ZahlungsDefinition;

public interface ZahlungsDefinitionRepository extends
        CrudRepository<ZahlungsDefinition, Long> {

}
