package org.nill.zahlungen.actions;

import java.util.Date;
import java.util.List;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.abrechnung.aufzählungen.SachKontoProvider;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.ZahlungsAuftrag;
import org.nill.abrechnung.repositories.ZahlungsAuftragRepository;
import org.nill.basiskomponenten.gemeinsam.BetragsBündelMap;
import org.nill.buchhaltung.eingang.Beschreibung;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;
import org.nill.buchhaltung.eingang.EinBucher;
import org.springframework.transaction.annotation.Transactional;

public class ZahlungenEntfernen extends EinBucher {
    public ZahlungenEntfernen(SachKontoProvider sachKontoProvider) {
        super(sachKontoProvider);
    }

    @Transactional("dbATransactionManager")
    public void entferneZahlungsaufträgeFallsRestguthaben(Abrechnung abrechnung) {
        MonetaryAmount saldo = getBuchungRepository().getSaldo(abrechnung);
        if (!saldo.isZero()) {
            entferneZahlungsaufträge(abrechnung);
            entferneRestGuthaben(abrechnung);
            entferneRestSchulden(abrechnung);
        }
    }

    @Transactional("dbATransactionManager")
    public void entferneZahlungsaufträge(Abrechnung abrechnung) {
        ZahlungsAuftragRepository zahlungsAuftragRepository = getZahlungsAuftragRepository();
        List<ZahlungsAuftrag> aufträge = zahlungsAuftragRepository
                .getOffeneZahlungen(abrechnung, ABGLEICH_GUTHABEN());
        for (ZahlungsAuftrag a : aufträge) {
            entferneZahlungsauftrag(a);
        }
    }

    private void entferneRestGuthaben(Abrechnung abrechnung) {
        MonetaryAmount betrag = getBuchungRepository().getSumKonto(abrechnung,
                ABGLEICH_GUTHABEN(), GUTHABEN().ordinal());
        if (betrag != null && !betrag.isZero()) {
            bucheStorno(abrechnung, betrag, ABGLEICH_GUTHABEN(), GUTHABEN());
        }
    }

    private void entferneRestSchulden(Abrechnung abrechnung) {
        MonetaryAmount betrag = getBuchungRepository().getSumKonto(abrechnung,
                ABGLEICH_SCHULDEN(), SCHULDEN().ordinal());
        if (betrag != null && !betrag.isZero()) {
            bucheStorno(abrechnung, betrag, ABGLEICH_SCHULDEN(), SCHULDEN());
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
