package boundingContext.abrechnung.geb�hren;

import javax.money.MonetaryAmount;

public class FestGeb�hr implements Geb�hr {
    private MonetaryAmount geb�hr;

    public FestGeb�hr(MonetaryAmount geb�hr) {
        super();
        if (geb�hr == null || geb�hr.isNegative()) {
            throw new IllegalArgumentException(
                    "Festgeb�hren m�ssen positiv sein");
        }
        this.geb�hr = geb�hr;
    }

    @Override
    public MonetaryAmount apply(MonetaryAmount betrag) {
        return geb�hr;
    }

}
