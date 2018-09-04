package org.nill.abrechnung.interfaces;

/**
 * Erzeugt �ber einen Parameterwert eine 
 * entsprechende {@link Geb�hr}.
 * 
 * @author Thomas Nill
 *
 */
@FunctionalInterface
public interface Geb�hrFabrik {
    Geb�hr createGeb�hr(double parameter);
}
