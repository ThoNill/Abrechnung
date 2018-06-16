package tests;

import app.entities.Abrechnung;
import app.helper.AbrechnungHelper;
import app.repositories.AbrechnungRepository;
import app.repositories.BuchungRepository;
import app.repositories.KontoBewegungRepository;

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
                Position.GUTHABEN, "Guthaben", BuchungsArt.ABGLEICH_SCHULDEN,
                Position.SCHULDEN, "Schulden");

        schuldenÜbertragen = new SchuldenInDieAbrechnung(buchungRepository,
                kontoBewegungRepository, abrechnungRepository,
                BuchungsArt.ABGLEICH_SCHULDEN, BuchungsArt.ÜBERNAHME_SCHULDEN,
                Position.SCHULDEN, Position.ZINS, "Schulden übernehmen",
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
