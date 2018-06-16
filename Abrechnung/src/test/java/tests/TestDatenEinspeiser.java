package tests;

import boundingContext.abrechnung.AbrechnungsBetr�ge;
import boundingContext.abrechnung.Betr�geEinspeisen;

public class TestDatenEinspeiser implements
        Betr�geEinspeisen<Position, TestDatenRepository> {

    @Override
    public AbrechnungsBetr�ge<Position> apply(TestDatenRepository t) {
        AbrechnungsBetr�ge<Position> abrechnungsBetr�ge = new AbrechnungsBetr�ge<>();
        abrechnungsBetr�ge.put(Position.BETRAG, t.getBetragA());
        return abrechnungsBetr�ge;
    }

}
