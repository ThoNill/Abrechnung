package boundingContext.zahlungen.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.money.MonetaryAmount;

import org.springframework.transaction.annotation.Transactional;

import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.Buchung;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.entities.ZahlungsAuftrag;
import boundingContext.abrechnung.entities.ZahlungsDefinition;
import boundingContext.abrechnung.entities.Überweisung;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.abrechnung.repositories.ÜberweisungRepository;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.buchhaltung.eingang.EinBucher;
import boundingContext.gemeinsam.BetragsBündel;
import boundingContext.gemeinsam.BetragsBündelMap;
import boundingContext.gemeinsam.ProzentBündelMap;
import boundingContext.zahlungen.values.BankVerbindung;

public class ZahlungsAuftragsManager extends EinBucher {
    private ZahlungsAuftragRepository zahlungsAuftragRepository;
    private ÜberweisungRepository überweisungsRepository;
    private SachKonto offen;
    private SachKonto überwiesen;

    public ZahlungsAuftragsManager(SachKontoProvider sachKontoProvider,
            BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository,
            ZahlungsAuftragRepository zahlungsAuftragRepository,
            ÜberweisungRepository überweisungsRepository, SachKonto offen,
            SachKonto überwiesen) {
        super(sachKontoProvider, buchungRepository, kontoBewegungRepository);
        this.zahlungsAuftragRepository = zahlungsAuftragRepository;
        this.überweisungsRepository = überweisungsRepository;
        this.offen = offen;
        this.überwiesen = überwiesen;
    }

    @Transactional("dbATransactionManager")
    public List<ZahlungsAuftrag> erzeugeAufträge(Abrechnung abrechnung,
            MonetaryAmount betrag, String verwendungszweck) {
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
            ZahlungsAuftrag a = new ZahlungsAuftrag();
            a.setBetrag(beträge.getBetrag(d));
            a.setBank(d.getBank());
            a.setBuchungsart(d.getBuchungsart());
            a.setVerwendungszweck(verwendungszweck);
            a.setZuZahlenAm(d.berechneAuszahlungsTernin(new Date()));
            a = zahlungsAuftragRepository.save(a);
            a.setAbrechnung(abrechnung);
            a.setMandant(mandant);
            a = zahlungsAuftragRepository.save(a);
            aufträge.add(a);
        }
        return aufträge;
    }

    @Transactional("dbATransactionManager")
    public void erzeugeÜberweisungen(Mandant mandant,
            List<ZahlungsAuftrag> zahlungsAufträge, BankVerbindung vonBank) {
        for (ZahlungsAuftrag auftrag : zahlungsAufträge) {
            Überweisung überweisung = new Überweisung();
            überweisung.setBetrag(auftrag.getBetrag());
            überweisung.setAn(auftrag.getBank());
            überweisung.setVon(vonBank);
            überweisung.setBuchungsart(auftrag.getBuchungsart());
            überweisung.setErstellt(new Date());
            überweisung = überweisungsRepository.save(überweisung);
            überweisung.setAuftrag(auftrag);
            überweisung.setMandant(auftrag.getMandant());
            überweisung = überweisungsRepository.save(überweisung);
            erzeugeBuchung(überweisung.getBetrag(),
                    überweisung.getBuchungsart(), offen, überwiesen,
                    "Überweisung erzeugt", auftrag.getAbrechnung());
        }
    }

    private BuchungsAuftrag<SachKonto> erzeugeBuchungsAuftrag(
            MonetaryAmount betrag, int buchungsart, SachKonto von,
            SachKonto nach, String buchungstext) {
        BetragsBündelMap<SachKonto> beträge = new BetragsBündelMap<>();
        beträge.put(von, betrag.negate());
        beträge.put(nach, betrag);
        Beschreibung beschreibung = new Beschreibung(buchungsart, buchungstext);
        return new BuchungsAuftrag<SachKonto>(beschreibung, beträge);
    }

    private Buchung erzeugeBuchung(MonetaryAmount betrag, int buchungsart,
            SachKonto von, SachKonto nach, String buchungstext,
            Abrechnung abrechnung) {

        return erzeugeBuchung(
                erzeugeBuchungsAuftrag(betrag, buchungsart, von, nach,
                        buchungstext), abrechnung);
    }

}
