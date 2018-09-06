package org.nill.abrechnung.interfaces;

import java.util.function.Function;

import javax.money.MonetaryAmount;

@FunctionalInterface
public interface BetragsFunction extends
        Function<MonetaryAmount, MonetaryAmount> {
}
