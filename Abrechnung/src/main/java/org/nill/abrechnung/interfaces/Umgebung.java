package org.nill.abrechnung.interfaces;

import org.nill.abrechnung.aufz�hlungen.BuchungsArt;
import org.nill.abrechnung.aufz�hlungen.SachKonto;

/**
 * Die {@link Umgebung} stellt verschiedene Sachkonten f�r Buchungen,
 * ein paar Buchungstypen zur Verf�gung, erzeugt Entities
 * und stellt Repositorys zur Verf�gung.
 * 
 * Diese Klasse abstrahiert also gewisserma�en die Ablaufumgebung der Abrechnung 
 * 
 * @author Thomas Nill
 *
 */
public interface Umgebung extends RepositoryProvider, EntitiyFabrik {
    SachKonto GEB�HR();

    SachKonto GUTHABEN();

    SachKonto AUSZUZAHLEN();

    SachKonto AUSBEZAHLT();

    SachKonto SCHULDEN();

    SachKonto ZINS();

    SachKonto MWST();

    SachKonto sachKontoFrom(int pos);

    default int ABGLEICH_GUTHABEN() {
        return BuchungsArt.ABGLEICH_GUTHABEN;
    }

    default int ABGLEICH_SCHULDEN() {
        return BuchungsArt.ABGLEICH_SCHULDEN;
    }

    default int �BERNAHME_SCHULDEN() {
        return BuchungsArt.�BERNAHME_SCHULDEN;
    }

}
