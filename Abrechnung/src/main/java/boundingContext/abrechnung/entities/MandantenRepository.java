package boundingContext.abrechnung.entities;

import java.util.Optional;

import ddd.Repository;

public interface MandantenRepository<ID, GEBÜHRENDEFINITION> extends Repository {
    Optional<Mandant<ID>> getMandant(ID id);

    void insert(Mandant<ID> mandant);

    void update(Mandant<ID> mandant);

    Optional<GEBÜHRENDEFINITION> getGebührenDefinition(Mandant<ID> mandant);

}
