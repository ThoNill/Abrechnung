package boundingContext.abrechnung.sammler;

import java.util.ArrayList;

import javax.money.MonetaryAmount;

import betrag.Geld;
import boundingContext.abrechnung.AbrechnungsBetr�ge;
import boundingContext.buchhaltung.eingang.BuchungsAuftr�ge;
import boundingContext.gemeinsam.BetragsB�ndel;

public class BuchungsAuftr�geAbfrage<ID, KEY> extends
        ArrayList<BuchungAuftr�geEinzelAbfrage<ID, KEY>> {
    private KEY name;

    public MonetaryAmount summiere(BuchungsAuftr�ge<ID, KEY> auftr�ge) {
        MonetaryAmount betrag = Geld.getNull();
        for (BuchungAuftr�geEinzelAbfrage<ID, KEY> a : this) {
            betrag = betrag.add(a.summiere(auftr�ge));
        }
        return betrag;
    }

    public AbrechnungsBetr�ge<KEY> erg�nzen(BetragsB�ndel<KEY> b�ndel,
            BuchungsAuftr�ge<ID, KEY> auftr�ge) {
        MonetaryAmount betrag = summiere(auftr�ge);
        AbrechnungsBetr�ge<KEY> neuesB�ndel = new AbrechnungsBetr�ge<>(b�ndel);
        neuesB�ndel.put(name, betrag);
        return neuesB�ndel;
    }
}
