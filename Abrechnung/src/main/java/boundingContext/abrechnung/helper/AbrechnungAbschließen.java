package boundingContext.abrechnung.helper;

import boundingContext.abrechnung.aufzählungen.BuchungsArt;
import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.buchhaltung.eingang.EinBucher;
import boundingContext.zahlungen.helper.ZahlungenEntfernenManager;

public class AbrechnungAbschließen extends EinBucher {

    private ZahlungenEntfernenManager zahlungenEntfernen;
    private SchuldenInDieAbrechnung schuldenÜbertragen;
    private SaldoAusgleichen ausgleichen;
    private AbrechnungRepository abrechnungRepository;
 
    public AbrechnungAbschließen(SachKontoProvider sachKontoProvider,BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository,
            AbrechnungRepository abrechnungRepository,
            ZahlungsAuftragRepository zahlungsAuftragRepository,
            double zinssatz) {
        super(sachKontoProvider,buchungRepository, kontoBewegungRepository);
        this.abrechnungRepository = abrechnungRepository;
        
        zahlungenEntfernen = new ZahlungenEntfernenManager(sachKontoProvider, buchungRepository, kontoBewegungRepository, 
                zahlungsAuftragRepository, 
                BuchungsArt.ABGLEICH_GUTHABEN, 
                GUTHABEN());
 
        ausgleichen = new SaldoAusgleichen(sachKontoProvider,buchungRepository,
                kontoBewegungRepository, BuchungsArt.ABGLEICH_GUTHABEN,
                GUTHABEN(), "Guthaben", BuchungsArt.ABGLEICH_SCHULDEN,
                SCHULDEN(), "Schulden");

        schuldenÜbertragen = new SchuldenInDieAbrechnung(sachKontoProvider,buchungRepository,
                kontoBewegungRepository, abrechnungRepository,
                BuchungsArt.ABGLEICH_SCHULDEN, BuchungsArt.ÜBERNAHME_SCHULDEN,
                SCHULDEN(), ZINS(), "Schulden übernehmen",
                zinssatz);

    }

    public Abrechnung abschleißen(Abrechnung abrechnung, int zinsDauer) {
        zahlungenEntfernen.entferneZahlungsaufträgeFallsRestguthaben(abrechnung);
        ausgleichen.saldoAusgleichen(abrechnung);
        AbrechnungHelper h = new AbrechnungHelper(abrechnungRepository);
        Abrechnung nächsteAbrechnung = h
                .createOrGetNächsteAbrechnung(abrechnung);
        schuldenÜbertragen.übertragen(nächsteAbrechnung, zinsDauer);
        return nächsteAbrechnung;
    }
}
