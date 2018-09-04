package org.nill.zahlungen.actions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.actions.EinBucher;
import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IBuchung;
import org.nill.abrechnung.interfaces.IMandant;
import org.nill.abrechnung.interfaces.IZahlungsAuftrag;
import org.nill.abrechnung.interfaces.I�berweisung;
import org.nill.abrechnung.interfaces.Umgebung;
import org.nill.abrechnung.values.ZahlungsDefinition;
import org.nill.basiskomponenten.gemeinsam.BetragsB�ndel;
import org.nill.basiskomponenten.gemeinsam.BetragsB�ndelMap;
import org.nill.basiskomponenten.gemeinsam.ProzentB�ndelMap;
import org.nill.buchhaltung.eingang.Beschreibung;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;
import org.nill.zahlungen.values.BankVerbindung;

/**
 * Erzeugt Zahlungsauftr�ge aufgrund der {@link ZahlungsDefinition} eines {@link IMandant} 
 * 
 * @author javaman
 *
 */
public class ZahlungsAuftr�geErzeugen extends EinBucher {

    public ZahlungsAuftr�geErzeugen(Umgebung umgebung) {
        super(umgebung);
    }

    public List<IZahlungsAuftrag> erzeugeAuftr�ge(IAbrechnung abrechnung,
            MonetaryAmount betrag, String verwendungszweck) {
        IMandant mandant = abrechnung.getMandant();
        Set<ZahlungsDefinition> definitionen = mandant
                .getZahlungsDefinitionen();
        ProzentB�ndelMap<ZahlungsDefinition> prozentMap = new ProzentB�ndelMap<>();
        for (ZahlungsDefinition d : definitionen) {
            prozentMap.put(d, d.getProzentSatz());
        }
        BetragsB�ndel<ZahlungsDefinition> betr�ge = prozentMap
                .verteilen(betrag);
        List<IZahlungsAuftrag> auftr�ge = new ArrayList<>();
        for (ZahlungsDefinition d : definitionen) {
            IZahlungsAuftrag zahlungsAuftrag = createZahlungsAuftrag();
            zahlungsAuftrag.setBetrag(betr�ge.getBetrag(d));
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
            auftr�ge.add(zahlungsAuftrag);
            erzeugeBuchung(GUTHABEN(), AUSZUZAHLEN(),
                    "Zahlungsauftrag erzeugt", abrechnung, zahlungsAuftrag);
        }
        return auftr�ge;
    }

    public void erzeuge�berweisungen(List<IZahlungsAuftrag> zahlungsAuftr�ge,
            BankVerbindung vonBank) {
        for (IZahlungsAuftrag auftrag : zahlungsAuftr�ge) {
            auftrag = getZahlungsAuftragRepository().save(auftrag);
            I�berweisung �berweisung = create�berweisung();
            �berweisung.setBetrag(auftrag.getBetrag());
            �berweisung.setAn(auftrag.getBank());
            �berweisung.setVon(vonBank);
            �berweisung.setBuchungsart(auftrag.getBuchungsart());
            �berweisung.setErstellt(new Date());
            �berweisung.setIZahlungsAuftrag(auftrag);
            �berweisung.setIMandat(auftrag.getMandant());
            �berweisung = get�berweisungRepository().save(�berweisung);
            IAbrechnung abrechnung = auftrag.getAbrechnung();
            erzeugeBuchung(AUSZUZAHLEN(), AUSBEZAHLT(), "�berweisung erzeugt",
                    abrechnung, �berweisung);
        }
    }

    private BuchungsAuftrag<SachKonto> erzeugeBuchungsAuftrag(SachKonto von,
            SachKonto nach, String buchungstext, I�berweisung �berweisung) {
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

    private IBuchung erzeugeBuchung(SachKonto von, SachKonto nach,
            String buchungstext, IAbrechnung abrechnung, I�berweisung �berweisung) {

        return erzeugeBuchung(
                erzeugeBuchungsAuftrag(von, nach, buchungstext, �berweisung),
                abrechnung);
    }

    private BuchungsAuftrag<SachKonto> erzeugeBuchungsAuftrag(SachKonto von,
            SachKonto nach, String buchungstext, IZahlungsAuftrag zahlungsAuftrag) {
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

    private IBuchung erzeugeBuchung(SachKonto von, SachKonto nach,
            String buchungstext, IAbrechnung abrechnung, IZahlungsAuftrag auftrag) {

        return erzeugeBuchung(
                erzeugeBuchungsAuftrag(von, nach, buchungstext, auftrag),
                abrechnung);
    }

}
