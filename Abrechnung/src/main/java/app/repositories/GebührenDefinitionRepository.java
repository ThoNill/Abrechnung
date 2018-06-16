package app.repositories;

import org.springframework.data.repository.CrudRepository;

import app.entities.GebuehrDefinition;

public interface GebührenDefinitionRepository extends
        CrudRepository<GebuehrDefinition, Long> {

}
