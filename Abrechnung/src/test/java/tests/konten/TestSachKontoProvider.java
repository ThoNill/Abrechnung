package tests.konten;

import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.aufzählungen.SachKontoProvider;

public class TestSachKontoProvider implements SachKontoProvider {

    public TestSachKontoProvider() {
        super();
    }

    @Override
    public SachKonto GEBÜHR() {
        return TestSachKonto.GEBÜHR;
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
