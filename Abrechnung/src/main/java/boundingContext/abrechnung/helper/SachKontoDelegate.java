package boundingContext.abrechnung.helper;

import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.aufzählungen.SachKontoProvider;

public class SachKontoDelegate {
    private SachKontoProvider sachKontoProvider;

    public SachKontoDelegate(SachKontoProvider sachKontoProvider) {
        super();
        this.sachKontoProvider = sachKontoProvider;
    }

    public SachKonto GEBÜHR() {
        return sachKontoProvider.GEBÜHR();
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
