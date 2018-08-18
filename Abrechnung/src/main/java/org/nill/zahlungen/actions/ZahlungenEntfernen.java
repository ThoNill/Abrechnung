package org.nill.zahlungen.actions;

import java.util.Date;
import java.util.List;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.actions.EinBucher;
import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IZahlungsAuftrag;
import org.nill.abrechnung.interfaces.IZahlungsAuftragRepository;
import org.nill.abrechnung.interfaces.Umgebung;
import org.nill.basiskomponenten.gemeinsam.BetragsBündelMap;
import org.nill.buchhaltung.eingang.Beschreibung;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;
import org.springframework.transaction.annotation.Transactional;

public class ZahlungenEntfernen extends EinBucher {
    public ZahlungenEntfernen(Umgebung umgebung) {
        super(umgebung);
    }

    @Transactional("dbATransactionManager")
    public void entferneZahlungsaufträgeFallsRestguthaben(IAbrechnung abrechnung) {
        MonetaryAmount saldo = getBuchungRepository().getSaldo(abrechnung);
        if (!saldo.isZero()) {
            entferneZahlungsaufträge(abrechnung);
            entferneRestGuthaben(abrechnung);
            entferneRestSchulden(abrechnung);
        }
    }

    @Transactional("dbATransactionManager")
    public void entferneZahlungsaufträge(IAbrechnung abrechnung) {
        IZahlungsAuftragRepository zahlungsAuftragRepository = getZahlungsAuftragRepository();
        List<IZahlungsAuftrag> aufträge = zahlungsAuftragRepository
                .getOffeneZahlungen(abrechnung, ABGLEICH_GUTHABEN());
        for (IZahlungsAuftrag a : aufträge) {
            entferneZahlungsauftrag(a);
        }
    }

    private void entferneRestGuthaben(IAbrechnung abrechnung) {
        MonetaryAmount betrag = getBuchungRepository().getSumKonto(abrechnung,
                ABGLEICH_GUTHABEN(), GUTHABEN().ordinal());
        if (betrag != null && !betrag.isZero()) {
            bucheStorno(abrechnung, betrag, ABGLEICH_GUTHABEN(), GUTHABEN());
        }
    }

    private void entferneRestSchulden(IAbrechnung abrechnung) {
        MonetaryAmount betrag = getBuchungRepository().getSumKonto(abrechnung,
                ABGLEICH_SCHULDEN(), SCHULDEN().ordinal());
        if (betrag != null && !betrag.isZero()) {
            bucheStorno(abrechnung, betrag, ABGLEICH_SCHULDEN(), SCHULDEN());
        }
    }

    private void entferneZahlungsauftrag(IZahlungsAuftrag a) {
        a.setStorniert(new Date());
        bucheStorno(a.getAbrechnung(), a.getBetrag(), a.getBuchungsart(),
                AUSZUZAHLEN());
    }

    private void bucheStorno(IAbrechnung abrechnung, MonetaryAmount betrag,
            int buchungsart, SachKonto sachKonto) {
        BetragsBündelMap<SachKonto> beträge = new BetragsBündelMap<>();
        beträge.put(sachKonto, betrag.negate());
        Beschreibung beschreibung = new Beschreibung(buchungsart, "Storno");
        BuchungsAuftrag<SachKonto> auftrag = new BuchungsAuftrag<SachKonto>(
                beschreibung, beträge);
        erzeugeBuchung(auftrag, abrechnung);
    }
}
