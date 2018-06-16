package tests;

import app.entities.GebuehrDefinition;
import app.repositories.LeistungRepository;
import boundingContext.abrechnung.gebühren.GebührFabrik;
import boundingContext.daten.GebührRepository;

public class TestAbrechnungsKonfigurator implements AbrechnungsKonfigurator {

    private LeistungRepository leistungRepository;

    public TestAbrechnungsKonfigurator(LeistungRepository leistungRepository) {
        super();
        this.leistungRepository = leistungRepository;
    }

    @Override
    public GebührFabrik erzeugeGebührFabrik(int gebührArt) {
        return new TestGebührFabrik();
    }

    @Override
    public GebührRepository<Position> erzeugeGebührRepository(int datenArt) {
        TestLeistungsRepository repo = new TestLeistungsRepository();
        repo.setLeistungsRepository(leistungRepository);
        return repo;
    }

    @Override
    public GebührenBerechnung erzeugeGebührenBerechner(
            GebuehrDefinition definition) {
        return new GebührenBerechnung(definition,
                erzeugeGebührRepository(definition.getDatenArt()),
                erzeugeGebührFabrik(definition.getGebührArt()));
    }

}
