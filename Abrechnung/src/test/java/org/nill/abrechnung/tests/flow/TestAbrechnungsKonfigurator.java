package org.nill.abrechnung.tests.flow;

import org.nill.abrechnung.actions.GebührRepository;
import org.nill.abrechnung.actions.GebührenBerechnung;
import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.abrechnung.aufzählungen.SachKontoProvider;
import org.nill.abrechnung.entities.GebuehrDefinition;
import org.nill.abrechnung.flow.handler.AbrechnungsKonfigurator;
import org.nill.abrechnung.flow.payloads.AbrechnungsArt;
import org.nill.abrechnung.gebühren.GebührFabrik;
import org.nill.abrechnung.repositories.LeistungRepository;

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
            GebuehrDefinition definition, SachKontoProvider sachKontoProvider,
            AbrechnungsArt abrechnungsArt) {
        return new GebührenBerechnung(sachKontoProvider, definition,
                erzeugeGebührRepository(definition.getDatenArt()),
                erzeugeGebührFabrik(definition.getGebührArt()), abrechnungsArt);
    }

}
