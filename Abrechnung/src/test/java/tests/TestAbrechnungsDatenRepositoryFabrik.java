package tests;

import java.util.ArrayList;
import java.util.List;

import boundingContext.abrechnung.AbrechnungsDatenRepositoryFabrik;
import boundingContext.abrechnung.TeilAbrechnungsBeschreibung;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.buchhaltung.eingang.BuchungsAufträge;

public class TestAbrechnungsDatenRepositoryFabrik
        implements
        AbrechnungsDatenRepositoryFabrik<Long, Position, TestGebührenDefinition, TestDatenRepository> {

    @Override
    public TestDatenRepository markierendesRepository(
            Abrechnung<Long> abrechnung) {
        return new TestDatenRepository();
    }

    @Override
    public TestDatenRepository nachberechnungsRepository(
            Abrechnung<Long> abrechnung) {
        return new TestDatenRepository();
    }

    @Override
    public TestDatenRepository stornierendesRepository(
            Abrechnung<Long> abrechnung) {
        return new TestDatenRepository();
    }

    @Override
    public List<TeilAbrechnungsBeschreibung<Position, TestGebührenDefinition, TestDatenRepository>> getTeilAbrechnungsBeschreibungen() {
        List<TeilAbrechnungsBeschreibung<Position, TestGebührenDefinition, TestDatenRepository>> liste = new ArrayList<>();
        TeilAbrechnungsBeschreibung<Position, TestGebührenDefinition, TestDatenRepository> beschreibung = new TeilAbrechnungsBeschreibung<>(
                3, "Test", new TestAbrechnungsSchrittFabrik(),
                new TestDatenEinspeiser());
        liste.add(beschreibung);
        return liste;
    }

    @Override
    public List<TeilAbrechnungsBeschreibung<Position, TestGebührenDefinition, BuchungsAufträge<Long, Position>>> getSammelAbrechnungsBeschreibungen() {
        // TODO Auto-generated method stub
        return null;
    }

}
