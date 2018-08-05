package org.nill.abrechnung.geb�hren;

import javax.money.MonetaryAmount;

public interface Geb�hr extends BetragsFunction {
    default MonetaryAmount getGeb�hr(MonetaryAmount betrag) {
        return apply(betrag);
    }
}
