package boundingContext.abrechnung.geb�hren;

import javax.money.MonetaryAmount;

import betrag.Geld;

public class AnzahlGeb�hr implements Geb�hr {
    private MonetaryAmount geb�hr;

    public AnzahlGeb�hr(MonetaryAmount geb�hr) {
        super();
        if (geb�hr == null || geb�hr.isNegative()) {
            throw new IllegalArgumentException(
                    "Festgeb�hren m�ssen positiv sein");
        }
        this.geb�hr = geb�hr;
    }

    @Override
    public MonetaryAmount apply(MonetaryAmount anzahl) {
        return Geld.round(anzahl.multiply(geb�hr.getNumber()));
    }

}
