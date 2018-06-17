package boundaryContext.abrechnung.repositories;

import org.springframework.data.repository.CrudRepository;

import boundaryContext.abrechnung.entities.GebuehrDefinition;

public interface GebührenDefinitionRepository extends
        CrudRepository<GebuehrDefinition, Long> {

}
