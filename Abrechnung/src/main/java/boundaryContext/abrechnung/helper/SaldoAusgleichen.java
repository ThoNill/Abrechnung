package boundaryContext.abrechnung.helper;

import javax.money.MonetaryAmount;

import boundaryContext.abrechnung.entities.Abrechnung;
import boundaryContext.abrechnung.repositories.BuchungRepository;
import boundaryContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.abrechnung.aufzählungen.Position;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.gemeinsam.BetragsBündelMap;

public class SaldoAusgleichen extends EinBucher {
    private int buchungstypGuthaben;
    private Position kontonrGuthaben;
    private String textGuthaben;

    private int buchungstypSchulden;
    private Position kontonrSchulden;
    private String textSchulden;

    public SaldoAusgleichen(BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository,
            int buchungstypGuthaben, Position kontonrGuthaben,
            String textGuthaben, int buchungstypSchulden,
            Position kontonrSchulden, String textSchulden) {
        super(buchungRepository, kontoBewegungRepository);
        this.buchungstypGuthaben = buchungstypGuthaben;
        this.kontonrGuthaben = kontonrGuthaben;
        this.textGuthaben = textGuthaben;
        this.buchungstypSchulden = buchungstypSchulden;
        this.kontonrSchulden = kontonrSchulden;
        this.textSchulden = textSchulden;
    }

    public void saldoAusgleichen(Abrechnung abrechnung) {
        MonetaryAmount saldo = buchungRepository.getSaldo(abrechnung);
        if (saldo.isNegative()) {
            buche(abrechnung, buchungstypSchulden, kontonrSchulden,
                    textSchulden, saldo.negate());
        } else {
            buche(abrechnung, buchungstypGuthaben, kontonrGuthaben,
                    textGuthaben, saldo.negate());
        }
    }

    private void buche(Abrechnung abrechnung, int buchungstyp,
            Position kontonr, String text, MonetaryAmount betrag) {
        BuchungsAuftrag<Position> auftrag = erzeugeBuchungsAuftrag(buchungstyp,
                kontonr, text, betrag);
        erzeugeBuchung(auftrag, abrechnung);
    }

    private BuchungsAuftrag<Position> erzeugeBuchungsAuftrag(int buchungstyp,
            Position kontonr, String text, MonetaryAmount betrag) {
        BetragsBündelMap<Position> beträge = new BetragsBündelMap<>();
        beträge.put(kontonr, betrag);
        Beschreibung beschreibung = new Beschreibung(buchungstyp, text);
        return new BuchungsAuftrag<Position>(beschreibung, beträge);
    }

}
