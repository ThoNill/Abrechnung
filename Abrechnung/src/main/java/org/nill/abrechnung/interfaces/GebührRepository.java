package org.nill.abrechnung.interfaces;

import javax.money.MonetaryAmount;

import org.nill.basiskomponenten.gemeinsam.BetragsBündel;

/**
 * Dieses Interface ist für die Gebührenberechnung zentral, es stellt die Daten
 * zur Verfügung, die dann zur Berechnung der Gebühren und zur Erzeugung der
 * Buchungen der Abrechnung notwendig sind.
 * 
 * Je GebührRepository gibt es eine Buchung inklusive berechneter Gebühr und
 * Mehrwertsteuer.
 * 
 */
public interface GebührRepository<KONTO> {

    /**
     * Diese Methode soll die Datengrundlage, auf denen dieses Repository
     * basiert für eine übergebene Abrechnung markieren. Die entsprechenden
     * Datensätze in einer Datenbank gehören dann zu dieser Abrechnung und
     * können in getBeträge aufsummiert werden.
     * 
     * @param abrechnung
     */
    void markieren(IAbrechnung abrechnung);

    /**
     * Die Beträge werden in die Abrechung als Positionen einer Buchung
     * übernommen KONTO stellt eine Referenzen auf ein Konto dar.
     */
    BetragsBündel<KONTO> getBeträge(IAbrechnung abrechnung);

    /**
     * Das ist der Basiswert um eine {@link Gebühr} für die von diesem
     * Repository verwalteten Werte zu berechnen.
     * 
     * @param abrechnung
     * @return
     */
    MonetaryAmount getGebührenBasis(IAbrechnung abrechnung);

}
