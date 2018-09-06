package org.nill.buchhaltung.eingang;

import java.util.HashMap;
import java.util.Map;

import javax.money.MonetaryAmount;

import org.nill.basiskomponenten.ddd.Value;
import org.nill.basiskomponenten.gemeinsam.BetragsBündel;
import org.nill.basiskomponenten.mathe.kopfpos.KopfMitPositionen;

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

    public synchronized Map<Integer, Long> getVerbundenMit() {
        if (verbundenMit == null) {
            verbundenMit = new HashMap<>();
        }
        return verbundenMit;
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

    public Beschreibung getBeschreibung() {
        return getKopf();
    }
}
