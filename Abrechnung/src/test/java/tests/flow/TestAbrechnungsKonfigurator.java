package tests.flow;

import tests.konten.TestSachKontoProvider;
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
 
    public TestAbrechnungsKonfigurator(LeistungRepository leistungRepository) {
        super();
        this.leistungRepository = leistungRepository;
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
        return new GebührenBerechnung(new TestSachKontoProvider(),definition,
                erzeugeGebührRepository(definition.getDatenArt()),
                erzeugeGebührFabrik(definition.getGebührArt()));
    }

}
