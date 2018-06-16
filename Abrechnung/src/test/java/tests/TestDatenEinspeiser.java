package tests;

import boundingContext.abrechnung.AbrechnungsBeträge;
import boundingContext.abrechnung.BeträgeEinspeisen;

public class TestDatenEinspeiser implements
        BeträgeEinspeisen<Position, TestDatenRepository> {

    @Override
    public AbrechnungsBeträge<Position> apply(TestDatenRepository t) {
        AbrechnungsBeträge<Position> abrechnungsBeträge = new AbrechnungsBeträge<>();
        abrechnungsBeträge.put(Position.BETRAG, t.getBetragA());
        return abrechnungsBeträge;
    }

}
