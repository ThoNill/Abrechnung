package tests.flow;

import boundingContext.abrechnung.aufz�hlungen.SachKonto;
import boundingContext.abrechnung.aufz�hlungen.SachKontoProvider;
import boundingContext.abrechnung.entities.GebuehrDefinition;
import boundingContext.abrechnung.flow.AbrechnungsKonfigurator;
import boundingContext.abrechnung.geb�hren.Geb�hrFabrik;
import boundingContext.abrechnung.helper.Geb�hrenBerechnung;
import boundingContext.abrechnung.repositories.LeistungRepository;
import boundingContext.daten.Geb�hrRepository;

public class TestAbrechnungsKonfigurator implements AbrechnungsKonfigurator {

    private LeistungRepository leistungRepository;
    private SachKontoProvider sachKontoProvider;

    public TestAbrechnungsKonfigurator(SachKontoProvider sachKontoProvider,LeistungRepository leistungRepository) {
        super();
        this.leistungRepository = leistungRepository;
        this.sachKontoProvider = sachKontoProvider;
    }

    @Override
    public Geb�hrFabrik erzeugeGeb�hrFabrik(int geb�hrArt) {
        return new TestGeb�hrFabrik();
    }

    @Override
    public Geb�hrRepository<SachKonto> erzeugeGeb�hrRepository(int datenArt) {
        TestLeistungsRepository repo = new TestLeistungsRepository();
        repo.setLeistungsRepository(leistungRepository);
        return repo;
    }

    @Override
    public Geb�hrenBerechnung erzeugeGeb�hrenBerechner(
            GebuehrDefinition definition) {
        return new Geb�hrenBerechnung(sachKontoProvider,definition,
                erzeugeGeb�hrRepository(definition.getDatenArt()),
                erzeugeGeb�hrFabrik(definition.getGeb�hrArt()));
    }

}
