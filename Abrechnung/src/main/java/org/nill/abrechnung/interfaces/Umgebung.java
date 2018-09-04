package org.nill.abrechnung.interfaces;

import org.nill.abrechnung.aufzählungen.BuchungsArt;
import org.nill.abrechnung.aufzählungen.SachKonto;

/**
 * Die {@link Umgebung} stellt verschiedene Sachkonten für Buchungen,
 * ein paar Buchungstypen zur Verfügung, erzeugt Entities
 * und stellt Repositorys zur Verfügung.
 * 
 * Diese Klasse abstrahiert also gewissermaßen die Ablaufumgebung der Abrechnung 
 * 
 * @author Thomas Nill
 *
 */
public interface Umgebung extends RepositoryProvider, EntitiyFabrik {
    SachKonto GEBÜHR();

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

    default int ÜBERNAHME_SCHULDEN() {
        return BuchungsArt.ÜBERNAHME_SCHULDEN;
    }

}
