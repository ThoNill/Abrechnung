package org.nill.zahlungen.actions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.abrechnung.aufz�hlungen.SachKontoProvider;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.Buchung;
import org.nill.abrechnung.entities.Mandant;
import org.nill.abrechnung.entities.ZahlungsAuftrag;
import org.nill.abrechnung.entities.�berweisung;
import org.nill.abrechnung.values.ZahlungsDefinition;
import org.nill.basiskomponenten.gemeinsam.BetragsB�ndel;
import org.nill.basiskomponenten.gemeinsam.BetragsB�ndelMap;
import org.nill.basiskomponenten.gemeinsam.ProzentB�ndelMap;
import org.nill.buchhaltung.eingang.Beschreibung;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;
import org.nill.buchhaltung.eingang.EinBucher;
import org.nill.zahlungen.values.BankVerbindung;

public class ZahlungsAuftr�geErzeugen extends EinBucher {

    public ZahlungsAuftr�geErzeugen(SachKontoProvider sachKontoProvider) {
        super(sachKontoProvider);
    }

    public List<ZahlungsAuftrag> erzeugeAuftr�ge(Abrechnung abrechnung,
            MonetaryAmount betrag, String verwendungszweck) {
        abrechnung = getAbrechnungRepository().save(abrechnung);
        Mandant mandant = abrechnung.getMandant();
        Set<ZahlungsDefinition> definitionen = mandant
                .getZahlungsDefinitionen();
        ProzentB�ndelMap<ZahlungsDefinition> prozentMap = new ProzentB�ndelMap<>();
        for (ZahlungsDefinition d : definitionen) {
            prozentMap.put(d, d.getProzentSatz());
        }
        BetragsB�ndel<ZahlungsDefinition> betr�ge = prozentMap
                .verteilen(betrag);
        List<ZahlungsAuftrag> auftr�ge = new ArrayList<>();
        for (ZahlungsDefinition d : definitionen) {
            ZahlungsAuftrag zahlungsAuftrag = new ZahlungsAuftrag();
            zahlungsAuftrag.setBetrag(betr�ge.getBetrag(d));
            zahlungsAuftrag.setBank(d.getBank());
            zahlungsAuftrag.setBuchungsart(d.getBuchungsart());
            zahlungsAuftrag.setVerwendungszweck(verwendungszweck);
            zahlungsAuftrag.setZuZahlenAm(d
                    .berechneAuszahlungsTernin(new Date()));
            zahlungsAuftrag.setAbrechnung(abrechnung);
            zahlungsAuftrag.setMandant(mandant);
            zahlungsAuftrag = getZahlungsAuftragRepository().save(
                    zahlungsAuftrag);
            auftr�ge.add(zahlungsAuftrag);
            erzeugeBuchung(GUTHABEN(), AUSZUZAHLEN(),
                    "Zahlungsauftrag erzeugt", abrechnung, zahlungsAuftrag);
        }
        return auftr�ge;
    }

    public void erzeuge�berweisungen(List<ZahlungsAuftrag> zahlungsAuftr�ge,
            BankVerbindung vonBank) {
        for (ZahlungsAuftrag auftrag : zahlungsAuftr�ge) {
            auftrag = getZahlungsAuftragRepository().save(auftrag);
            �berweisung �berweisung = new �berweisung();
            �berweisung.setBetrag(auftrag.getBetrag());
            �berweisung.setAn(auftrag.getBank());
            �berweisung.setVon(vonBank);
            �berweisung.setBuchungsart(auftrag.getBuchungsart());
            �berweisung.setErstellt(new Date());
            �berweisung.setAuftrag(auftrag);
            �berweisung.setMandant(auftrag.getMandant());
            �berweisung = get�berweisungRepository().save(�berweisung);
            erzeugeBuchung(AUSZUZAHLEN(), AUSBEZAHLT(), "�berweisung erzeugt",
                    auftrag.getAbrechnung(), �berweisung);
        }
    }

    private BuchungsAuftrag<SachKonto> erzeugeBuchungsAuftrag(SachKonto von,
            SachKonto nach, String buchungstext, �berweisung �berweisung) {
        MonetaryAmount betrag = �berweisung.getBetrag();
        int buchungsart = �berweisung.getBuchungsart();
        BetragsB�ndelMap<SachKonto> betr�ge = new BetragsB�ndelMap<>();
        betr�ge.put(von, betrag.negate());
        betr�ge.put(nach, betrag);
        Beschreibung beschreibung = new Beschreibung(buchungsart, buchungstext);
        BuchungsAuftrag<SachKonto> auftrag = new BuchungsAuftrag<SachKonto>(
                beschreibung, betr�ge);
        auftrag.verbinde(2, �berweisung.getUeberweisungsId());
        return auftrag;
    }

    private Buchung erzeugeBuchung(SachKonto von, SachKonto nach,
            String buchungstext, Abrechnung abrechnung, �berweisung �berweisung) {

        return erzeugeBuchung(
                erzeugeBuchungsAuftrag(von, nach, buchungstext, �berweisung),
                abrechnung);
    }

    private BuchungsAuftrag<SachKonto> erzeugeBuchungsAuftrag(SachKonto von,
            SachKonto nach, String buchungstext, ZahlungsAuftrag zahlungsAuftrag) {
        MonetaryAmount betrag = zahlungsAuftrag.getBetrag();
        int buchungsart = zahlungsAuftrag.getBuchungsart();
        BetragsB�ndelMap<SachKonto> betr�ge = new BetragsB�ndelMap<>();
        betr�ge.put(von, betrag.negate());
        betr�ge.put(nach, betrag);
        Beschreibung beschreibung = new Beschreibung(buchungsart, buchungstext);
        BuchungsAuftrag<SachKonto> auftrag = new BuchungsAuftrag<SachKonto>(
                beschreibung, betr�ge);
        auftrag.verbinde(1, zahlungsAuftrag.getZahlungsAuftragsId());
        return auftrag;
    }

    private Buchung erzeugeBuchung(SachKonto von, SachKonto nach,
            String buchungstext, Abrechnung abrechnung, ZahlungsAuftrag auftrag) {

        return erzeugeBuchung(
                erzeugeBuchungsAuftrag(von, nach, buchungstext, auftrag),
                abrechnung);
    }

}
