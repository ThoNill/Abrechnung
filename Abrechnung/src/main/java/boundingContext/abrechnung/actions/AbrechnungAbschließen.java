package boundingContext.abrechnung.actions;

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
        super(sachKontoProvider);

        zahlungenEntfernen = new ZahlungenEntfernen(sachKontoProvider,
                buchungRepository, kontoBewegungRepository,
                zahlungsAuftragRepository, abrechnungRepository);

        ausgleichen = new SaldoAusgleichen(sachKontoProvider,
                buchungRepository, kontoBewegungRepository,
                abrechnungRepository, "Guthaben", "Schulden");

        schulden‹bertragen = new SchuldenInDieAbrechnung(sachKontoProvider, "Schulden ¸bernehmen", zinssatz);

    }

    public Abrechnung abschleiﬂen(Abrechnung abrechnung, int zinsDauer) {

        zahlungenEntfernen
                .entferneZahlungsauftr‰geFallsRestguthaben(abrechnung);
        ausgleichen.saldoAusgleichen(abrechnung);
        Abrechnung n‰chsteAbrechnung = abrechnung.createOrGetN‰chsteAbrechnung(this);
        schulden‹bertragen.¸bertragen(n‰chsteAbrechnung, zinsDauer);
        return n‰chsteAbrechnung;
    }
}
