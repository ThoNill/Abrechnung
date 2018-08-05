package org.nill.abrechnung.gebühren;

import java.util.function.Function;

import javax.money.MonetaryAmount;

public interface BetragsFunction extends
        Function<MonetaryAmount, MonetaryAmount> {
}
