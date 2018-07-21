package boundingContext.abrechnung.actions;

import boundingContext.abrechnung.aufz‰hlungen.BuchungsArt;
import boundingContext.abrechnung.aufz‰hlungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.buchhaltung.eingang.EinBucher;
import boundingContext.zahlungen.actions.ZahlungenEntfernen;

public class AbrechnungAbschlieﬂen extends EinBucher {

    private ZahlungenEntfernen zahlungenEntfernen;
    private SchuldenInDieAbrechnung schulden‹bertragen;
    private SaldoAusgleichen ausgleichen;

    public AbrechnungAbschlieﬂen(SachKontoProvider sachKontoProvider,
            BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository,
            AbrechnungRepository abrechnungRepository,
            ZahlungsAuftragRepository zahlungsAuftragRepository, double zinssatz) {
        super(sachKontoProvider, buchungRepository, kontoBewegungRepository,
                abrechnungRepository);

        zahlungenEntfernen = new ZahlungenEntfernen(sachKontoProvider,
                buchungRepository, kontoBewegungRepository,
                zahlungsAuftragRepository, abrechnungRepository);

        ausgleichen = new SaldoAusgleichen(sachKontoProvider,
                buchungRepository, kontoBewegungRepository,
                abrechnungRepository, "Guthaben", "Schulden");

        schulden‹bertragen = new SchuldenInDieAbrechnung(sachKontoProvider,
                buchungRepository, kontoBewegungRepository,
                abrechnungRepository, "Schulden ¸bernehmen", zinssatz);

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
