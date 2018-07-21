package tests.konten;

import boundingContext.abrechnung.aufz�hlungen.SachKonto;
import boundingContext.abrechnung.aufz�hlungen.SachKontoProvider;

public class TestSachKontoProvider implements SachKontoProvider {

    public TestSachKontoProvider() {
        super();
    }

    @Override
    public SachKonto GEB�HR() {
        return TestSachKonto.GEB�HR;
    }

    @Override
    public SachKonto GUTHABEN() {
        return TestSachKonto.GUTHABEN;
    }

    @Override
    public SachKonto AUSZUZAHLEN() {
        return TestSachKonto.AUSZUZAHLEN;
    }

    
    @Override
    public SachKonto AUSBEZAHLT() {
        return TestSachKonto.AUSBEZAHLT;
    }

    @Override
    public SachKonto SCHULDEN() {
        return TestSachKonto.SCHULDEN;
    }

    @Override
    public SachKonto ZINS() {
        return TestSachKonto.ZINS;
    }

    @Override
    public SachKonto MWST() {
        return TestSachKonto.MWST;
    }

    @Override
    public SachKonto sachKontoFrom(int pos) {
        return TestSachKonto.values()[pos];
    }

}
