package tests;

import java.util.Optional;

import javax.money.MonetaryAmount;

import app.entities.Abrechnung;
import app.helper.AbrechnungHelper;
import app.repositories.AbrechnungRepository;
import app.repositories.BuchungRepository;
import app.repositories.KontoBewegungRepository;
import betrag.Geld;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.gemeinsam.BetragsBündelMap;

public class SchuldenInDieAbrechnung extends EinBucher {

    public SchuldenInDieAbrechnung(BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository,
            AbrechnungRepository abrechnungRepository,
            int buchungstypSchuldenStart, int buchungstypSchulden,
            Position kontonrSchulden, Position kontonrZinsen, String text,
            double zinssatz) {
        super(buchungRepository, kontoBewegungRepository);
        this.buchungstypSchuldenStart = buchungstypSchuldenStart;
        this.kontonrZinsen = kontonrZinsen;
        this.text = text;
        this.buchungstypSchulden = buchungstypSchulden;
        this.kontonrSchulden = kontonrSchulden;
        this.abrechnungRepository = abrechnungRepository;
        this.zinssatz = zinssatz;
    }

    private int buchungstypSchuldenStart;
    private Position kontonrZinsen;
    private String text;

    private int buchungstypSchulden;
    private Position kontonrSchulden;
    private AbrechnungRepository abrechnungRepository;
    private double zinssatz;

    public void übertragen(Abrechnung abrechnung, int zinsDauer) {
        AbrechnungHelper h = new AbrechnungHelper(abrechnungRepository);
        Optional<Abrechnung> oAbrechnung = h.getVorherigeAbrechnung(abrechnung);
        if (oAbrechnung.isPresent()) {
            MonetaryAmount saldo = buchungRepository.getSumKonto(
                    oAbrechnung.get(), buchungstypSchuldenStart,
                    kontonrSchulden.ordinal());
            if (saldo != null) {
                buche(abrechnung, saldo.negate(), zinsDauer);
            }
        }
    }

    private void buche(Abrechnung abrechnung, MonetaryAmount betrag,
            int zinsDauer) {
        BuchungsAuftrag<Position> auftrag = erzeugeBuchungsAuftrag(betrag,
                zinsDauer);
        erzeugeDifferenzBuchung(auftrag, abrechnung);
    }

    private BuchungsAuftrag<Position> erzeugeBuchungsAuftrag(
            MonetaryAmount betrag, int zinsDauer) {
        BetragsBündelMap<Position> beträge = new BetragsBündelMap<>();
        beträge.put(kontonrSchulden, betrag);
        MonetaryAmount zins = berechneZins(betrag, zinsDauer);
        beträge.put(kontonrZinsen, zins);
        Beschreibung beschreibung = new Beschreibung(buchungstypSchulden, text);
        return new BuchungsAuftrag<Position>(beschreibung, beträge);
    }

    private MonetaryAmount berechneZins(MonetaryAmount betrag, int zinsDauer) {
        return Geld.round(betrag.multiply(zinsDauer).multiply(zinssatz)
                .divide(360));
    }

}
