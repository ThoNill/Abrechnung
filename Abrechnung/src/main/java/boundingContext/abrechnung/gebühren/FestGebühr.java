package boundingContext.abrechnung.gebühren;

import javax.money.MonetaryAmount;

public class FestGebühr implements Gebühr {
    private MonetaryAmount gebühr;

    public FestGebühr(MonetaryAmount gebühr) {
        super();
        if (gebühr == null || gebühr.isNegative()) {
            throw new IllegalArgumentException(
                    "Festgebühren müssen positiv sein");
        }
        this.gebühr = gebühr;
    }

    @Override
    public MonetaryAmount apply(MonetaryAmount betrag) {
        return gebühr;
    }

}
