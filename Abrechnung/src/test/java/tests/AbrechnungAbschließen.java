package tests;

import app.entities.Abrechnung;
import app.helper.AbrechnungHelper;
import app.repositories.AbrechnungRepository;
import app.repositories.BuchungRepository;
import app.repositories.KontoBewegungRepository;

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
                Position.GUTHABEN, "Guthaben", BuchungsArt.ABGLEICH_SCHULDEN,
                Position.SCHULDEN, "Schulden");

        schulden�bertragen = new SchuldenInDieAbrechnung(buchungRepository,
                kontoBewegungRepository, abrechnungRepository,
                BuchungsArt.ABGLEICH_SCHULDEN, BuchungsArt.�BERNAHME_SCHULDEN,
                Position.SCHULDEN, Position.ZINS, "Schulden �bernehmen",
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
