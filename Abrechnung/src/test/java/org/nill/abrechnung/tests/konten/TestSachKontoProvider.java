package org.nill.abrechnung.tests.konten;

import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.abrechnung.aufz�hlungen.SachKontoProvider;
import org.nill.abrechnung.repositories.AbrechnungRepository;
import org.nill.abrechnung.repositories.AusgangsDateiRepository;
import org.nill.abrechnung.repositories.BuchungRepository;
import org.nill.abrechnung.repositories.MandantRepository;
import org.nill.abrechnung.repositories.ParameterRepository;
import org.nill.abrechnung.repositories.ZahlungsAuftragRepository;
import org.nill.abrechnung.repositories.�berweisungRepository;

public class TestSachKontoProvider implements SachKontoProvider {

    private MandantRepository mandantRepository;
    private AbrechnungRepository abrechnungRepository;

    private BuchungRepository buchungRepository;

    private ZahlungsAuftragRepository zahlungsAuftragRepository;

    private �berweisungRepository �berweisungRepository;

    private ParameterRepository parameterRepository;

    private AusgangsDateiRepository ausgangsDateiRepository;

    public TestSachKontoProvider(MandantRepository mandantRepository,
            AbrechnungRepository abrechnungRepository,
            BuchungRepository buchungRepository,
            ZahlungsAuftragRepository zahlungsAuftragRepository,
            �berweisungRepository �berweisungRepository,
            ParameterRepository parameterRepository,
            AusgangsDateiRepository ausgangsDateiRepository) {
        super();
        this.mandantRepository = mandantRepository;
        this.abrechnungRepository = abrechnungRepository;
        this.buchungRepository = buchungRepository;
        this.zahlungsAuftragRepository = zahlungsAuftragRepository;
        this.ausgangsDateiRepository = ausgangsDateiRepository;
        this.�berweisungRepository = �berweisungRepository;
        this.parameterRepository = parameterRepository;
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

    /*
     * (non-Javadoc)
     * 
     * @see org.nill.abrechnung.tests.konten.RepositoryProvider#getMandantRepository()
     */
    @Override
    public MandantRepository getMandantRepository() {
        return mandantRepository;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nill.abrechnung.tests.konten.RepositoryProvider#getAbrechnungRepository()
     */
    @Override
    public AbrechnungRepository getAbrechnungRepository() {
        return abrechnungRepository;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nill.abrechnung.tests.konten.RepositoryProvider#getBuchungRepository()
     */
    @Override
    public BuchungRepository getBuchungRepository() {
        return buchungRepository;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nill.abrechnung.tests.konten.RepositoryProvider#getZahlungsAuftragRepository()
     */
    @Override
    public ZahlungsAuftragRepository getZahlungsAuftragRepository() {
        return zahlungsAuftragRepository;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nill.abrechnung.tests.konten.RepositoryProvider#get�berweisungRepository()
     */
    @Override
    public �berweisungRepository get�berweisungRepository() {
        return �berweisungRepository;
    }

    @Override
    public ParameterRepository getParameterRepository() {
        return parameterRepository;
    }

    @Override
    public AusgangsDateiRepository getAusgangsDateiRepository() {
        return ausgangsDateiRepository;
    }

}
