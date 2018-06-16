package boundingContext.abrechnung.sammler;

import javax.money.MonetaryAmount;

import betrag.Geld;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.buchhaltung.eingang.BuchungsAufträge;

public class BuchungAufträgeEinzelAbfrage<ID, KEY> {
    private int art;
    private KEY[] positionen;

    public BuchungAufträgeEinzelAbfrage(int art, KEY... positionen) {
        this.art = art;
        this.positionen = positionen;
    }

    public MonetaryAmount summiere(BuchungsAufträge<ID, KEY> aufträge) {
        for (BuchungsAuftrag<KEY> a : aufträge) {
            if (a.getBeschreibung().getArt() == art) {
                return a.getPositionen().getSumme(positionen);
            }
        }
        return Geld.getNull();
    }

}
