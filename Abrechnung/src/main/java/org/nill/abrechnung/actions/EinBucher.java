package org.nill.abrechnung.actions;

import java.util.HashMap;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IBuchung;
import org.nill.abrechnung.interfaces.SachKontoDelegate;
import org.nill.abrechnung.interfaces.SachKontoProvider;
import org.nill.abrechnung.values.KontoBewegung;
import org.nill.allgemein.values.TypeReference;
import org.nill.basiskomponenten.gemeinsam.BetragsB�ndel;
import org.nill.basiskomponenten.gemeinsam.BetragsB�ndelMap;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;

public class EinBucher extends SachKontoDelegate {

    public EinBucher(SachKontoProvider sachKontoProvider) {
        super(sachKontoProvider);
    }

    public IBuchung erzeugeBuchung(BuchungsAuftrag<SachKonto> auftrag,
            IAbrechnung abrechnung) {
        abrechnung = getAbrechnungRepository().saveIAbrechnung(abrechnung);
        if (!auftrag.isEmpty()) {
            IBuchung buchung = createBuchung();
            buchung.setText(auftrag.getBeschreibung().getText());
            buchung.setArt(auftrag.getBeschreibung().getArt());
            buchung.setIAbrechnung(abrechnung);

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

    private void bewegungHinzuf�gen(IBuchung buchung, SachKonto p,
            MonetaryAmount betrag) {
        if (!betrag.isZero()) {
            KontoBewegung bew = new KontoBewegung();
            bew.setBetrag(betrag);
            bew.setArt(1);
            bew.setKontoNr(p.ordinal());
            buchung.addBewegungen(bew);
        }
    }

    private void bezugHinzuf�gen(IBuchung buchung, int rolle, Long refernzid) {
        buchung.addBezug(new TypeReference(rolle,refernzid));
    }

    public BetragsB�ndel<SachKonto> betr�geEinerBuchungsartHolen(
            IAbrechnung abrechnung, int art) {
        BetragsB�ndelMap<SachKonto> betr�ge = new BetragsB�ndelMap<>();
        for (Object o : getBuchungRepository()
                .getSumBewegungen(abrechnung, art)) {
            Object[] werte = (Object[]) o;
            SachKonto p = sachKontoFrom((int) werte[0]);
            betr�ge.put(p, (MonetaryAmount) werte[1]);
        }
        return betr�ge;
    }

    public IBuchung erzeugeDifferenzBuchung(BuchungsAuftrag<SachKonto> auftrag,
            IAbrechnung abrechnung) {
        abrechnung = getAbrechnungRepository().saveIAbrechnung(abrechnung);
        BetragsB�ndel<SachKonto> aktuell = betr�geEinerBuchungsartHolen(
                abrechnung, auftrag.getBeschreibung().getArt());
        BetragsB�ndel<SachKonto> differenz = (BetragsB�ndel<SachKonto>) aktuell
                .subtract(auftrag.getPositionen(), aktuell);
        BuchungsAuftrag<SachKonto> differenzAuftrag = new BuchungsAuftrag<>(
                auftrag.getBeschreibung(), differenz);
        return erzeugeBuchung(differenzAuftrag, abrechnung);
    }

}