package boundingContext.buchhaltung.eingang;

import javax.money.MonetaryAmount;

import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.Buchung;
import boundingContext.abrechnung.entities.KontoBewegung;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.gemeinsam.BetragsBündel;
import boundingContext.gemeinsam.BetragsBündelMap;

public class EinBucher extends SachKontoDelegate {

    protected BuchungRepository buchungRepository;

    protected KontoBewegungRepository kontoBewegungRepository;

    public EinBucher(SachKontoProvider sachKontoProvider,
            BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository) {
        super(sachKontoProvider);
        this.buchungRepository = buchungRepository;
        this.kontoBewegungRepository = kontoBewegungRepository;
    }

    public Buchung erzeugeBuchung(BuchungsAuftrag<SachKonto> auftrag,
            Abrechnung abrechnung) {
        if (!auftrag.isEmpty()) {
            BetragsBündel<SachKonto> beträge = auftrag.getPositionen();
            Buchung buchung = new Buchung();
            buchung.setText(auftrag.getBeschreibung().getText());
            buchung.setArt(auftrag.getBeschreibung().getArt());
            buchung.setAbrechnung(abrechnung);
            buchung = buchungRepository.save(buchung);
            for (SachKonto p : beträge.getKeys()) {
                MonetaryAmount betrag = beträge.getValue(p);
                bewegungHinzufügen(buchung, p, betrag);
            }
            return buchungRepository.save(buchung);
        }
        return null;
    }

    private void bewegungHinzufügen(Buchung buchung, SachKonto p,
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

    public BetragsBündel<SachKonto> beträgeEinerBuchungsartHolen(
            Abrechnung abrechnung, int art) {
        BetragsBündelMap<SachKonto> beträge = new BetragsBündelMap<>();
        for (Object o : buchungRepository.getSumBewegungen(abrechnung, art)) {
            Object[] werte = (Object[]) o;
            SachKonto p = sachKontoFrom((int) werte[0]);
            beträge.put(p, (MonetaryAmount) werte[1]);
        }
        return beträge;
    }

    public Buchung erzeugeDifferenzBuchung(BuchungsAuftrag<SachKonto> auftrag,
            Abrechnung abrechnung) {
        BetragsBündel<SachKonto> aktuell = beträgeEinerBuchungsartHolen(
                abrechnung, auftrag.getBeschreibung().getArt());
        BetragsBündel<SachKonto> differenz = (BetragsBündel<SachKonto>) aktuell
                .subtract(auftrag.getPositionen(), aktuell);
        BuchungsAuftrag<SachKonto> differenzAuftrag = new BuchungsAuftrag<>(
                auftrag.getBeschreibung(), differenz);
        return erzeugeBuchung(differenzAuftrag, abrechnung);
    }

}