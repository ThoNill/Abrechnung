package boundingContext.abrechnung.sammler;

import java.util.ArrayList;

import boundingContext.abrechnung.AbrechnungsBeträge;
import boundingContext.abrechnung.BeträgeEinspeisen;
import boundingContext.buchhaltung.eingang.BuchungsAufträge;

public class BuchungsAufträgeZuBetragsBündel<ID, KEY>

extends ArrayList<BuchungsAufträgeAbfrage> implements
        BeträgeEinspeisen<KEY, BuchungsAufträge<ID, KEY>> {

    @Override
    public AbrechnungsBeträge<KEY> apply(BuchungsAufträge<ID, KEY> aufträge) {
        AbrechnungsBeträge<KEY> beträge = new AbrechnungsBeträge<>();
        for (BuchungsAufträgeAbfrage<ID, KEY> abfrage : this) {
            beträge = abfrage.ergänzen(beträge, aufträge);
        }
        return beträge;
    }

}
