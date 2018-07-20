package boundingContext.zahlungen.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.money.MonetaryAmount;

import org.springframework.transaction.annotation.Transactional;

import boundingContext.abrechnung.aufz�hlungen.SachKonto;
import boundingContext.abrechnung.aufz�hlungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.Buchung;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.entities.ZahlungsAuftrag;
import boundingContext.abrechnung.entities.ZahlungsDefinition;
import boundingContext.abrechnung.entities.�berweisung;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.abrechnung.repositories.�berweisungRepository;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.buchhaltung.eingang.EinBucher;
import boundingContext.gemeinsam.BetragsB�ndel;
import boundingContext.gemeinsam.BetragsB�ndelMap;
import boundingContext.gemeinsam.ProzentB�ndelMap;
import boundingContext.zahlungen.values.BankVerbindung;

public class ZahlungsAuftragsManager extends EinBucher {
    private ZahlungsAuftragRepository zahlungsAuftragRepository;
    private �berweisungRepository �berweisungsRepository;
    private SachKonto offen;
    private SachKonto �berwiesen;

    public ZahlungsAuftragsManager(SachKontoProvider sachKontoProvider,
            BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository,
            ZahlungsAuftragRepository zahlungsAuftragRepository,
            �berweisungRepository �berweisungsRepository,
            AbrechnungRepository abrechnungRepository,SachKonto offen,
            SachKonto �berwiesen) {
        super(sachKontoProvider, buchungRepository, kontoBewegungRepository,abrechnungRepository);
        this.zahlungsAuftragRepository = zahlungsAuftragRepository;
        this.�berweisungsRepository = �berweisungsRepository;
        this.offen = offen;
        this.�berwiesen = �berwiesen;
    }

    public List<ZahlungsAuftrag> erzeugeAuftr�ge(Abrechnung abrechnung,
            MonetaryAmount betrag, String verwendungszweck) {
        abrechnung = abrechnungRepository.save(abrechnung);
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
            ZahlungsAuftrag a = new ZahlungsAuftrag();
            a.setBetrag(betr�ge.getBetrag(d));
            a.setBank(d.getBank());
            a.setBuchungsart(d.getBuchungsart());
            a.setVerwendungszweck(verwendungszweck);
            a.setZuZahlenAm(d.berechneAuszahlungsTernin(new Date()));
            a.setAbrechnung(abrechnung);
            a.setMandant(mandant);
            a = zahlungsAuftragRepository.save(a);
            auftr�ge.add(a);
        }
        return auftr�ge;
    }

    public void erzeuge�berweisungen(
            List<ZahlungsAuftrag> zahlungsAuftr�ge, BankVerbindung vonBank) {
        for (ZahlungsAuftrag auftrag : zahlungsAuftr�ge) {
            auftrag = zahlungsAuftragRepository.save(auftrag);
            �berweisung �berweisung = new �berweisung();
            �berweisung.setBetrag(auftrag.getBetrag());
            �berweisung.setAn(auftrag.getBank());
            �berweisung.setVon(vonBank);
            �berweisung.setBuchungsart(auftrag.getBuchungsart());
            �berweisung.setErstellt(new Date());
            �berweisung.setAuftrag(auftrag);
            �berweisung.setMandant(auftrag.getMandant());
            �berweisung = �berweisungsRepository.save(�berweisung);
            erzeugeBuchung(offen, �berwiesen,"�berweisung erzeugt", auftrag.getAbrechnung(),�berweisung);
        }
    }

    private BuchungsAuftrag<SachKonto> erzeugeBuchungsAuftrag(
            SachKonto von,
            SachKonto nach, String buchungstext,�berweisung �berweisung) {
        MonetaryAmount betrag = �berweisung.getBetrag();
        int buchungsart = �berweisung.getBuchungsart();
        BetragsB�ndelMap<SachKonto> betr�ge = new BetragsB�ndelMap<>();
        betr�ge.put(von, betrag.negate());
        betr�ge.put(nach, betrag);
        Beschreibung beschreibung = new Beschreibung(buchungsart, buchungstext);
        BuchungsAuftrag<SachKonto> auftrag = new BuchungsAuftrag<SachKonto>(beschreibung, betr�ge);
        auftrag.verbinde(1,�berweisung.getUeberweisungsId());
        return auftrag;
    }

    private Buchung erzeugeBuchung(SachKonto von, SachKonto nach, String buchungstext,
            Abrechnung abrechnung,�berweisung �berweisung) {

        return erzeugeBuchung(
                erzeugeBuchungsAuftrag(von, nach,
                        buchungstext,�berweisung), abrechnung);
    }

}
