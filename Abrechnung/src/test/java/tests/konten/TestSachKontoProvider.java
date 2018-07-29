package tests.konten;

import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.MandantRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.abrechnung.repositories.ZahlungsDefinitionRepository;
import boundingContext.abrechnung.repositories.ÜberweisungRepository;

public class TestSachKontoProvider implements SachKontoProvider {

    private MandantRepository mandantRepository;
    private AbrechnungRepository abrechnungRepository;

    private BuchungRepository buchungRepository;

    private ZahlungsAuftragRepository zahlungsAuftragRepository;

    private ZahlungsDefinitionRepository zahlungsDefinitionRepository;

    private ÜberweisungRepository überweisungRepository;

    public TestSachKontoProvider(MandantRepository mandantRepository,
            AbrechnungRepository abrechnungRepository,
            BuchungRepository buchungRepository,
            ZahlungsAuftragRepository zahlungsAuftragRepository,
            ZahlungsDefinitionRepository zahlungsDefinitionRepository,
            ÜberweisungRepository überweisungRepository) {
        super();
        this.mandantRepository = mandantRepository;
        this.abrechnungRepository = abrechnungRepository;
        this.buchungRepository = buchungRepository;
        this.zahlungsAuftragRepository = zahlungsAuftragRepository;
        this.zahlungsDefinitionRepository = zahlungsDefinitionRepository;
        this.überweisungRepository = überweisungRepository;
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

    /*
     * (non-Javadoc)
     * 
     * @see tests.konten.RepositoryProvider#getMandantRepository()
     */
    @Override
    public MandantRepository getMandantRepository() {
        return mandantRepository;
    }

    /*
     * (non-Javadoc)
     * 
     * @see tests.konten.RepositoryProvider#getAbrechnungRepository()
     */
    @Override
    public AbrechnungRepository getAbrechnungRepository() {
        return abrechnungRepository;
    }

    /*
     * (non-Javadoc)
     * 
     * @see tests.konten.RepositoryProvider#getBuchungRepository()
     */
    @Override
    public BuchungRepository getBuchungRepository() {
        return buchungRepository;
    }


    /*
     * (non-Javadoc)
     * 
     * @see tests.konten.RepositoryProvider#getZahlungsAuftragRepository()
     */
    @Override
    public ZahlungsAuftragRepository getZahlungsAuftragRepository() {
        return zahlungsAuftragRepository;
    }

    /*
     * (non-Javadoc)
     * 
     * @see tests.konten.RepositoryProvider#getZahlungsDefinitionRepository()
     */
    @Override
    public ZahlungsDefinitionRepository getZahlungsDefinitionRepository() {
        return zahlungsDefinitionRepository;
    }

    /*
     * (non-Javadoc)
     * 
     * @see tests.konten.RepositoryProvider#getÜberweisungRepository()
     */
    @Override
    public ÜberweisungRepository getÜberweisungRepository() {
        return überweisungRepository;
    }

}
