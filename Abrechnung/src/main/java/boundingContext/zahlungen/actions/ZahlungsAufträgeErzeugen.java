package boundingContext.zahlungen.actions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.money.MonetaryAmount;

import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.Buchung;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.entities.ZahlungsAuftrag;
import boundingContext.abrechnung.entities.ZahlungsDefinition;
import boundingContext.abrechnung.entities.Überweisung;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.abrechnung.repositories.ÜberweisungRepository;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.buchhaltung.eingang.EinBucher;
import boundingContext.gemeinsam.BetragsBündel;
import boundingContext.gemeinsam.BetragsBündelMap;
import boundingContext.gemeinsam.ProzentBündelMap;
import boundingContext.zahlungen.values.BankVerbindung;

public class ZahlungsAufträgeErzeugen extends EinBucher {
    private ZahlungsAuftragRepository zahlungsAuftragRepository;
    private ÜberweisungRepository überweisungsRepository;
  
    public ZahlungsAufträgeErzeugen(SachKontoProvider sachKontoProvider,
            BuchungRepository buchungRepository,
            ZahlungsAuftragRepository zahlungsAuftragRepository,
            ÜberweisungRepository überweisungsRepository,
            AbrechnungRepository abrechnungRepository) {
        super(sachKontoProvider);
        this.zahlungsAuftragRepository = zahlungsAuftragRepository;
        this.überweisungsRepository = überweisungsRepository;
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
            zahlungsAuftrag.setZuZahlenAm(d.berechneAuszahlungsTernin(new Date()));
            zahlungsAuftrag.setAbrechnung(abrechnung);
            zahlungsAuftrag.setMandant(mandant);
            zahlungsAuftrag = zahlungsAuftragRepository.save(zahlungsAuftrag);
            aufträge.add(zahlungsAuftrag);
            erzeugeBuchung(GUTHABEN(),AUSZUZAHLEN(),"Zahlungsauftrag erzeugt", abrechnung,zahlungsAuftrag);
        }
        return aufträge;
    }

    public void erzeugeÜberweisungen(
            List<ZahlungsAuftrag> zahlungsAufträge, BankVerbindung vonBank) {
        for (ZahlungsAuftrag auftrag : zahlungsAufträge) {
            auftrag = zahlungsAuftragRepository.save(auftrag);
            Überweisung überweisung = new Überweisung();
            überweisung.setBetrag(auftrag.getBetrag());
            überweisung.setAn(auftrag.getBank());
            überweisung.setVon(vonBank);
            überweisung.setBuchungsart(auftrag.getBuchungsart());
            überweisung.setErstellt(new Date());
            überweisung.setAuftrag(auftrag);
            überweisung.setMandant(auftrag.getMandant());
            überweisung = überweisungsRepository.save(überweisung);
            erzeugeBuchung(AUSZUZAHLEN(),AUSBEZAHLT(),"Überweisung erzeugt", auftrag.getAbrechnung(),überweisung);
        }
    }

    private BuchungsAuftrag<SachKonto> erzeugeBuchungsAuftrag(
            SachKonto von,
            SachKonto nach, String buchungstext,Überweisung überweisung) {
        MonetaryAmount betrag = überweisung.getBetrag();
        int buchungsart = überweisung.getBuchungsart();
        BetragsBündelMap<SachKonto> beträge = new BetragsBündelMap<>();
        beträge.put(von, betrag.negate());
        beträge.put(nach, betrag);
        Beschreibung beschreibung = new Beschreibung(buchungsart, buchungstext);
        BuchungsAuftrag<SachKonto> auftrag = new BuchungsAuftrag<SachKonto>(beschreibung, beträge);
        auftrag.verbinde(2,überweisung.getUeberweisungsId());
        return auftrag;
    }

    private Buchung erzeugeBuchung(SachKonto von, SachKonto nach, String buchungstext,
            Abrechnung abrechnung,Überweisung überweisung) {

        return erzeugeBuchung(
                erzeugeBuchungsAuftrag(von, nach,
                        buchungstext,überweisung), abrechnung);
    }


    private BuchungsAuftrag<SachKonto> erzeugeBuchungsAuftrag(
            SachKonto von,
            SachKonto nach, String buchungstext,ZahlungsAuftrag zahlungsAuftrag) {
        MonetaryAmount betrag = zahlungsAuftrag.getBetrag();
        int buchungsart = zahlungsAuftrag.getBuchungsart();
        BetragsBündelMap<SachKonto> beträge = new BetragsBündelMap<>();
        beträge.put(von, betrag.negate());
        beträge.put(nach, betrag);
        Beschreibung beschreibung = new Beschreibung(buchungsart, buchungstext);
        BuchungsAuftrag<SachKonto> auftrag = new BuchungsAuftrag<SachKonto>(beschreibung, beträge);
        auftrag.verbinde(1,zahlungsAuftrag.getZahlungsAuftragsId());
        return auftrag;
    }

    private Buchung erzeugeBuchung(SachKonto von, SachKonto nach, String buchungstext,
            Abrechnung abrechnung,ZahlungsAuftrag auftrag) {

        return erzeugeBuchung(
                erzeugeBuchungsAuftrag(von, nach,
                        buchungstext,auftrag), abrechnung);
    }

}
