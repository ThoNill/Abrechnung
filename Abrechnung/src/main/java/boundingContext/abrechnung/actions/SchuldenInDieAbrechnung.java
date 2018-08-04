package boundingContext.abrechnung.actions;

import java.util.Optional;

import javax.money.MonetaryAmount;

import betrag.Geld;
import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.buchhaltung.eingang.EinBucher;
import boundingContext.gemeinsam.BetragsBündelMap;

public class SchuldenInDieAbrechnung extends EinBucher {

    public SchuldenInDieAbrechnung(SachKontoProvider sachKontoProvider,
             String text,
            double zinssatz,double mwstsatz) {
        super(sachKontoProvider);
        this.text = text;
        this.zinssatz = zinssatz;
        this.mwstsatz = mwstsatz;
    }

    private String text;
    private double zinssatz;
    private double mwstsatz;

    public void übertragen(Abrechnung abrechnung, int zinsDauer) {
        Optional<Abrechnung> oAbrechnung = abrechnung.getVorherigeAbrechnung(this);
        if (oAbrechnung.isPresent()) {
            MonetaryAmount saldo = getBuchungRepository().getSumKonto(
                    oAbrechnung.get(), ABGLEICH_SCHULDEN(),
                    SCHULDEN().ordinal());
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
        beträge.put(SCHULDEN(), betrag);
        MonetaryAmount zins = berechneZins(betrag, zinsDauer);
        beträge.put(ZINS(), zins);
        MonetaryAmount mwst = berechneMwst(zins);
        beträge.put(MWST(), mwst);
     
        Beschreibung beschreibung = new Beschreibung(ÜBERNAHME_SCHULDEN(), text);
        return new BuchungsAuftrag<SachKonto>(beschreibung, beträge);
    }

    private MonetaryAmount berechneZins(MonetaryAmount betrag, int zinsDauer) {
        return Geld.round(betrag.multiply(zinsDauer).multiply(zinssatz)
                .divide(360));
    }
    
    private MonetaryAmount berechneMwst(MonetaryAmount betrag) {
        return Geld.round(betrag.multiply(mwstsatz));
    }

}
