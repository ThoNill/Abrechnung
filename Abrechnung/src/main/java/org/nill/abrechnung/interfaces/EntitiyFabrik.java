package org.nill.abrechnung.interfaces;

/**
 * Erzeugt geeignete Entities f�r eine {@link Umgebung}
 * 
 * @author Thomas Nill
 *
 */
public interface EntitiyFabrik {
        IBuchung createBuchung();
        IAusgangsDatei createAusgangsDatei();
        IZahlungsAuftrag createZahlungsAuftrag();
        I�berweisung create�berweisung();
}
