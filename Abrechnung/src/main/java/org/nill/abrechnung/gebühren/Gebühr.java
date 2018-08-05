package org.nill.abrechnung.gebühren;

import javax.money.MonetaryAmount;

public interface Gebühr extends BetragsFunction {
    default MonetaryAmount getGebühr(MonetaryAmount betrag) {
        return apply(betrag);
    }
}
