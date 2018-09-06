package org.nill.abrechnung.interfaces;

import javax.money.MonetaryAmount;

import org.nill.basiskomponenten.gemeinsam.BetragsB�ndel;

/**
 * Dieses Interface ist f�r die Geb�hrenberechnung zentral, es stellt die Daten
 * zur Verf�gung, die dann zur Berechnung der Geb�hren und zur Erzeugung der
 * Buchungen der Abrechnung notwendig sind.
 * 
 * Je Geb�hrRepository gibt es eine Buchung inklusive berechneter Geb�hr und
 * Mehrwertsteuer.
 * 
 */
public interface Geb�hrRepository<KONTO> {

    /**
     * Diese Methode soll die Datengrundlage, auf denen dieses Repository
     * basiert f�r eine �bergebene Abrechnung markieren. Die entsprechenden
     * Datens�tze in einer Datenbank geh�ren dann zu dieser Abrechnung und
     * k�nnen in getBetr�ge aufsummiert werden.
     * 
     * @param abrechnung
     */
    void markieren(IAbrechnung abrechnung);

    /**
     * Die Betr�ge werden in die Abrechung als Positionen einer Buchung
     * �bernommen KONTO stellt eine Referenzen auf ein Konto dar.
     */
    BetragsB�ndel<KONTO> getBetr�ge(IAbrechnung abrechnung);

    /**
     * Das ist der Basiswert um eine {@link Geb�hr} f�r die von diesem
     * Repository verwalteten Werte zu berechnen.
     * 
     * @param abrechnung
     * @return
     */
    MonetaryAmount getGeb�hrenBasis(IAbrechnung abrechnung);

}
