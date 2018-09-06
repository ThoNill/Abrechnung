package org.nill.buchhaltung.eingang;

import java.util.HashMap;
import java.util.Map;

import javax.money.MonetaryAmount;

import org.nill.basiskomponenten.ddd.Value;
import org.nill.basiskomponenten.gemeinsam.BetragsB�ndel;
import org.nill.basiskomponenten.mathe.kopfpos.KopfMitPositionen;

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

    public synchronized Map<Integer, Long> getVerbundenMit() {
        if (verbundenMit == null) {
            verbundenMit = new HashMap<>();
        }
        return verbundenMit;
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

    public Beschreibung getBeschreibung() {
        return getKopf();
    }
}
