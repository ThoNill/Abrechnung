package org.nill.daten;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.entities.Abrechnung;

import boundingContext.gemeinsam.BetragsBündel;

public interface GebührRepository<KEY> {
    void markieren(Abrechnung abrechnung);

    BetragsBündel<KEY> getBeträge(Abrechnung abrechnung);

    MonetaryAmount getGebührenBasis(Abrechnung abrechnung);

}
