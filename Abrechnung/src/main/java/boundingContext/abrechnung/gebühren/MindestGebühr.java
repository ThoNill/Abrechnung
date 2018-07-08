package boundingContext.abrechnung.gebühren;

import javax.money.MonetaryAmount;

import betrag.Geld;

public class MindestGebühr implements Gebühr {
    private MonetaryAmount minimum;

    public MindestGebühr(MonetaryAmount minimum) {
        super();
        if (minimum == null || minimum.isNegative()) {
            throw new IllegalArgumentException(
                    "Mindesgebühren müssen positiv sein");
        }
        this.minimum = minimum;
    }

    @Override
    public MonetaryAmount apply(MonetaryAmount gebühr) {
        if (gebühr.isPositive()) {
            return (gebühr.isLessThan(minimum)) ? minimum : Geld.getNull();
        } else {
            MonetaryAmount negGebühr = gebühr.negate();
            return (negGebühr.isLessThan(minimum)) ? minimum.negate() : Geld
                    .getNull();
        }
    }

}
