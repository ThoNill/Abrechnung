package boundingContext.buchhaltung.eingang;

import java.util.HashMap;

import javax.money.MonetaryAmount;

import mathe.kopfpos.KopfMitPositionen;
import boundingContext.gemeinsam.BetragsBündel;
import ddd.Value;

public class BuchungsAuftrag<KEY>
        extends
        KopfMitPositionen<Beschreibung, KEY, MonetaryAmount, BetragsBündel<KEY>>
        implements Value {

    private HashMap<Integer, Long> verbundenMit;

    public BuchungsAuftrag(Beschreibung beschreibung,
            BetragsBündel<KEY> positionen) {
        super(beschreibung, positionen);
    }

    public void verbinde(int rolle, Long value) {
        getVerbundenMit().put(rolle, value);
    }

    public Beschreibung getBeschreibung() {
        return getKopf();
    }

    public boolean isEmpty() {
        BetragsBündel<KEY> bündel = getPositionen();
        for (KEY p : bündel.getKeys()) {
            MonetaryAmount betrag = bündel.getValue(p);
            if (!betrag.isZero()) {
                return false;
            }
        }
        return true;
    }

    public synchronized HashMap<Integer, Long> getVerbundenMit() {
        if (verbundenMit == null) {
            verbundenMit = new HashMap<>();
        }
        return verbundenMit;
    }
}
