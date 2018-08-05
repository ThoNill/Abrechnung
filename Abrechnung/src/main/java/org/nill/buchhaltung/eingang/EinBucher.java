package org.nill.buchhaltung.eingang;

import java.util.HashMap;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.abrechnung.aufz�hlungen.SachKontoProvider;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.BezugZurBuchung;
import org.nill.abrechnung.entities.Buchung;
import org.nill.abrechnung.entities.KontoBewegung;

import boundingContext.gemeinsam.BetragsB�ndel;
import boundingContext.gemeinsam.BetragsB�ndelMap;

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

            BetragsB�ndel<SachKonto> betr�ge = auftrag.getPositionen();
            for (SachKonto p : betr�ge.getKeys()) {
                MonetaryAmount betrag = betr�ge.getValue(p);
                bewegungHinzuf�gen(buchung, p, betrag);
            }
            HashMap<Integer, Long> bez�ge = auftrag.getVerbundenMit();
            for (Integer rolle : bez�ge.keySet()) {
                bezugHinzuf�gen(buchung, rolle, bez�ge.get(rolle));
            }
            return getBuchungRepository().save(buchung);
        }
        return null;
    }

    private void bewegungHinzuf�gen(Buchung buchung, SachKonto p,
            MonetaryAmount betrag) {
        if (!betrag.isZero()) {
            KontoBewegung bew = new KontoBewegung();
            bew.setBetrag(betrag);
            bew.setArt(1);
            bew.setKontoNr(p.ordinal());
            bew.setBuchung(buchung);
            buchung.addBewegungen(bew);
        }
    }

    private void bezugHinzuf�gen(Buchung buchung, int rolle, Long refernzid) {
        BezugZurBuchung bezug = new BezugZurBuchung();
        bezug.setBuchung(buchung);
        buchung.addBezug(bezug);
    }

    public BetragsB�ndel<SachKonto> betr�geEinerBuchungsartHolen(
            Abrechnung abrechnung, int art) {
        BetragsB�ndelMap<SachKonto> betr�ge = new BetragsB�ndelMap<>();
        for (Object o : getBuchungRepository()
                .getSumBewegungen(abrechnung, art)) {
            Object[] werte = (Object[]) o;
            SachKonto p = sachKontoFrom((int) werte[0]);
            betr�ge.put(p, (MonetaryAmount) werte[1]);
        }
        return betr�ge;
    }

    public Buchung erzeugeDifferenzBuchung(BuchungsAuftrag<SachKonto> auftrag,
            Abrechnung abrechnung) {
        abrechnung = getAbrechnungRepository().save(abrechnung);
        BetragsB�ndel<SachKonto> aktuell = betr�geEinerBuchungsartHolen(
                abrechnung, auftrag.getBeschreibung().getArt());
        BetragsB�ndel<SachKonto> differenz = (BetragsB�ndel<SachKonto>) aktuell
                .subtract(auftrag.getPositionen(), aktuell);
        BuchungsAuftrag<SachKonto> differenzAuftrag = new BuchungsAuftrag<>(
                auftrag.getBeschreibung(), differenz);
        return erzeugeBuchung(differenzAuftrag, abrechnung);
    }

}