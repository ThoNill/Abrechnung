package boundingContext.abrechnung.repositories;

import org.springframework.data.repository.CrudRepository;

import boundingContext.abrechnung.entities.GebuehrDefinition;

public interface Geb�hrenDefinitionRepository extends
        CrudRepository<GebuehrDefinition, Long> {

}
