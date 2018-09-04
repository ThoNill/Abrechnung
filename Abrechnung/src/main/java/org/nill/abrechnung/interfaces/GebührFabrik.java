package org.nill.abrechnung.interfaces;

/**
 * Erzeugt über einen Parameterwert eine 
 * entsprechende {@link Gebühr}.
 * 
 * @author Thomas Nill
 *
 */
@FunctionalInterface
public interface GebührFabrik {
    Gebühr createGebühr(double parameter);
}
