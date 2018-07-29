package boundingContext.buchhaltung.eingang;

import boundingContext.abrechnung.aufz‰hlungen.SachKonto;
import boundingContext.abrechnung.aufz‰hlungen.SachKontoProvider;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.MandantRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.abrechnung.repositories.ZahlungsDefinitionRepository;
import boundingContext.abrechnung.repositories.‹berweisungRepository;

public class SachKontoDelegate implements SachKontoProvider {
    private SachKontoProvider sachKontoProvider;

    public SachKontoDelegate(SachKontoProvider sachKontoProvider) {
        super();
        this.sachKontoProvider = sachKontoProvider;
    }

    public SachKonto GEB‹HR() {
        return sachKontoProvider.GEB‹HR();
    }

    public SachKonto GUTHABEN() {
        return sachKontoProvider.GUTHABEN();
    }

    public SachKonto AUSZUZAHLEN() {
        return sachKontoProvider.AUSZUZAHLEN();
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

    public MandantRepository getMandantRepository() {
        return sachKontoProvider.getMandantRepository();
    }

    public AbrechnungRepository getAbrechnungRepository() {
        return sachKontoProvider.getAbrechnungRepository();
    }

    public BuchungRepository getBuchungRepository() {
        return sachKontoProvider.getBuchungRepository();
    }


    public ZahlungsAuftragRepository getZahlungsAuftragRepository() {
        return sachKontoProvider.getZahlungsAuftragRepository();
    }

    public ZahlungsDefinitionRepository getZahlungsDefinitionRepository() {
        return sachKontoProvider.getZahlungsDefinitionRepository();
    }

    public ‹berweisungRepository get‹berweisungRepository() {
        return sachKontoProvider.get‹berweisungRepository();
    }


}
