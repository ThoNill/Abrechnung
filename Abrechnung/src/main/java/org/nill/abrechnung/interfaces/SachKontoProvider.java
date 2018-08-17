package org.nill.abrechnung.interfaces;

import org.nill.abrechnung.aufz�hlungen.BuchungsArt;
import org.nill.abrechnung.aufz�hlungen.SachKonto;

public interface SachKontoProvider extends RepositoryProvider, EntitiyFabrik {
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
    };

    default int ABGLEICH_SCHULDEN() {
        return BuchungsArt.ABGLEICH_SCHULDEN;
    };

    default int �BERNAHME_SCHULDEN() {
        return BuchungsArt.�BERNAHME_SCHULDEN;
    };

}
