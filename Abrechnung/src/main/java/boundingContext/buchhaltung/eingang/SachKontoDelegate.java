package boundingContext.buchhaltung.eingang;

import boundingContext.abrechnung.aufz‰hlungen.SachKonto;
import boundingContext.abrechnung.aufz‰hlungen.SachKontoProvider;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.AusgangsDateiRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.MandantRepository;
import boundingContext.abrechnung.repositories.ParameterRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.abrechnung.repositories.‹berweisungRepository;

public class SachKontoDelegate implements SachKontoProvider {
    private SachKontoProvider sachKontoProvider;

    public SachKontoDelegate(SachKontoProvider sachKontoProvider) {
        super();
        this.sachKontoProvider = sachKontoProvider;
    }

    @Override
    public SachKonto GEB‹HR() {
        return sachKontoProvider.GEB‹HR();
    }

    @Override
    public SachKonto GUTHABEN() {
        return sachKontoProvider.GUTHABEN();
    }

    @Override
    public SachKonto AUSZUZAHLEN() {
        return sachKontoProvider.AUSZUZAHLEN();
    }

    @Override
    public SachKonto AUSBEZAHLT() {
        return sachKontoProvider.AUSBEZAHLT();
    }

    @Override
    public SachKonto SCHULDEN() {
        return sachKontoProvider.SCHULDEN();
    }

    @Override
    public SachKonto ZINS() {
        return sachKontoProvider.ZINS();
    }

    @Override
    public SachKonto MWST() {
        return sachKontoProvider.MWST();
    }

    @Override
    public SachKonto sachKontoFrom(int pos) {
        return sachKontoProvider.sachKontoFrom(pos);
    }

    @Override
    public MandantRepository getMandantRepository() {
        return sachKontoProvider.getMandantRepository();
    }

    @Override
    public AbrechnungRepository getAbrechnungRepository() {
        return sachKontoProvider.getAbrechnungRepository();
    }

    @Override
    public BuchungRepository getBuchungRepository() {
        return sachKontoProvider.getBuchungRepository();
    }

    @Override
    public ZahlungsAuftragRepository getZahlungsAuftragRepository() {
        return sachKontoProvider.getZahlungsAuftragRepository();
    }

    @Override
    public ‹berweisungRepository get‹berweisungRepository() {
        return sachKontoProvider.get‹berweisungRepository();
    }

    @Override
    public ParameterRepository getParameterRepository() {
        return sachKontoProvider.getParameterRepository();
    }

    @Override
    public AusgangsDateiRepository getAusgangsDateiRepository() {
        return sachKontoProvider.getAusgangsDateiRepository();
    }

}
