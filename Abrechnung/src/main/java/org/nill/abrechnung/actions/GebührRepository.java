package org.nill.abrechnung.actions;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.entities.Abrechnung;
import org.nill.basiskomponenten.gemeinsam.BetragsBündel;

public interface GebührRepository<KEY> {
    void markieren(Abrechnung abrechnung);

    BetragsBündel<KEY> getBeträge(Abrechnung abrechnung);

    MonetaryAmount getGebührenBasis(Abrechnung abrechnung);

}
