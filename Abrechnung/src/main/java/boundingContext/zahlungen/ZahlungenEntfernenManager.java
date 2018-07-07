package boundingContext.zahlungen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import boundingContext.abrechnung.helper.EinBucher;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.abrechnung.repositories.ÜberweisungRepository;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.gemeinsam.BetragsBündel;
import boundingContext.gemeinsam.BetragsBündelMap;
import boundingContext.gemeinsam.ProzentBündelMap;

public class ZahlungenEntfernenManager extends EinBucher {
    private ZahlungsAuftragRepository zahlungsAuftragRepository;
    private int buchungstypGuthaben;
    private SachKonto kontonrGuthaben;

    public ZahlungenEntfernenManager(SachKontoProvider sachKontoProvider,
            BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository,
            ZahlungsAuftragRepository zahlungsAuftragRepository,
            int buchungstypGuthaben, SachKonto kontonrGuthaben) {
        super(sachKontoProvider, buchungRepository, kontoBewegungRepository);
        this.zahlungsAuftragRepository = zahlungsAuftragRepository;
        this.buchungstypGuthaben = buchungstypGuthaben;
        this.kontonrGuthaben = kontonrGuthaben;
    }

    @Transactional("dbATransactionManager")
    public void entferneZahlungsaufträgeFallsRestguthaben(Abrechnung abrechnung) {
        MonetaryAmount saldo = buchungRepository.getSaldo(abrechnung);
        if (!saldo.isZero()) {
            entferneZahlungsaufträge(abrechnung, buchungstypGuthaben);
        }
    }

    @Transactional("dbATransactionManager")
    public void entferneZahlungsaufträge(Abrechnung abrechnung, int art) {
        List<ZahlungsAuftrag> aufträge = zahlungsAuftragRepository
                .getOffeneZahlungen(abrechnung,art);
        for (ZahlungsAuftrag a : aufträge) {
            entferneZahlungsauftrag(a);
        }
    }

    private void entferneZahlungsauftrag(ZahlungsAuftrag a) {
        a.setStorniert(new Date());
        bucheStorno(a.getAbrechnung(), a.getBetrag(), a.getBuchungsart(), kontonrGuthaben);
    }

    private void bucheStorno(Abrechnung abrechnung, MonetaryAmount betrag,
            int buchungsart, SachKonto sachKonto) {
        BetragsBündelMap<SachKonto> beträge = new BetragsBündelMap<>();
        beträge.put(sachKonto, betrag.negate());
        Beschreibung beschreibung = new Beschreibung(buchungsart, "Storno");
        BuchungsAuftrag<SachKonto> auftrag = new BuchungsAuftrag<SachKonto>(
                beschreibung, beträge);
        erzeugeBuchung(auftrag, abrechnung);
    }
}
