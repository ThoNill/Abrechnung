package org.nill.abrechnung.interfaces;

import javax.money.MonetaryAmount;

import org.nill.basiskomponenten.gemeinsam.BetragsB�ndel;

public interface Geb�hrRepository<KEY> {
    void markieren(IAbrechnung abrechnung);

    BetragsB�ndel<KEY> getBetr�ge(IAbrechnung abrechnung);

    MonetaryAmount getGeb�hrenBasis(IAbrechnung abrechnung);

}
