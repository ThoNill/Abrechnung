package boundingContext.abrechnung.helper;

import boundingContext.abrechnung.aufzählungen.BuchungsArt;
import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;

public class AbrechnungAbschließen extends EinBucher {

    private SchuldenInDieAbrechnung schuldenÜbertragen;
    private SaldoAusgleichen ausgleichen;
    private AbrechnungRepository abrechnungRepository;

    public AbrechnungAbschließen(BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository,
            AbrechnungRepository abrechnungRepository, double zinssatz) {
        super(buchungRepository, kontoBewegungRepository);
        this.abrechnungRepository = abrechnungRepository;

        ausgleichen = new SaldoAusgleichen(buchungRepository,
                kontoBewegungRepository, BuchungsArt.ABGLEICH_GUTHABEN,
                SachKonto.GUTHABEN, "Guthaben", BuchungsArt.ABGLEICH_SCHULDEN,
                SachKonto.SCHULDEN, "Schulden");

        schuldenÜbertragen = new SchuldenInDieAbrechnung(buchungRepository,
                kontoBewegungRepository, abrechnungRepository,
                BuchungsArt.ABGLEICH_SCHULDEN, BuchungsArt.ÜBERNAHME_SCHULDEN,
                SachKonto.SCHULDEN, SachKonto.ZINS, "Schulden übernehmen",
                zinssatz);

    }

    public Abrechnung abschleißen(Abrechnung abrechnung, int zinsDauer) {
        ausgleichen.saldoAusgleichen(abrechnung);
        AbrechnungHelper h = new AbrechnungHelper(abrechnungRepository);
        Abrechnung nächsteAbrechnung = h
                .createOrGetNächsteAbrechnung(abrechnung);
        schuldenÜbertragen.übertragen(nächsteAbrechnung, zinsDauer);
        return nächsteAbrechnung;
    }
}
