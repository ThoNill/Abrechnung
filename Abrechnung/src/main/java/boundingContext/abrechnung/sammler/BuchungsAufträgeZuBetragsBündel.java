package boundingContext.abrechnung.sammler;

import java.util.ArrayList;

import boundingContext.abrechnung.AbrechnungsBetr�ge;
import boundingContext.abrechnung.Betr�geEinspeisen;
import boundingContext.buchhaltung.eingang.BuchungsAuftr�ge;

public class BuchungsAuftr�geZuBetragsB�ndel<ID, KEY>

extends ArrayList<BuchungsAuftr�geAbfrage> implements
        Betr�geEinspeisen<KEY, BuchungsAuftr�ge<ID, KEY>> {

    @Override
    public AbrechnungsBetr�ge<KEY> apply(BuchungsAuftr�ge<ID, KEY> auftr�ge) {
        AbrechnungsBetr�ge<KEY> betr�ge = new AbrechnungsBetr�ge<>();
        for (BuchungsAuftr�geAbfrage<ID, KEY> abfrage : this) {
            betr�ge = abfrage.erg�nzen(betr�ge, auftr�ge);
        }
        return betr�ge;
    }

}
