package boundaryContext.abrechnung.helper;

import boundaryContext.abrechnung.entities.Abrechnung;
import boundaryContext.abrechnung.repositories.AbrechnungRepository;
import boundaryContext.abrechnung.repositories.BuchungRepository;
import boundaryContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.abrechnung.aufz‰hlungen.BuchungsArt;
import boundingContext.abrechnung.aufz‰hlungen.Position;

public class AbrechnungAbschlieﬂen extends EinBucher {

    private SchuldenInDieAbrechnung schulden‹bertragen;
    private SaldoAusgleichen ausgleichen;
    private AbrechnungRepository abrechnungRepository;

    public AbrechnungAbschlieﬂen(BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository,
            AbrechnungRepository abrechnungRepository, double zinssatz) {
        super(buchungRepository, kontoBewegungRepository);
        this.abrechnungRepository = abrechnungRepository;

        ausgleichen = new SaldoAusgleichen(buchungRepository,
                kontoBewegungRepository, BuchungsArt.ABGLEICH_GUTHABEN,
                Position.GUTHABEN, "Guthaben", BuchungsArt.ABGLEICH_SCHULDEN,
                Position.SCHULDEN, "Schulden");

        schulden‹bertragen = new SchuldenInDieAbrechnung(buchungRepository,
                kontoBewegungRepository, abrechnungRepository,
                BuchungsArt.ABGLEICH_SCHULDEN, BuchungsArt.‹BERNAHME_SCHULDEN,
                Position.SCHULDEN, Position.ZINS, "Schulden ¸bernehmen",
                zinssatz);

    }

    public Abrechnung abschleiﬂen(Abrechnung abrechnung, int zinsDauer) {
        ausgleichen.saldoAusgleichen(abrechnung);
        AbrechnungHelper h = new AbrechnungHelper(abrechnungRepository);
        Abrechnung n‰chsteAbrechnung = h
                .createOrGetN‰chsteAbrechnung(abrechnung);
        schulden‹bertragen.¸bertragen(n‰chsteAbrechnung, zinsDauer);
        return n‰chsteAbrechnung;
    }
}
