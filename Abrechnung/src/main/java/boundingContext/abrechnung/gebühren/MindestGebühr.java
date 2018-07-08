package boundingContext.abrechnung.geb�hren;

import javax.money.MonetaryAmount;

import betrag.Geld;

public class MindestGeb�hr implements Geb�hr {
    private MonetaryAmount minimum;

    public MindestGeb�hr(MonetaryAmount minimum) {
        super();
        if (minimum == null || minimum.isNegative()) {
            throw new IllegalArgumentException(
                    "Mindesgeb�hren m�ssen positiv sein");
        }
        this.minimum = minimum;
    }

    @Override
    public MonetaryAmount apply(MonetaryAmount geb�hr) {
        if (geb�hr.isPositive()) {
            return (geb�hr.isLessThan(minimum)) ? minimum : Geld.getNull();
        } else {
            MonetaryAmount negGeb�hr = geb�hr.negate();
            return (negGeb�hr.isLessThan(minimum)) ? minimum.negate() : Geld
                    .getNull();
        }
    }

}
