package org.nill.daten;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.entities.Abrechnung;

import boundingContext.gemeinsam.BetragsB�ndel;

public interface Geb�hrRepository<KEY> {
    void markieren(Abrechnung abrechnung);

    BetragsB�ndel<KEY> getBetr�ge(Abrechnung abrechnung);

    MonetaryAmount getGeb�hrenBasis(Abrechnung abrechnung);

}
