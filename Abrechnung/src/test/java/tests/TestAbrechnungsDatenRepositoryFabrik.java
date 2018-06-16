package tests;

import java.util.ArrayList;
import java.util.List;

import boundingContext.abrechnung.AbrechnungsDatenRepositoryFabrik;
import boundingContext.abrechnung.TeilAbrechnungsBeschreibung;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.buchhaltung.eingang.BuchungsAuftr�ge;

public class TestAbrechnungsDatenRepositoryFabrik
        implements
        AbrechnungsDatenRepositoryFabrik<Long, Position, TestGeb�hrenDefinition, TestDatenRepository> {

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
    public List<TeilAbrechnungsBeschreibung<Position, TestGeb�hrenDefinition, TestDatenRepository>> getTeilAbrechnungsBeschreibungen() {
        List<TeilAbrechnungsBeschreibung<Position, TestGeb�hrenDefinition, TestDatenRepository>> liste = new ArrayList<>();
        TeilAbrechnungsBeschreibung<Position, TestGeb�hrenDefinition, TestDatenRepository> beschreibung = new TeilAbrechnungsBeschreibung<>(
                3, "Test", new TestAbrechnungsSchrittFabrik(),
                new TestDatenEinspeiser());
        liste.add(beschreibung);
        return liste;
    }

    @Override
    public List<TeilAbrechnungsBeschreibung<Position, TestGeb�hrenDefinition, BuchungsAuftr�ge<Long, Position>>> getSammelAbrechnungsBeschreibungen() {
        // TODO Auto-generated method stub
        return null;
    }

}
