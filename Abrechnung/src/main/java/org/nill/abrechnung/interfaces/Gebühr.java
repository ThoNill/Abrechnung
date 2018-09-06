package org.nill.abrechnung.interfaces;

import javax.money.MonetaryAmount;

/**
 * Eine Geb�hrberechnung, die aus einem Basisbetrag den Betrag der Geb�hr
 * berechnet.
 * 
 * @author Thomas Nill
 *
 */
public interface Geb�hr extends BetragsFunction {
    default MonetaryAmount getGeb�hr(MonetaryAmount basisbetrag) {
        return apply(basisbetrag);
    }
}
