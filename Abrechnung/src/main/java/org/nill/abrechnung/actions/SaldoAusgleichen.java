package org.nill.abrechnung.actions;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.abrechnung.entities.Mandant;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.Umgebung;
import org.nill.basiskomponenten.gemeinsam.BetragsBündelMap;
import org.nill.buchhaltung.eingang.Beschreibung;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;

/**
 * Nach der Berechnung der Gebühren bleibt auf dem Konto des {@link IMandant} ein
 * Saldo, dieses wird entweder durch eine Buchung in die andere Richtung ausgeglichen.  
 * 
 * @author Thomas Nill
 *
 */
public class SaldoAusgleichen extends EinBucher {
    private String textGuthaben;
    private String textSchulden;

    public SaldoAusgleichen(Umgebung umgebung,
            String textGuthaben, String textSchulden) {
        super(umgebung);
        this.textGuthaben = textGuthaben;
        this.textSchulden = textSchulden;
    }

    public void saldoAusgleichen(IAbrechnung abrechnung) {
        MonetaryAmount saldo = getBuchungRepository().getSaldo(abrechnung);
        if (saldo.isNegative()) {
            buche(abrechnung, ABGLEICH_SCHULDEN(), SCHULDEN(), textSchulden,
                    saldo.negate());
        } else {
            buche(abrechnung, ABGLEICH_GUTHABEN(), GUTHABEN(), textGuthaben,
                    saldo.negate());
        }
    }

    private void buche(IAbrechnung abrechnung, int buchungstyp,
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
        return new BuchungsAuftrag<>(beschreibung, beträge);
    }

}
