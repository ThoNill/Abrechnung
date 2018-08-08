package org.nill.buchhaltung.eingang;

import java.util.HashMap;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.abrechnung.aufzählungen.SachKontoProvider;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.Buchung;
import org.nill.abrechnung.values.KontoBewegung;
import org.nill.allgemein.values.TypeReference;
import org.nill.basiskomponenten.gemeinsam.BetragsBündel;
import org.nill.basiskomponenten.gemeinsam.BetragsBündelMap;

public class EinBucher extends SachKontoDelegate {

    public EinBucher(SachKontoProvider sachKontoProvider) {
        super(sachKontoProvider);
    }

    public Buchung erzeugeBuchung(BuchungsAuftrag<SachKonto> auftrag,
            Abrechnung abrechnung) {
        abrechnung = getAbrechnungRepository().save(abrechnung);
        if (!auftrag.isEmpty()) {
            Buchung buchung = new Buchung();
            buchung.setText(auftrag.getBeschreibung().getText());
            buchung.setArt(auftrag.getBeschreibung().getArt());
            buchung.setAbrechnung(abrechnung);

            BetragsBündel<SachKonto> beträge = auftrag.getPositionen();
            for (SachKonto p : beträge.getKeys()) {
                MonetaryAmount betrag = beträge.getValue(p);
                bewegungHinzufügen(buchung, p, betrag);
            }
            HashMap<Integer, Long> bezüge = auftrag.getVerbundenMit();
            for (Integer rolle : bezüge.keySet()) {
                bezugHinzufügen(buchung, rolle, bezüge.get(rolle));
            }
            return getBuchungRepository().save(buchung);
        }
        return null;
    }

    private void bewegungHinzufügen(Buchung buchung, SachKonto p,
            MonetaryAmount betrag) {
        if (!betrag.isZero()) {
            KontoBewegung bew = new KontoBewegung();
            bew.setBetrag(betrag);
            bew.setArt(1);
            bew.setKontoNr(p.ordinal());
            buchung.addBewegungen(bew);
        }
    }

    private void bezugHinzufügen(Buchung buchung, int rolle, Long refernzid) {
        buchung.addBezug(new TypeReference(rolle,refernzid));
    }

    public BetragsBündel<SachKonto> beträgeEinerBuchungsartHolen(
            Abrechnung abrechnung, int art) {
        BetragsBündelMap<SachKonto> beträge = new BetragsBündelMap<>();
        for (Object o : getBuchungRepository()
                .getSumBewegungen(abrechnung, art)) {
            Object[] werte = (Object[]) o;
            SachKonto p = sachKontoFrom((int) werte[0]);
            beträge.put(p, (MonetaryAmount) werte[1]);
        }
        return beträge;
    }

    public Buchung erzeugeDifferenzBuchung(BuchungsAuftrag<SachKonto> auftrag,
            Abrechnung abrechnung) {
        abrechnung = getAbrechnungRepository().save(abrechnung);
        BetragsBündel<SachKonto> aktuell = beträgeEinerBuchungsartHolen(
                abrechnung, auftrag.getBeschreibung().getArt());
        BetragsBündel<SachKonto> differenz = (BetragsBündel<SachKonto>) aktuell
                .subtract(auftrag.getPositionen(), aktuell);
        BuchungsAuftrag<SachKonto> differenzAuftrag = new BuchungsAuftrag<>(
                auftrag.getBeschreibung(), differenz);
        return erzeugeBuchung(differenzAuftrag, abrechnung);
    }

}