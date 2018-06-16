package boundingContext.abrechnung.entities;

import java.util.List;
import java.util.Optional;

import ddd.Repository;

public interface AbrechnungsRepository<ID> extends Repository {
    Optional<Abrechnung<ID>> aktuelleAbrechnung(Mandant<ID> mandant,
            Zeitraum zeitraum);

    Optional<Abrechnung<ID>> getAbrechnung(ID id);

    List<ID> getAbzurechnendeMandanten(Zeitraum zeitraum);

    Optional<Abrechnung<ID>> getVorherigeAbrechnung(Abrechnung<ID> abrechnung);

    Optional<Abrechnung<ID>> getFolgendeAbrechnung(Abrechnung<ID> abrechnung);

    void insert(Abrechnung<ID> abrechnung);

    void update(Abrechnung<ID> abrechnung);

}
