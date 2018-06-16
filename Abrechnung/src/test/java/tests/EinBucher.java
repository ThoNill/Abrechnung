package tests;

import javax.money.MonetaryAmount;

import app.entities.Abrechnung;
import app.entities.Buchung;
import app.entities.KontoBewegung;
import app.repositories.BuchungRepository;
import app.repositories.KontoBewegungRepository;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.gemeinsam.BetragsBündel;
import boundingContext.gemeinsam.BetragsBündelMap;

public class EinBucher {

    protected BuchungRepository buchungRepository;

    protected KontoBewegungRepository kontoBewegungRepository;

    public EinBucher(BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository) {
        super();
        this.buchungRepository = buchungRepository;
        this.kontoBewegungRepository = kontoBewegungRepository;
    }

    public Buchung erzeugeBuchung(BuchungsAuftrag<Position> auftrag,
            Abrechnung abrechnung) {
        if (!auftrag.isEmpty()) {
            BetragsBündel<Position> beträge = auftrag.getPositionen();
            Buchung buchung = new Buchung();
            buchung.setText(auftrag.getBeschreibung().getText());
            buchung.setArt(auftrag.getBeschreibung().getArt());
            buchung.setAbrechnung(abrechnung);
            buchung = buchungRepository.save(buchung);
            for (Position p : beträge.getKeys()) {
                MonetaryAmount betrag = beträge.getValue(p);
                bewegungHinzufügen(buchung, p, betrag);
            }
            return buchungRepository.save(buchung);
        }
        return null;
    }

    private void bewegungHinzufügen(Buchung buchung, Position p,
            MonetaryAmount betrag) {
        if (!betrag.isZero()) {
            KontoBewegung bew = new KontoBewegung();
            bew.setBetrag(betrag);
            bew.setArt(1);
            bew.setKontoNr(p.ordinal());
            bew = kontoBewegungRepository.save(bew);
            bew.setBuchung(buchung);
            buchung.addBewegungen(bew);
        }
    }

    public BetragsBündel<Position> beträgeEinerBuchungsartHolen(
            Abrechnung abrechnung, int art) {
        BetragsBündelMap<Position> beträge = new BetragsBündelMap<>();
        for (Object o : buchungRepository.getSumBewegungen(abrechnung, art)) {
            Object[] werte = (Object[]) o;
            Position p = Position.values()[(int) werte[0]];
            beträge.put(p, (MonetaryAmount) werte[1]);
        }
        return beträge;
    }

    public Buchung erzeugeDifferenzBuchung(BuchungsAuftrag<Position> auftrag,
            Abrechnung abrechnung) {
        BetragsBündel<Position> aktuell = beträgeEinerBuchungsartHolen(
                abrechnung, auftrag.getBeschreibung().getArt());
        BetragsBündel<Position> differenz = (BetragsBündel<Position>) aktuell
                .subtract(auftrag.getPositionen(), aktuell);
        BuchungsAuftrag<Position> differenzAuftrag = new BuchungsAuftrag<>(
                auftrag.getBeschreibung(), differenz);
        return erzeugeBuchung(differenzAuftrag, abrechnung);
    }

}