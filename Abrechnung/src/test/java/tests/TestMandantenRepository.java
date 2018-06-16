package tests;

import java.util.Optional;

import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.entities.MandantenRepository;

public class TestMandantenRepository extends TestRepository<Mandant<Long>>
        implements MandantenRepository<Long, TestGeb�hrenDefinition> {

    @Override
    public Optional<Mandant<Long>> getMandant(Long id) {
        return Optional.ofNullable(getEntity(id));
    }

    @Override
    public Optional<TestGeb�hrenDefinition> getGeb�hrenDefinition(
            Mandant<Long> mandant) {
        return Optional.ofNullable(new TestGeb�hrenDefinition(true, 0.2d, true,
                0.3d, 20d, 10d));
    }

}
