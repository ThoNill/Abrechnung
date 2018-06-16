package boundingContext.abrechnung.sammler;

import javax.money.MonetaryAmount;

import betrag.Geld;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.buchhaltung.eingang.BuchungsAuftr�ge;

public class BuchungAuftr�geEinzelAbfrage<ID, KEY> {
    private int art;
    private KEY[] positionen;

    public BuchungAuftr�geEinzelAbfrage(int art, KEY... positionen) {
        this.art = art;
        this.positionen = positionen;
    }

    public MonetaryAmount summiere(BuchungsAuftr�ge<ID, KEY> auftr�ge) {
        for (BuchungsAuftrag<KEY> a : auftr�ge) {
            if (a.getBeschreibung().getArt() == art) {
                return a.getPositionen().getSumme(positionen);
            }
        }
        return Geld.getNull();
    }

}
