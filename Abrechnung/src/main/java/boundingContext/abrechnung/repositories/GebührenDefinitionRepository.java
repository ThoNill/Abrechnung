package boundingContext.abrechnung.repositories;

import org.springframework.data.repository.CrudRepository;

import boundingContext.abrechnung.entities.GebuehrDefinition;

public interface GebührenDefinitionRepository extends
        CrudRepository<GebuehrDefinition, Long> {

}
