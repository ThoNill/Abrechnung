package org.nill.abrechnung.interfaces;

/**
 * Erzeugt geeignete Entities für eine {@link Umgebung}
 * 
 * @author Thomas Nill
 *
 */
public interface EntitiyFabrik {
        IBuchung createBuchung();
        IAusgangsDatei createAusgangsDatei();
        IZahlungsAuftrag createZahlungsAuftrag();
        IÜberweisung createÜberweisung();
}
