package boundingContext.abrechnung.actions;

import java.util.Optional;

import javax.money.MonetaryAmount;

import betrag.Geld;
import boundingContext.abrechnung.aufz�hlungen.SachKonto;
import boundingContext.abrechnung.aufz�hlungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.buchhaltung.eingang.EinBucher;
import boundingContext.gemeinsam.BetragsB�ndelMap;

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

    public void �bertragen(Abrechnung abrechnung, int zinsDauer) {
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
        BetragsB�ndelMap<SachKonto> betr�ge = new BetragsB�ndelMap<>();
        betr�ge.put(SCHULDEN(), betrag);
        MonetaryAmount zins = berechneZins(betrag, zinsDauer);
        betr�ge.put(ZINS(), zins);
        MonetaryAmount mwst = berechneMwst(zins);
        betr�ge.put(MWST(), mwst);
     
        Beschreibung beschreibung = new Beschreibung(�BERNAHME_SCHULDEN(), text);
        return new BuchungsAuftrag<SachKonto>(beschreibung, betr�ge);
    }

    private MonetaryAmount berechneZins(MonetaryAmount betrag, int zinsDauer) {
        return Geld.round(betrag.multiply(zinsDauer).multiply(zinssatz)
                .divide(360));
    }
    
    private MonetaryAmount berechneMwst(MonetaryAmount betrag) {
        return Geld.round(betrag.multiply(mwstsatz));
    }

}
