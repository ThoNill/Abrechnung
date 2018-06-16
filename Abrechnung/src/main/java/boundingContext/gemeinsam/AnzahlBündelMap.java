package boundingContext.gemeinsam;

import mathe.b�ndel.B�ndelMap;
import mathe.b�ndel.GruppenB�ndelMap;
import mathe.gruppen.IntegerAdditiv;

public class AnzahlB�ndelMap<KEY> extends GruppenB�ndelMap<KEY, Integer>
        implements AnzahlB�ndel<KEY> {

    public AnzahlB�ndelMap(B�ndelMap<KEY, Integer> map) {
        super(IntegerAdditiv.GRUPPE, map);
    }

    public AnzahlB�ndelMap() {
        super(IntegerAdditiv.GRUPPE);
    }

    @Override
    public String toString() {
        return "Werte [entrySet=" + entrySet() + "]";
    }

}
