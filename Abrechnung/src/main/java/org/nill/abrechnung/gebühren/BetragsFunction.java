package org.nill.abrechnung.geb�hren;

import java.util.function.Function;

import javax.money.MonetaryAmount;

public interface BetragsFunction extends
        Function<MonetaryAmount, MonetaryAmount> {
}
