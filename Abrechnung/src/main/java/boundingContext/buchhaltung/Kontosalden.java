package boundingContext.buchhaltung;

import mathe.paare.Paar;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.gemeinsam.BetragsB�ndelMap;

public class Kontosalden extends BetragsB�ndelMap<Paar<Beschreibung, Konto>> {

    public Kontosalden() {
        super();
    }

}
