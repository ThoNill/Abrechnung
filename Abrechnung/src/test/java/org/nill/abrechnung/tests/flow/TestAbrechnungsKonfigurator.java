package org.nill.abrechnung.tests.flow;

import org.nill.abrechnung.actions.Geb�hrRepository;
import org.nill.abrechnung.actions.Geb�hrenBerechnung;
import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.abrechnung.aufz�hlungen.SachKontoProvider;
import org.nill.abrechnung.entities.GebuehrDefinition;
import org.nill.abrechnung.flow.handler.AbrechnungsKonfigurator;
import org.nill.abrechnung.flow.payloads.AbrechnungsArt;
import org.nill.abrechnung.geb�hren.Geb�hrFabrik;
import org.nill.abrechnung.repositories.LeistungRepository;

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
    public Geb�hrRepository<SachKonto> erzeugeGeb�hrRepository(int datenArt) {
        TestLeistungsRepository repo = new TestLeistungsRepository();
        repo.setLeistungsRepository(leistungRepository);
        return repo;
    }

    @Override
    public Geb�hrenBerechnung erzeugeGeb�hrenBerechner(
            GebuehrDefinition definition, SachKontoProvider sachKontoProvider,
            AbrechnungsArt abrechnungsArt) {
        return new Geb�hrenBerechnung(sachKontoProvider, definition,
                erzeugeGeb�hrRepository(definition.getDatenArt()),
                erzeugeGeb�hrFabrik(definition.getGeb�hrArt()), abrechnungsArt);
    }

}
