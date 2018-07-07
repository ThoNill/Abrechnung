package boundingContext.abrechnung.helper;

import java.util.Optional;

import javax.money.MonetaryAmount;

import betrag.Geld;
import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.buchhaltung.eingang.EinBucher;
import boundingContext.gemeinsam.BetragsBündelMap;

public class SchuldenInDieAbrechnung extends EinBucher {

    public SchuldenInDieAbrechnung(
            SachKontoProvider sachKontoProvider,
            BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository,
            AbrechnungRepository abrechnungRepository,
            int buchungstypSchuldenStart, int buchungstypSchulden,
            SachKonto kontonrSchulden, SachKonto kontonrZinsen, String text,
            double zinssatz) {
        super(sachKontoProvider,buchungRepository, kontoBewegungRepository);
        this.buchungstypSchuldenStart = buchungstypSchuldenStart;
        this.kontonrZinsen = kontonrZinsen;
        this.text = text;
        this.buchungstypSchulden = buchungstypSchulden;
        this.kontonrSchulden = kontonrSchulden;
        this.abrechnungRepository = abrechnungRepository;
        this.zinssatz = zinssatz;
    }

    private int buchungstypSchuldenStart;
    private SachKonto kontonrZinsen;
    private String text;

    private int buchungstypSchulden;
    private SachKonto kontonrSchulden;
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
        BuchungsAuftrag<SachKonto> auftrag = erzeugeBuchungsAuftrag(betrag,
                zinsDauer);
        erzeugeDifferenzBuchung(auftrag, abrechnung);
    }

    private BuchungsAuftrag<SachKonto> erzeugeBuchungsAuftrag(
            MonetaryAmount betrag, int zinsDauer) {
        BetragsBündelMap<SachKonto> beträge = new BetragsBündelMap<>();
        beträge.put(kontonrSchulden, betrag);
        MonetaryAmount zins = berechneZins(betrag, zinsDauer);
        beträge.put(kontonrZinsen, zins);
        Beschreibung beschreibung = new Beschreibung(buchungstypSchulden, text);
        return new BuchungsAuftrag<SachKonto>(beschreibung, beträge);
    }

    private MonetaryAmount berechneZins(MonetaryAmount betrag, int zinsDauer) {
        return Geld.round(betrag.multiply(zinsDauer).multiply(zinssatz)
                .divide(360));
    }

}
