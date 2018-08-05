package org.nill.abrechnung.aufz�hlungen;

public interface SachKontoProvider extends RepositoryProvider {
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
