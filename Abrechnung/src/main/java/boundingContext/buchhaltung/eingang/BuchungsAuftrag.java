package boundingContext.buchhaltung.eingang;

import javax.money.MonetaryAmount;

import mathe.kopfpos.KopfMitPositionen;
import boundingContext.gemeinsam.BetragsB�ndel;
import ddd.Value;

public class BuchungsAuftrag<KEY>
        extends
        KopfMitPositionen<Beschreibung, KEY, MonetaryAmount, BetragsB�ndel<KEY>>
        implements Value {

    public BuchungsAuftrag(Beschreibung beschreibung,
            BetragsB�ndel<KEY> positionen) {
        super(beschreibung, positionen);
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
}
