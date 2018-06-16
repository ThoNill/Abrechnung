package tests;

import boundingContext.abrechnung.AbrechnungsSchrittFabrik;
import boundingContext.abrechnung.Geb�hrBerechnen;
import boundingContext.abrechnung.MehrereAbrechnungsSchritte;
import boundingContext.abrechnung.geb�hren.ProzentualeGeb�hr;
import boundingContext.gemeinsam.BetragsB�ndel;
import boundingContext.gemeinsam.EinzelBetrag;
import boundingContext.gemeinsam.EinzelBetragAusB�ndel;

public class TestAbrechnungsSchrittFabrik implements
        AbrechnungsSchrittFabrik<Position, TestGeb�hrenDefinition> {

    @Override
    public MehrereAbrechnungsSchritte<Position> apply(TestGeb�hrenDefinition t) {
        MehrereAbrechnungsSchritte<Position> mehrereAbrechnungsSchritte = new MehrereAbrechnungsSchritte<>();

        EinzelBetrag<BetragsB�ndel<Position>> betrag = new EinzelBetragAusB�ndel<>(
                Position.BETRAG);
        boolean add = mehrereAbrechnungsSchritte.add(

        new Geb�hrBerechnen<Position>(Position.GEB�HR, new ProzentualeGeb�hr(t
                .getGeb�hrA()), betrag)

        );

        return mehrereAbrechnungsSchritte;
    }

}
