package org.nill.abrechnung.tests.flow;

import org.nill.abrechnung.actions.GebührenBerechnung;
import org.nill.abrechnung.aufzählungen.AbrechnungsArt;
import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.abrechnung.interfaces.AbrechnungsKonfigurator;
import org.nill.abrechnung.interfaces.GebührFabrik;
import org.nill.abrechnung.interfaces.GebührRepository;
import org.nill.abrechnung.interfaces.IGebührBerechnung;
import org.nill.abrechnung.interfaces.IGebührDefinition;
import org.nill.abrechnung.interfaces.Umgebung;
import org.nill.abrechnung.repositories.ILeistungRepository;

public class TestAbrechnungsKonfigurator implements AbrechnungsKonfigurator {

    private ILeistungRepository leistungRepository;

    public TestAbrechnungsKonfigurator(ILeistungRepository leistungRepository) {
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
    public IGebührBerechnung erzeugeGebührenBerechner(
            IGebührDefinition definition, Umgebung umgebung,
            AbrechnungsArt abrechnungsArt) {
        return new GebührenBerechnung(umgebung, definition,
                erzeugeGebührRepository(definition.getDatenArt()),
                erzeugeGebührFabrik(definition.getGebührArt()), abrechnungsArt);
    }

}
