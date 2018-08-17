package org.nill.abrechnung.gebühren;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.interfaces.Gebühr;
import org.nill.basiskomponenten.betrag.Geld;

public class AnzahlGebühr implements Gebühr {
    private MonetaryAmount gebühr;

    public AnzahlGebühr(MonetaryAmount gebühr) {
        super();
        if (gebühr == null || gebühr.isNegative()) {
            throw new IllegalArgumentException(
                    "Festgebühren müssen positiv sein");
        }
        this.gebühr = gebühr;
    }

    @Override
    public MonetaryAmount apply(MonetaryAmount anzahl) {
        return Geld.round(anzahl.multiply(gebühr.getNumber()));
    }

}
