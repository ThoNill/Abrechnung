package boundingContext.gemeinsam;

import mathe.bündel.BündelMap;
import mathe.bündel.GruppenBündelMap;
import mathe.gruppen.IntegerAdditiv;

public class AnzahlBündelMap<KEY> extends GruppenBündelMap<KEY, Integer>
        implements AnzahlBündel<KEY> {

    public AnzahlBündelMap(BündelMap<KEY, Integer> map) {
        super(IntegerAdditiv.GRUPPE, map);
    }

    public AnzahlBündelMap() {
        super(IntegerAdditiv.GRUPPE);
    }

    @Override
    public String toString() {
        return "Werte [entrySet=" + entrySet() + "]";
    }

}
