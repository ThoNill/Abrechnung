package tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.AbrechnungsRepository;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.entities.Zeitraum;

public class TestAbrechnungsRepository extends TestRepository<Abrechnung<Long>>
        implements AbrechnungsRepository<Long> {

    @Override
    public Optional<Abrechnung<Long>> aktuelleAbrechnung(Mandant<Long> mandant,
            Zeitraum zeitraum) {
        if (entities.values().size() == 0) {
            return Optional.empty();
        }
        return Optional
                .ofNullable((TestAbrechnung) entities.values().toArray()[0]);
    }

    @Override
    public Optional<Abrechnung<Long>> getAbrechnung(Long id) {
        return Optional.ofNullable(getEntity(id));
    }

    @Override
    public List<Long> getAbzurechnendeMandanten(Zeitraum zeitraum) {
        return new ArrayList<>();
    }

    @Override
    public Optional<Abrechnung<Long>> getVorherigeAbrechnung(
            Abrechnung<Long> abrechnung) {
        return Optional.empty();
    }

    @Override
    public Optional<Abrechnung<Long>> getFolgendeAbrechnung(
            Abrechnung<Long> abrechnung) {
        return Optional.empty();
    }

}
