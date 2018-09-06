package org.nill.abrechnung.interfaces;

import javax.money.MonetaryAmount;

/**
 * Eine Gebührberechnung, die aus einem Basisbetrag den Betrag der Gebühr
 * berechnet.
 * 
 * @author Thomas Nill
 *
 */
public interface Gebühr extends BetragsFunction {
    default MonetaryAmount getGebühr(MonetaryAmount basisbetrag) {
        return apply(basisbetrag);
    }
}
