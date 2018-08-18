package org.nill.abrechnung.tests.konten;

import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.abrechnung.entities.AusgangsDatei;
import org.nill.abrechnung.entities.Buchung;
import org.nill.abrechnung.entities.ZahlungsAuftrag;
import org.nill.abrechnung.entities.Überweisung;
import org.nill.abrechnung.interfaces.IAbrechnungRepository;
import org.nill.abrechnung.interfaces.IAusgangsDatei;
import org.nill.abrechnung.interfaces.IAusgangsDateiRepository;
import org.nill.abrechnung.interfaces.IBuchung;
import org.nill.abrechnung.interfaces.IBuchungsRepository;
import org.nill.abrechnung.interfaces.IMandantRepository;
import org.nill.abrechnung.interfaces.IParameterRepository;
import org.nill.abrechnung.interfaces.IZahlungsAuftrag;
import org.nill.abrechnung.interfaces.IZahlungsAuftragRepository;
import org.nill.abrechnung.interfaces.IÜberweisung;
import org.nill.abrechnung.interfaces.IÜberweisungRepository;
import org.nill.abrechnung.interfaces.Umgebung;

public class TestUmgebung implements Umgebung {

    private IMandantRepository mandantRepository;
    private IAbrechnungRepository abrechnungRepository;

    private IBuchungsRepository buchungRepository;

    private IZahlungsAuftragRepository zahlungsAuftragRepository;

    private IÜberweisungRepository überweisungRepository;

    private IParameterRepository parameterRepository;

    private IAusgangsDateiRepository ausgangsDateiRepository;

    public TestUmgebung(IMandantRepository mandantRepository,
            IAbrechnungRepository abrechnungRepository,
            IBuchungsRepository buchungRepository,
            IZahlungsAuftragRepository zahlungsAuftragRepository,
            IÜberweisungRepository überweisungRepository,
            IParameterRepository parameterRepository,
            IAusgangsDateiRepository ausgangsDateiRepository) {
        super();
        this.mandantRepository = mandantRepository;
        this.abrechnungRepository = abrechnungRepository;
        this.buchungRepository = buchungRepository;
        this.zahlungsAuftragRepository = zahlungsAuftragRepository;
        this.ausgangsDateiRepository = ausgangsDateiRepository;
        this.überweisungRepository = überweisungRepository;
        this.parameterRepository = parameterRepository;
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
     * @see org.nill.abrechnung.tests.konten.RepositoryProvider#getMandantRepository()
     */
    @Override
    public IMandantRepository getMandantRepository() {
        return mandantRepository;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nill.abrechnung.tests.konten.RepositoryProvider#getAbrechnungRepository()
     */
    @Override
    public IAbrechnungRepository getAbrechnungRepository() {
        return abrechnungRepository;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nill.abrechnung.tests.konten.RepositoryProvider#getBuchungRepository()
     */
    @Override
    public IBuchungsRepository getBuchungRepository() {
        return buchungRepository;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nill.abrechnung.tests.konten.RepositoryProvider#getZahlungsAuftragRepository()
     */
    @Override
    public IZahlungsAuftragRepository getZahlungsAuftragRepository() {
        return zahlungsAuftragRepository;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nill.abrechnung.tests.konten.RepositoryProvider#getÜberweisungRepository()
     */
    @Override
    public IÜberweisungRepository getÜberweisungRepository() {
        return überweisungRepository;
    }

    @Override
    public IParameterRepository getParameterRepository() {
        return parameterRepository;
    }

    @Override
    public IAusgangsDateiRepository getAusgangsDateiRepository() {
        return ausgangsDateiRepository;
    }

    @Override
    public IBuchung createBuchung() {
        return new Buchung();
    }

    @Override
    public IAusgangsDatei createAusgangsDatei() {
      return new AusgangsDatei();
    }

    @Override
    public IZahlungsAuftrag createZahlungsAuftrag() {
       return new ZahlungsAuftrag();
    }

    @Override
    public IÜberweisung createÜberweisung() {
        return new Überweisung();
    }

}
