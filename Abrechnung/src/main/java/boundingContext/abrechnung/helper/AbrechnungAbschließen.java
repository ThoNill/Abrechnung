package boundingContext.abrechnung.helper;

import boundingContext.abrechnung.aufz‰hlungen.BuchungsArt;
import boundingContext.abrechnung.aufz‰hlungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.buchhaltung.eingang.EinBucher;
import boundingContext.zahlungen.helper.ZahlungenEntfernenManager;

public class AbrechnungAbschlieﬂen extends EinBucher {

    private ZahlungenEntfernenManager zahlungenEntfernen;
    private SchuldenInDieAbrechnung schulden‹bertragen;
    private SaldoAusgleichen ausgleichen;

    public AbrechnungAbschlieﬂen(SachKontoProvider sachKontoProvider,
            BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository,
            AbrechnungRepository abrechnungRepository,
            ZahlungsAuftragRepository zahlungsAuftragRepository, double zinssatz) {
        super(sachKontoProvider, buchungRepository, kontoBewegungRepository,abrechnungRepository);
       

        zahlungenEntfernen = new ZahlungenEntfernenManager(sachKontoProvider,
                buchungRepository, kontoBewegungRepository,
                zahlungsAuftragRepository,abrechnungRepository, BuchungsArt.ABGLEICH_GUTHABEN,
                GUTHABEN());

        ausgleichen = new SaldoAusgleichen(sachKontoProvider,
                buchungRepository, kontoBewegungRepository,abrechnungRepository,
                BuchungsArt.ABGLEICH_GUTHABEN, GUTHABEN(), "Guthaben",
                BuchungsArt.ABGLEICH_SCHULDEN, SCHULDEN(), "Schulden");

        schulden‹bertragen = new SchuldenInDieAbrechnung(sachKontoProvider,
                buchungRepository, kontoBewegungRepository,
                abrechnungRepository, BuchungsArt.ABGLEICH_SCHULDEN,
                BuchungsArt.‹BERNAHME_SCHULDEN, SCHULDEN(), ZINS(),
                "Schulden ¸bernehmen", zinssatz);

    }

    public Abrechnung abschleiﬂen(Abrechnung abrechnung, int zinsDauer) {
        
        zahlungenEntfernen
                .entferneZahlungsauftr‰geFallsRestguthaben(abrechnung);
        ausgleichen.saldoAusgleichen(abrechnung);
        AbrechnungHelper h = new AbrechnungHelper(abrechnungRepository);
        Abrechnung n‰chsteAbrechnung = h
                .createOrGetN‰chsteAbrechnung(abrechnung);
        schulden‹bertragen.¸bertragen(n‰chsteAbrechnung, zinsDauer);
        return n‰chsteAbrechnung;
    }
}
