package boundingContext.abrechnung.helper;

import boundingContext.abrechnung.aufz�hlungen.BuchungsArt;
import boundingContext.abrechnung.aufz�hlungen.SachKonto;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;

public class AbrechnungAbschlie�en extends EinBucher {

    private SchuldenInDieAbrechnung schulden�bertragen;
    private SaldoAusgleichen ausgleichen;
    private AbrechnungRepository abrechnungRepository;

    public AbrechnungAbschlie�en(BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository,
            AbrechnungRepository abrechnungRepository, double zinssatz) {
        super(buchungRepository, kontoBewegungRepository);
        this.abrechnungRepository = abrechnungRepository;

        ausgleichen = new SaldoAusgleichen(buchungRepository,
                kontoBewegungRepository, BuchungsArt.ABGLEICH_GUTHABEN,
                SachKonto.GUTHABEN, "Guthaben", BuchungsArt.ABGLEICH_SCHULDEN,
                SachKonto.SCHULDEN, "Schulden");

        schulden�bertragen = new SchuldenInDieAbrechnung(buchungRepository,
                kontoBewegungRepository, abrechnungRepository,
                BuchungsArt.ABGLEICH_SCHULDEN, BuchungsArt.�BERNAHME_SCHULDEN,
                SachKonto.SCHULDEN, SachKonto.ZINS, "Schulden �bernehmen",
                zinssatz);

    }

    public Abrechnung abschlei�en(Abrechnung abrechnung, int zinsDauer) {
        ausgleichen.saldoAusgleichen(abrechnung);
        AbrechnungHelper h = new AbrechnungHelper(abrechnungRepository);
        Abrechnung n�chsteAbrechnung = h
                .createOrGetN�chsteAbrechnung(abrechnung);
        schulden�bertragen.�bertragen(n�chsteAbrechnung, zinsDauer);
        return n�chsteAbrechnung;
    }
}
