package boundaryContext.abrechnung.helper;

import boundaryContext.abrechnung.entities.GebuehrDefinition;
import boundaryContext.abrechnung.flow.AbrechnungsKonfigurator;
import boundaryContext.abrechnung.repositories.LeistungRepository;
import boundingContext.abrechnung.aufz�hlungen.Position;
import boundingContext.abrechnung.geb�hren.Geb�hrFabrik;
import boundingContext.daten.Geb�hrRepository;

public class TestAbrechnungsKonfigurator implements AbrechnungsKonfigurator {

    private LeistungRepository leistungRepository;

    public TestAbrechnungsKonfigurator(LeistungRepository leistungRepository) {
        super();
        this.leistungRepository = leistungRepository;
    }

    @Override
    public Geb�hrFabrik erzeugeGeb�hrFabrik(int geb�hrArt) {
        return new TestGeb�hrFabrik();
    }

    @Override
    public Geb�hrRepository<Position> erzeugeGeb�hrRepository(int datenArt) {
        TestLeistungsRepository repo = new TestLeistungsRepository();
        repo.setLeistungsRepository(leistungRepository);
        return repo;
    }

    @Override
    public Geb�hrenBerechnung erzeugeGeb�hrenBerechner(
            GebuehrDefinition definition) {
        return new Geb�hrenBerechnung(definition,
                erzeugeGeb�hrRepository(definition.getDatenArt()),
                erzeugeGeb�hrFabrik(definition.getGeb�hrArt()));
    }

}
