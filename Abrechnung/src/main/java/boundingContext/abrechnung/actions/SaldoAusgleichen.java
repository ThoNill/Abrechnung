package boundingContext.abrechnung.actions;

import javax.money.MonetaryAmount;

import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.buchhaltung.eingang.EinBucher;
import boundingContext.gemeinsam.BetragsBündelMap;

public class SaldoAusgleichen extends EinBucher {
    private String textGuthaben;
    private String textSchulden;

    public SaldoAusgleichen(SachKontoProvider sachKontoProvider,
            String textGuthaben, String textSchulden) {
        super(sachKontoProvider);
        this.textGuthaben = textGuthaben;
        this.textSchulden = textSchulden;
    }

    public void saldoAusgleichen(Abrechnung abrechnung) {
        MonetaryAmount saldo = getBuchungRepository().getSaldo(abrechnung);
        if (saldo.isNegative()) {
            buche(abrechnung, ABGLEICH_SCHULDEN(), SCHULDEN(), textSchulden,
                    saldo.negate());
        } else {
            buche(abrechnung, ABGLEICH_GUTHABEN(), GUTHABEN(), textGuthaben,
                    saldo.negate());
        }
    }

    private void buche(Abrechnung abrechnung, int buchungstyp,
            SachKonto kontonr, String text, MonetaryAmount betrag) {
        BuchungsAuftrag<SachKonto> auftrag = erzeugeBuchungsAuftrag(
                buchungstyp, kontonr, text, betrag);
        erzeugeBuchung(auftrag, abrechnung);
    }

    private BuchungsAuftrag<SachKonto> erzeugeBuchungsAuftrag(int buchungstyp,
            SachKonto kontonr, String text, MonetaryAmount betrag) {
        BetragsBündelMap<SachKonto> beträge = new BetragsBündelMap<>();
        beträge.put(kontonr, betrag);
        Beschreibung beschreibung = new Beschreibung(buchungstyp, text);
        return new BuchungsAuftrag<SachKonto>(beschreibung, beträge);
    }

}
