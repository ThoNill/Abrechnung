package org.nill.zahlungen.actions;

import java.util.Date;
import java.util.List;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.abrechnung.aufz�hlungen.SachKontoProvider;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.ZahlungsAuftrag;
import org.nill.abrechnung.repositories.ZahlungsAuftragRepository;
import org.nill.basiskomponenten.gemeinsam.BetragsB�ndelMap;
import org.nill.buchhaltung.eingang.Beschreibung;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;
import org.nill.buchhaltung.eingang.EinBucher;
import org.springframework.transaction.annotation.Transactional;

public class ZahlungenEntfernen extends EinBucher {
    public ZahlungenEntfernen(SachKontoProvider sachKontoProvider) {
        super(sachKontoProvider);
    }

    @Transactional("dbATransactionManager")
    public void entferneZahlungsauftr�geFallsRestguthaben(Abrechnung abrechnung) {
        MonetaryAmount saldo = getBuchungRepository().getSaldo(abrechnung);
        if (!saldo.isZero()) {
            entferneZahlungsauftr�ge(abrechnung);
            entferneRestGuthaben(abrechnung);
            entferneRestSchulden(abrechnung);
        }
    }

    @Transactional("dbATransactionManager")
    public void entferneZahlungsauftr�ge(Abrechnung abrechnung) {
        ZahlungsAuftragRepository zahlungsAuftragRepository = getZahlungsAuftragRepository();
        List<ZahlungsAuftrag> auftr�ge = zahlungsAuftragRepository
                .getOffeneZahlungen(abrechnung, ABGLEICH_GUTHABEN());
        for (ZahlungsAuftrag a : auftr�ge) {
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
        BetragsB�ndelMap<SachKonto> betr�ge = new BetragsB�ndelMap<>();
        betr�ge.put(sachKonto, betrag.negate());
        Beschreibung beschreibung = new Beschreibung(buchungsart, "Storno");
        BuchungsAuftrag<SachKonto> auftrag = new BuchungsAuftrag<SachKonto>(
                beschreibung, betr�ge);
        erzeugeBuchung(auftrag, abrechnung);
    }
}
