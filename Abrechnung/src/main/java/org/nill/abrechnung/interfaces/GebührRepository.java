package org.nill.abrechnung.interfaces;

import javax.money.MonetaryAmount;

import org.nill.basiskomponenten.gemeinsam.BetragsBündel;

public interface GebührRepository<KEY> {
    void markieren(IAbrechnung abrechnung);

    BetragsBündel<KEY> getBeträge(IAbrechnung abrechnung);

    MonetaryAmount getGebührenBasis(IAbrechnung abrechnung);

}
