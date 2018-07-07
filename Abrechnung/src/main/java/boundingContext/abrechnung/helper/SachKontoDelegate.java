package boundingContext.abrechnung.helper;

import boundingContext.abrechnung.aufz�hlungen.SachKonto;
import boundingContext.abrechnung.aufz�hlungen.SachKontoProvider;

public class SachKontoDelegate {
    private SachKontoProvider sachKontoProvider;

    public SachKontoDelegate(SachKontoProvider sachKontoProvider) {
        super();
        this.sachKontoProvider = sachKontoProvider;
    }

    public SachKonto GEB�HR() {
        return sachKontoProvider.GEB�HR();
    }

    public SachKonto GUTHABEN() {
        return sachKontoProvider.GUTHABEN();
    }

    public SachKonto AUSBEZAHLT() {
        return sachKontoProvider.AUSBEZAHLT();
    }
    
    public SachKonto SCHULDEN() {
        return sachKontoProvider.SCHULDEN();
    }

    public SachKonto ZINS() {
        return sachKontoProvider.ZINS();
    }

    public SachKonto MWST() {
        return sachKontoProvider.MWST();
    }

    public SachKonto sachKontoFrom(int pos) {
        return sachKontoProvider.sachKontoFrom(pos);
    }

}
