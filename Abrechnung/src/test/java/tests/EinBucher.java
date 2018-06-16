package tests;

import javax.money.MonetaryAmount;

import app.entities.Abrechnung;
import app.entities.Buchung;
import app.entities.KontoBewegung;
import app.repositories.BuchungRepository;
import app.repositories.KontoBewegungRepository;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.gemeinsam.BetragsB�ndel;
import boundingContext.gemeinsam.BetragsB�ndelMap;

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
            BetragsB�ndel<Position> betr�ge = auftrag.getPositionen();
            Buchung buchung = new Buchung();
            buchung.setText(auftrag.getBeschreibung().getText());
            buchung.setArt(auftrag.getBeschreibung().getArt());
            buchung.setAbrechnung(abrechnung);
            buchung = buchungRepository.save(buchung);
            for (Position p : betr�ge.getKeys()) {
                MonetaryAmount betrag = betr�ge.getValue(p);
                bewegungHinzuf�gen(buchung, p, betrag);
            }
            return buchungRepository.save(buchung);
        }
        return null;
    }

    private void bewegungHinzuf�gen(Buchung buchung, Position p,
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

    public BetragsB�ndel<Position> betr�geEinerBuchungsartHolen(
            Abrechnung abrechnung, int art) {
        BetragsB�ndelMap<Position> betr�ge = new BetragsB�ndelMap<>();
        for (Object o : buchungRepository.getSumBewegungen(abrechnung, art)) {
            Object[] werte = (Object[]) o;
            Position p = Position.values()[(int) werte[0]];
            betr�ge.put(p, (MonetaryAmount) werte[1]);
        }
        return betr�ge;
    }

    public Buchung erzeugeDifferenzBuchung(BuchungsAuftrag<Position> auftrag,
            Abrechnung abrechnung) {
        BetragsB�ndel<Position> aktuell = betr�geEinerBuchungsartHolen(
                abrechnung, auftrag.getBeschreibung().getArt());
        BetragsB�ndel<Position> differenz = (BetragsB�ndel<Position>) aktuell
                .subtract(auftrag.getPositionen(), aktuell);
        BuchungsAuftrag<Position> differenzAuftrag = new BuchungsAuftrag<>(
                auftrag.getBeschreibung(), differenz);
        return erzeugeBuchung(differenzAuftrag, abrechnung);
    }

}