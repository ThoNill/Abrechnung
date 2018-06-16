package boundingContext.abrechnung.entities;

import java.util.Optional;

import ddd.Repository;

public interface MandantenRepository<ID, GEB�HRENDEFINITION> extends Repository {
    Optional<Mandant<ID>> getMandant(ID id);

    void insert(Mandant<ID> mandant);

    void update(Mandant<ID> mandant);

    Optional<GEB�HRENDEFINITION> getGeb�hrenDefinition(Mandant<ID> mandant);

}
