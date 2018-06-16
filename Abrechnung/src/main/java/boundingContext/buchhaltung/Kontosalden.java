package boundingContext.buchhaltung;

import mathe.paare.Paar;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.gemeinsam.BetragsBündelMap;

public class Kontosalden extends BetragsBündelMap<Paar<Beschreibung, Konto>> {

    public Kontosalden() {
        super();
    }

}
