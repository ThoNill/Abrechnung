package org.nill.abrechnung.actions;

import java.util.Optional;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.Umgebung;
import org.nill.basiskomponenten.betrag.Geld;
import org.nill.basiskomponenten.gemeinsam.BetragsBündelMap;
import org.nill.buchhaltung.eingang.Beschreibung;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;

public class SchuldenInDieAbrechnung extends EinBucher {
    private String text;
    private double zinssatz;
    private double mwstsatz;

    public SchuldenInDieAbrechnung(Umgebung umgebung,
            String text, double zinssatz, double mwstsatz) {
        super(umgebung);
        this.text = text;
        this.zinssatz = zinssatz;
        this.mwstsatz = mwstsatz;
    }

    public void übertragen(IAbrechnung abrechnung, int zinsDauer) {
        Optional<IAbrechnung> oAbrechnung = abrechnung
                .getVorherigeAbrechnung(this);
        if (oAbrechnung.isPresent()) {
            MonetaryAmount saldo = getBuchungRepository().getSumKonto(
                    oAbrechnung.get(), ABGLEICH_SCHULDEN(),
                    SCHULDEN().ordinal());
            if (saldo != null) {
                buche(abrechnung, saldo.negate(), zinsDauer);
            }
        }
    }

    private void buche(IAbrechnung abrechnung, MonetaryAmount betrag,
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
        return new BuchungsAuftrag<>(beschreibung, beträge);
    }

    private MonetaryAmount berechneZins(MonetaryAmount betrag, int zinsDauer) {
        return Geld.round(betrag.multiply(zinsDauer).multiply(zinssatz)
                .divide(360));
    }

    private MonetaryAmount berechneMwst(MonetaryAmount betrag) {
        return Geld.round(betrag.multiply(mwstsatz));
    }

}
