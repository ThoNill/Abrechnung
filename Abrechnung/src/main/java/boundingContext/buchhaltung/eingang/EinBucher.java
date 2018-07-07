package boundingContext.buchhaltung.eingang;

import javax.money.MonetaryAmount;

import boundingContext.abrechnung.aufz�hlungen.SachKonto;
import boundingContext.abrechnung.aufz�hlungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.Buchung;
import boundingContext.abrechnung.entities.KontoBewegung;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.gemeinsam.BetragsB�ndel;
import boundingContext.gemeinsam.BetragsB�ndelMap;

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
            BetragsB�ndel<SachKonto> betr�ge = auftrag.getPositionen();
            Buchung buchung = new Buchung();
            buchung.setText(auftrag.getBeschreibung().getText());
            buchung.setArt(auftrag.getBeschreibung().getArt());
            buchung.setAbrechnung(abrechnung);
            buchung = buchungRepository.save(buchung);
            for (SachKonto p : betr�ge.getKeys()) {
                MonetaryAmount betrag = betr�ge.getValue(p);
                bewegungHinzuf�gen(buchung, p, betrag);
            }
            return buchungRepository.save(buchung);
        }
        return null;
    }

    private void bewegungHinzuf�gen(Buchung buchung, SachKonto p,
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

    public BetragsB�ndel<SachKonto> betr�geEinerBuchungsartHolen(
            Abrechnung abrechnung, int art) {
        BetragsB�ndelMap<SachKonto> betr�ge = new BetragsB�ndelMap<>();
        for (Object o : buchungRepository.getSumBewegungen(abrechnung, art)) {
            Object[] werte = (Object[]) o;
            SachKonto p = sachKontoFrom((int) werte[0]);
            betr�ge.put(p, (MonetaryAmount) werte[1]);
        }
        return betr�ge;
    }

    public Buchung erzeugeDifferenzBuchung(BuchungsAuftrag<SachKonto> auftrag,
            Abrechnung abrechnung) {
        BetragsB�ndel<SachKonto> aktuell = betr�geEinerBuchungsartHolen(
                abrechnung, auftrag.getBeschreibung().getArt());
        BetragsB�ndel<SachKonto> differenz = (BetragsB�ndel<SachKonto>) aktuell
                .subtract(auftrag.getPositionen(), aktuell);
        BuchungsAuftrag<SachKonto> differenzAuftrag = new BuchungsAuftrag<>(
                auftrag.getBeschreibung(), differenz);
        return erzeugeBuchung(differenzAuftrag, abrechnung);
    }

}