package boundingContext.gemeinsam;

import mathe.b�ndel.GruppenB�ndelMap;
import mathe.gruppen.DoubleAdditiv;

public class DoubleB�ndelMap<KEY> extends GruppenB�ndelMap<KEY, Double> {

    public DoubleB�ndelMap() {
        super(DoubleAdditiv.GRUPPE);
    }

    @Override
    public String toString() {
        return "Werte [entrySet=" + entrySet() + "]";
    }

}
