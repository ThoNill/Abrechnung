package org.nill.zahlungen.actions;

import java.util.Date;
import java.util.List;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.actions.EinBucher;
import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IZahlungsAuftrag;
import org.nill.abrechnung.interfaces.IZahlungsAuftragRepository;
import org.nill.abrechnung.interfaces.Umgebung;
import org.nill.basiskomponenten.gemeinsam.BetragsB�ndelMap;
import org.nill.buchhaltung.eingang.Beschreibung;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;
import org.springframework.transaction.annotation.Transactional;

public class ZahlungenEntfernen extends EinBucher {
    public ZahlungenEntfernen(Umgebung umgebung) {
        super(umgebung);
    }

    @Transactional("dbATransactionManager")
    public void entferneZahlungsauftr�geFallsRestguthaben(IAbrechnung abrechnung) {
        MonetaryAmount saldo = getBuchungRepository().getSaldo(abrechnung);
        if (!saldo.isZero()) {
            entferneZahlungsauftr�ge(abrechnung);
            entferneRestGuthaben(abrechnung);
            entferneRestSchulden(abrechnung);
        }
    }

    @Transactional("dbATransactionManager")
    public void entferneZahlungsauftr�ge(IAbrechnung abrechnung) {
        IZahlungsAuftragRepository zahlungsAuftragRepository = getZahlungsAuftragRepository();
        List<IZahlungsAuftrag> auftr�ge = zahlungsAuftragRepository
                .getOffeneZahlungen(abrechnung, ABGLEICH_GUTHABEN());
        for (IZahlungsAuftrag a : auftr�ge) {
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
        BetragsB�ndelMap<SachKonto> betr�ge = new BetragsB�ndelMap<>();
        betr�ge.put(sachKonto, betrag.negate());
        Beschreibung beschreibung = new Beschreibung(buchungsart, "Storno");
        BuchungsAuftrag<SachKonto> auftrag = new BuchungsAuftrag<SachKonto>(
                beschreibung, betr�ge);
        erzeugeBuchung(auftrag, abrechnung);
    }
}
