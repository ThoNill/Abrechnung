package boundingContext.abrechnung.actions;

import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.buchhaltung.eingang.EinBucher;
import boundingContext.zahlungen.actions.ZahlungenEntfernen;

public class AbrechnungAbschließen extends EinBucher {

    private ZahlungenEntfernen zahlungenEntfernen;
    private SchuldenInDieAbrechnung schuldenÜbertragen;
    private SaldoAusgleichen ausgleichen;

    public AbrechnungAbschließen(SachKontoProvider sachKontoProvider,
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

        schuldenÜbertragen = new SchuldenInDieAbrechnung(sachKontoProvider, "Schulden übernehmen", zinssatz);

    }

    public Abrechnung abschleißen(Abrechnung abrechnung, int zinsDauer) {

        zahlungenEntfernen
                .entferneZahlungsaufträgeFallsRestguthaben(abrechnung);
        ausgleichen.saldoAusgleichen(abrechnung);
        Abrechnung nächsteAbrechnung = abrechnung.createOrGetNächsteAbrechnung(this);
        schuldenÜbertragen.übertragen(nächsteAbrechnung, zinsDauer);
        return nächsteAbrechnung;
    }
}
