package org.nill.abrechnung.geb�hren;

import javax.money.MonetaryAmount;

import betrag.Geld;

public class ProzentualeGeb�hr implements Geb�hr {
    private double prozentsatz;

    public ProzentualeGeb�hr(double prozentsatz) {
        super();
        if (prozentsatz < 0.0d) {
            throw new IllegalArgumentException(
                    "Prozents�tze m�ssen positiv sein");
        }
        this.prozentsatz = prozentsatz;
    }

    @Override
    public MonetaryAmount apply(MonetaryAmount betrag) {
        return Geld.percentAmount(betrag, prozentsatz);
    }

}
