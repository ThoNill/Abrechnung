package boundingContext.gemeinsam;

import mathe.bündel.GruppenBündelMap;
import mathe.gruppen.DoubleAdditiv;

public class DoubleBündelMap<KEY> extends GruppenBündelMap<KEY, Double> {

    public DoubleBündelMap() {
        super(DoubleAdditiv.GRUPPE);
    }

    @Override
    public String toString() {
        return "Werte [entrySet=" + entrySet() + "]";
    }

}
