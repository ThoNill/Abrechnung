package tests.flow;

import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.entities.GebuehrDefinition;
import boundingContext.abrechnung.flow.AbrechnungsKonfigurator;
import boundingContext.abrechnung.gebühren.GebührFabrik;
import boundingContext.abrechnung.helper.GebührenBerechnung;
import boundingContext.abrechnung.repositories.LeistungRepository;
import boundingContext.daten.GebührRepository;

public class TestAbrechnungsKonfigurator implements AbrechnungsKonfigurator {

    private LeistungRepository leistungRepository;
    private SachKontoProvider sachKontoProvider;

    public TestAbrechnungsKonfigurator(SachKontoProvider sachKontoProvider,LeistungRepository leistungRepository) {
        super();
        this.leistungRepository = leistungRepository;
        this.sachKontoProvider = sachKontoProvider;
    }

    @Override
    public GebührFabrik erzeugeGebührFabrik(int gebührArt) {
        return new TestGebührFabrik();
    }

    @Override
    public GebührRepository<SachKonto> erzeugeGebührRepository(int datenArt) {
        TestLeistungsRepository repo = new TestLeistungsRepository();
        repo.setLeistungsRepository(leistungRepository);
        return repo;
    }

    @Override
    public GebührenBerechnung erzeugeGebührenBerechner(
            GebuehrDefinition definition) {
        return new GebührenBerechnung(sachKontoProvider,definition,
                erzeugeGebührRepository(definition.getDatenArt()),
                erzeugeGebührFabrik(definition.getGebührArt()));
    }

}
