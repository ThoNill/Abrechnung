package boundingContext.abrechnung.sammler;

import java.util.ArrayList;

import javax.money.MonetaryAmount;

import betrag.Geld;
import boundingContext.abrechnung.AbrechnungsBeträge;
import boundingContext.buchhaltung.eingang.BuchungsAufträge;
import boundingContext.gemeinsam.BetragsBündel;

public class BuchungsAufträgeAbfrage<ID, KEY> extends
        ArrayList<BuchungAufträgeEinzelAbfrage<ID, KEY>> {
    private KEY name;

    public MonetaryAmount summiere(BuchungsAufträge<ID, KEY> aufträge) {
        MonetaryAmount betrag = Geld.getNull();
        for (BuchungAufträgeEinzelAbfrage<ID, KEY> a : this) {
            betrag = betrag.add(a.summiere(aufträge));
        }
        return betrag;
    }

    public AbrechnungsBeträge<KEY> ergänzen(BetragsBündel<KEY> bündel,
            BuchungsAufträge<ID, KEY> aufträge) {
        MonetaryAmount betrag = summiere(aufträge);
        AbrechnungsBeträge<KEY> neuesBündel = new AbrechnungsBeträge<>(bündel);
        neuesBündel.put(name, betrag);
        return neuesBündel;
    }
}
