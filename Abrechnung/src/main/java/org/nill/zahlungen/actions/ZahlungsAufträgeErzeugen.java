package org.nill.zahlungen.actions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.actions.EinBucher;
import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IBuchung;
import org.nill.abrechnung.interfaces.IMandant;
import org.nill.abrechnung.interfaces.IZahlungsAuftrag;
import org.nill.abrechnung.interfaces.IÜberweisung;
import org.nill.abrechnung.interfaces.Umgebung;
import org.nill.abrechnung.values.ZahlungsDefinition;
import org.nill.basiskomponenten.gemeinsam.BetragsBündel;
import org.nill.basiskomponenten.gemeinsam.BetragsBündelMap;
import org.nill.basiskomponenten.gemeinsam.ProzentBündelMap;
import org.nill.buchhaltung.eingang.Beschreibung;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;
import org.nill.zahlungen.values.BankVerbindung;

/**
 * Erzeugt Zahlungsaufträge aufgrund der {@link ZahlungsDefinition} eines {@link IMandant} 
 * 
 * @author javaman
 *
 */
public class ZahlungsAufträgeErzeugen extends EinBucher {

    public ZahlungsAufträgeErzeugen(Umgebung umgebung) {
        super(umgebung);
    }

    public List<IZahlungsAuftrag> erzeugeAufträge(IAbrechnung abrechnung,
            MonetaryAmount betrag, String verwendungszweck) {
        IMandant mandant = abrechnung.getMandant();
        Set<ZahlungsDefinition> definitionen = mandant
                .getZahlungsDefinitionen();
        ProzentBündelMap<ZahlungsDefinition> prozentMap = new ProzentBündelMap<>();
        for (ZahlungsDefinition d : definitionen) {
            prozentMap.put(d, d.getProzentSatz());
        }
        BetragsBündel<ZahlungsDefinition> beträge = prozentMap
                .verteilen(betrag);
        List<IZahlungsAuftrag> aufträge = new ArrayList<>();
        for (ZahlungsDefinition d : definitionen) {
            IZahlungsAuftrag zahlungsAuftrag = createZahlungsAuftrag();
            zahlungsAuftrag.setBetrag(beträge.getBetrag(d));
            zahlungsAuftrag.setBank(d.getBank());
            zahlungsAuftrag.setBuchungsart(d.getBuchungsart());
            zahlungsAuftrag.setVerwendungszweck(verwendungszweck);
            zahlungsAuftrag.setZuZahlenAm(d
                    .berechneAuszahlungsTernin(new Date()));
            zahlungsAuftrag.setIAbrechnung(abrechnung);
            zahlungsAuftrag.setIMandant(mandant);
            zahlungsAuftrag = getZahlungsAuftragRepository().save(
                    zahlungsAuftrag);
            getZahlungsAuftragRepository().save(zahlungsAuftrag);
            aufträge.add(zahlungsAuftrag);
            erzeugeBuchung(GUTHABEN(), AUSZUZAHLEN(),
                    "Zahlungsauftrag erzeugt", abrechnung, zahlungsAuftrag);
        }
        return aufträge;
    }

    public void erzeugeÜberweisungen(List<IZahlungsAuftrag> zahlungsAufträge,
            BankVerbindung vonBank) {
        for (IZahlungsAuftrag auftrag : zahlungsAufträge) {
            auftrag = getZahlungsAuftragRepository().save(auftrag);
            IÜberweisung überweisung = createÜberweisung();
            überweisung.setBetrag(auftrag.getBetrag());
            überweisung.setAn(auftrag.getBank());
            überweisung.setVon(vonBank);
            überweisung.setBuchungsart(auftrag.getBuchungsart());
            überweisung.setErstellt(new Date());
            überweisung.setIZahlungsAuftrag(auftrag);
            überweisung.setIMandat(auftrag.getMandant());
            überweisung = getÜberweisungRepository().save(überweisung);
            IAbrechnung abrechnung = auftrag.getAbrechnung();
            erzeugeBuchung(AUSZUZAHLEN(), AUSBEZAHLT(), "Überweisung erzeugt",
                    abrechnung, überweisung);
        }
    }

    private BuchungsAuftrag<SachKonto> erzeugeBuchungsAuftrag(SachKonto von,
            SachKonto nach, String buchungstext, IÜberweisung überweisung) {
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

    private IBuchung erzeugeBuchung(SachKonto von, SachKonto nach,
            String buchungstext, IAbrechnung abrechnung, IÜberweisung überweisung) {

        return erzeugeBuchung(
                erzeugeBuchungsAuftrag(von, nach, buchungstext, überweisung),
                abrechnung);
    }

    private BuchungsAuftrag<SachKonto> erzeugeBuchungsAuftrag(SachKonto von,
            SachKonto nach, String buchungstext, IZahlungsAuftrag zahlungsAuftrag) {
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

    private IBuchung erzeugeBuchung(SachKonto von, SachKonto nach,
            String buchungstext, IAbrechnung abrechnung, IZahlungsAuftrag auftrag) {

        return erzeugeBuchung(
                erzeugeBuchungsAuftrag(von, nach, buchungstext, auftrag),
                abrechnung);
    }

}
