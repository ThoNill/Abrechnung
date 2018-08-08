package org.nill.zahlungen.actions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.abrechnung.aufzählungen.SachKontoProvider;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.Buchung;
import org.nill.abrechnung.entities.Mandant;
import org.nill.abrechnung.entities.ZahlungsAuftrag;
import org.nill.abrechnung.entities.Überweisung;
import org.nill.abrechnung.values.ZahlungsDefinition;
import org.nill.basiskomponenten.gemeinsam.BetragsBündel;
import org.nill.basiskomponenten.gemeinsam.BetragsBündelMap;
import org.nill.basiskomponenten.gemeinsam.ProzentBündelMap;
import org.nill.buchhaltung.eingang.Beschreibung;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;
import org.nill.buchhaltung.eingang.EinBucher;
import org.nill.zahlungen.values.BankVerbindung;

public class ZahlungsAufträgeErzeugen extends EinBucher {

    public ZahlungsAufträgeErzeugen(SachKontoProvider sachKontoProvider) {
        super(sachKontoProvider);
    }

    public List<ZahlungsAuftrag> erzeugeAufträge(Abrechnung abrechnung,
            MonetaryAmount betrag, String verwendungszweck) {
        abrechnung = getAbrechnungRepository().save(abrechnung);
        Mandant mandant = abrechnung.getMandant();
        Set<ZahlungsDefinition> definitionen = mandant
                .getZahlungsDefinitionen();
        ProzentBündelMap<ZahlungsDefinition> prozentMap = new ProzentBündelMap<>();
        for (ZahlungsDefinition d : definitionen) {
            prozentMap.put(d, d.getProzentSatz());
        }
        BetragsBündel<ZahlungsDefinition> beträge = prozentMap
                .verteilen(betrag);
        List<ZahlungsAuftrag> aufträge = new ArrayList<>();
        for (ZahlungsDefinition d : definitionen) {
            ZahlungsAuftrag zahlungsAuftrag = new ZahlungsAuftrag();
            zahlungsAuftrag.setBetrag(beträge.getBetrag(d));
            zahlungsAuftrag.setBank(d.getBank());
            zahlungsAuftrag.setBuchungsart(d.getBuchungsart());
            zahlungsAuftrag.setVerwendungszweck(verwendungszweck);
            zahlungsAuftrag.setZuZahlenAm(d
                    .berechneAuszahlungsTernin(new Date()));
            zahlungsAuftrag.setAbrechnung(abrechnung);
            zahlungsAuftrag.setMandant(mandant);
            zahlungsAuftrag = getZahlungsAuftragRepository().save(
                    zahlungsAuftrag);
            aufträge.add(zahlungsAuftrag);
            erzeugeBuchung(GUTHABEN(), AUSZUZAHLEN(),
                    "Zahlungsauftrag erzeugt", abrechnung, zahlungsAuftrag);
        }
        return aufträge;
    }

    public void erzeugeÜberweisungen(List<ZahlungsAuftrag> zahlungsAufträge,
            BankVerbindung vonBank) {
        for (ZahlungsAuftrag auftrag : zahlungsAufträge) {
            auftrag = getZahlungsAuftragRepository().save(auftrag);
            Überweisung überweisung = new Überweisung();
            überweisung.setBetrag(auftrag.getBetrag());
            überweisung.setAn(auftrag.getBank());
            überweisung.setVon(vonBank);
            überweisung.setBuchungsart(auftrag.getBuchungsart());
            überweisung.setErstellt(new Date());
            überweisung.setAuftrag(auftrag);
            überweisung.setMandant(auftrag.getMandant());
            überweisung = getÜberweisungRepository().save(überweisung);
            erzeugeBuchung(AUSZUZAHLEN(), AUSBEZAHLT(), "Überweisung erzeugt",
                    auftrag.getAbrechnung(), überweisung);
        }
    }

    private BuchungsAuftrag<SachKonto> erzeugeBuchungsAuftrag(SachKonto von,
            SachKonto nach, String buchungstext, Überweisung überweisung) {
        MonetaryAmount betrag = überweisung.getBetrag();
        int buchungsart = überweisung.getBuchungsart();
        BetragsBündelMap<SachKonto> beträge = new BetragsBündelMap<>();
        beträge.put(von, betrag.negate());
        beträge.put(nach, betrag);
        Beschreibung beschreibung = new Beschreibung(buchungsart, buchungstext);
        BuchungsAuftrag<SachKonto> auftrag = new BuchungsAuftrag<SachKonto>(
                beschreibung, beträge);
        auftrag.verbinde(2, überweisung.getUeberweisungsId());
        return auftrag;
    }

    private Buchung erzeugeBuchung(SachKonto von, SachKonto nach,
            String buchungstext, Abrechnung abrechnung, Überweisung überweisung) {

        return erzeugeBuchung(
                erzeugeBuchungsAuftrag(von, nach, buchungstext, überweisung),
                abrechnung);
    }

    private BuchungsAuftrag<SachKonto> erzeugeBuchungsAuftrag(SachKonto von,
            SachKonto nach, String buchungstext, ZahlungsAuftrag zahlungsAuftrag) {
        MonetaryAmount betrag = zahlungsAuftrag.getBetrag();
        int buchungsart = zahlungsAuftrag.getBuchungsart();
        BetragsBündelMap<SachKonto> beträge = new BetragsBündelMap<>();
        beträge.put(von, betrag.negate());
        beträge.put(nach, betrag);
        Beschreibung beschreibung = new Beschreibung(buchungsart, buchungstext);
        BuchungsAuftrag<SachKonto> auftrag = new BuchungsAuftrag<SachKonto>(
                beschreibung, beträge);
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
