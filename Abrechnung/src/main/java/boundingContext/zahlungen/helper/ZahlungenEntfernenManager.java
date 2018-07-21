package boundingContext.zahlungen.helper;

import java.util.Date;
import java.util.List;

import javax.money.MonetaryAmount;

import org.springframework.transaction.annotation.Transactional;

import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.ZahlungsAuftrag;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.buchhaltung.eingang.EinBucher;
import boundingContext.gemeinsam.BetragsBündelMap;

public class ZahlungenEntfernenManager extends EinBucher {
    private ZahlungsAuftragRepository zahlungsAuftragRepository;
 
    public ZahlungenEntfernenManager(SachKontoProvider sachKontoProvider,
            BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository,
            ZahlungsAuftragRepository zahlungsAuftragRepository,
            AbrechnungRepository abrechnungRepository) {
        super(sachKontoProvider, buchungRepository, kontoBewegungRepository,abrechnungRepository);
        this.zahlungsAuftragRepository = zahlungsAuftragRepository;
    }

    @Transactional("dbATransactionManager")
    public void entferneZahlungsaufträgeFallsRestguthaben(Abrechnung abrechnung) {
        MonetaryAmount saldo = buchungRepository.getSaldo(abrechnung);
        if (!saldo.isZero()) {
            entferneZahlungsaufträge(abrechnung);
            entferneRestGuthaben(abrechnung);
            entferneRestSchulden(abrechnung);
        }
    }

    @Transactional("dbATransactionManager")
    public void entferneZahlungsaufträge(Abrechnung abrechnung) {
        List<ZahlungsAuftrag> aufträge = zahlungsAuftragRepository
                .getOffeneZahlungen(abrechnung,ABGLEICH_GUTHABEN());
        for (ZahlungsAuftrag a : aufträge) {
            entferneZahlungsauftrag(a);
        }
    }

    private void entferneRestGuthaben(Abrechnung abrechnung) {
        MonetaryAmount betrag = buchungRepository.getSumKonto(abrechnung,ABGLEICH_GUTHABEN(),GUTHABEN().ordinal());
        if (betrag != null && !betrag.isZero()) {
            bucheStorno(abrechnung,betrag,ABGLEICH_GUTHABEN(),GUTHABEN());
        }
    }

    private void entferneRestSchulden(Abrechnung abrechnung) {
        MonetaryAmount betrag = buchungRepository.getSumKonto(abrechnung,ABGLEICH_SCHULDEN(),SCHULDEN().ordinal());
        if (betrag != null && !betrag.isZero()) {
            bucheStorno(abrechnung,betrag,ABGLEICH_SCHULDEN(),SCHULDEN());
        }
    }

    
    private void entferneZahlungsauftrag(ZahlungsAuftrag a) {
        a.setStorniert(new Date());
        bucheStorno(a.getAbrechnung(), a.getBetrag(), a.getBuchungsart(),
                AUSZUZAHLEN());
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
