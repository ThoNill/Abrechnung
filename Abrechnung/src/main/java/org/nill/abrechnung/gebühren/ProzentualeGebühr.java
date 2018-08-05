package org.nill.abrechnung.gebühren;

import javax.money.MonetaryAmount;

import betrag.Geld;

public class ProzentualeGebühr implements Gebühr {
    private double prozentsatz;

    public ProzentualeGebühr(double prozentsatz) {
        super();
        if (prozentsatz < 0.0d) {
            throw new IllegalArgumentException(
                    "Prozentsätze müssen positiv sein");
        }
        this.prozentsatz = prozentsatz;
    }

    @Override
    public MonetaryAmount apply(MonetaryAmount betrag) {
        return Geld.percentAmount(betrag, prozentsatz);
    }

}
