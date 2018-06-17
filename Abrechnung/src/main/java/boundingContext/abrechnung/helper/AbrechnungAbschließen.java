package boundingContext.abrechnung.helper;

import boundingContext.abrechnung.aufz‰hlungen.BuchungsArt;
import boundingContext.abrechnung.aufz‰hlungen.SachKonto;
import boundingContext.abrechnung.aufz‰hlungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;

public class AbrechnungAbschlieﬂen extends EinBucher {

    private SchuldenInDieAbrechnung schulden‹bertragen;
    private SaldoAusgleichen ausgleichen;
    private AbrechnungRepository abrechnungRepository;
 
    public AbrechnungAbschlieﬂen(SachKontoProvider sachKontoProvider,BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository,
            AbrechnungRepository abrechnungRepository,
            double zinssatz) {
        super(sachKontoProvider,buchungRepository, kontoBewegungRepository);
        this.abrechnungRepository = abrechnungRepository;
 
        ausgleichen = new SaldoAusgleichen(sachKontoProvider,buchungRepository,
                kontoBewegungRepository, BuchungsArt.ABGLEICH_GUTHABEN,
                GUTHABEN(), "Guthaben", BuchungsArt.ABGLEICH_SCHULDEN,
                SCHULDEN(), "Schulden");

        schulden‹bertragen = new SchuldenInDieAbrechnung(sachKontoProvider,buchungRepository,
                kontoBewegungRepository, abrechnungRepository,
                BuchungsArt.ABGLEICH_SCHULDEN, BuchungsArt.‹BERNAHME_SCHULDEN,
                SCHULDEN(), ZINS(), "Schulden ¸bernehmen",
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
