package tests;

import boundingContext.abrechnung.AbrechnungsSchrittFabrik;
import boundingContext.abrechnung.GebührBerechnen;
import boundingContext.abrechnung.MehrereAbrechnungsSchritte;
import boundingContext.abrechnung.gebühren.ProzentualeGebühr;
import boundingContext.gemeinsam.BetragsBündel;
import boundingContext.gemeinsam.EinzelBetrag;
import boundingContext.gemeinsam.EinzelBetragAusBündel;

public class TestAbrechnungsSchrittFabrik implements
        AbrechnungsSchrittFabrik<Position, TestGebührenDefinition> {

    @Override
    public MehrereAbrechnungsSchritte<Position> apply(TestGebührenDefinition t) {
        MehrereAbrechnungsSchritte<Position> mehrereAbrechnungsSchritte = new MehrereAbrechnungsSchritte<>();

        EinzelBetrag<BetragsBündel<Position>> betrag = new EinzelBetragAusBündel<>(
                Position.BETRAG);
        boolean add = mehrereAbrechnungsSchritte.add(

        new GebührBerechnen<Position>(Position.GEBÜHR, new ProzentualeGebühr(t
                .getGebührA()), betrag)

        );

        return mehrereAbrechnungsSchritte;
    }

}
