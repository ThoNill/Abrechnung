package org.nill.abrechnung.actions;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.entities.Abrechnung;
import org.nill.basiskomponenten.gemeinsam.BetragsB�ndel;

public interface Geb�hrRepository<KEY> {
    void markieren(Abrechnung abrechnung);

    BetragsB�ndel<KEY> getBetr�ge(Abrechnung abrechnung);

    MonetaryAmount getGeb�hrenBasis(Abrechnung abrechnung);

}
