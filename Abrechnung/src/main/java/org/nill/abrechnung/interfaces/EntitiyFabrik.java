package org.nill.abrechnung.interfaces;


public interface EntitiyFabrik {
        IBuchung createBuchung();
        IAusgangsDatei createAusgangsDatei();
        IZahlungsAuftrag createZahlungsAuftrag();
        I‹berweisung create‹berweisung();
}
