package org.nill.abrechnung.tests.flow;

import org.nill.abrechnung.actions.Geb�hrenBerechnung;
import org.nill.abrechnung.aufz�hlungen.AbrechnungsArt;
import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.abrechnung.interfaces.AbrechnungsKonfigurator;
import org.nill.abrechnung.interfaces.Geb�hrFabrik;
import org.nill.abrechnung.interfaces.Geb�hrRepository;
import org.nill.abrechnung.interfaces.IGeb�hrBerechnung;
import org.nill.abrechnung.interfaces.IGeb�hrDefinition;
import org.nill.abrechnung.interfaces.ILeistungRepository;
import org.nill.abrechnung.interfaces.SachKontoProvider;

public class TestAbrechnungsKonfigurator implements AbrechnungsKonfigurator {

    private ILeistungRepository leistungRepository;

    public TestAbrechnungsKonfigurator(ILeistungRepository leistungRepository) {
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
    public IGeb�hrBerechnung erzeugeGeb�hrenBerechner(
            IGeb�hrDefinition definition, SachKontoProvider sachKontoProvider,
            AbrechnungsArt abrechnungsArt) {
        return new Geb�hrenBerechnung(sachKontoProvider, definition,
                erzeugeGeb�hrRepository(definition.getDatenArt()),
                erzeugeGeb�hrFabrik(definition.getGeb�hrArt()), abrechnungsArt);
    }

}
