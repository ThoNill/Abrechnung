package boundingContext.buchhaltung.eingang;

import java.util.HashMap;

import javax.money.MonetaryAmount;

import mathe.kopfpos.KopfMitPositionen;
import boundingContext.gemeinsam.BetragsB�ndel;
import ddd.Value;

public class BuchungsAuftrag<KEY>
        extends
        KopfMitPositionen<Beschreibung, KEY, MonetaryAmount, BetragsB�ndel<KEY>>
        implements Value {

    private HashMap<Integer, Long> verbundenMit;

    public BuchungsAuftrag(Beschreibung beschreibung,
            BetragsB�ndel<KEY> positionen) {
        super(beschreibung, positionen);
    }

    public void verbinde(int rolle, Long value) {
        getVerbundenMit().put(rolle, value);
    }

    public Beschreibung getBeschreibung() {
        return getKopf();
    }

    public boolean isEmpty() {
        BetragsB�ndel<KEY> b�ndel = getPositionen();
        for (KEY p : b�ndel.getKeys()) {
            MonetaryAmount betrag = b�ndel.getValue(p);
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
