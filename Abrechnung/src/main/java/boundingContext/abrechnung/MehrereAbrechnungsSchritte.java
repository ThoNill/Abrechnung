package boundingContext.abrechnung;

import java.util.ArrayList;
import java.util.Objects;

import ddd.Value;

public class MehrereAbrechnungsSchritte<KEY> extends
        ArrayList<AbrechnungsSchritt<KEY>> implements Value {

    public AbrechnungsBeträge<KEY> apply(AbrechnungsBeträge<KEY> bündel) {
        Objects.requireNonNull(bündel);

        AbrechnungsBeträge<KEY> neuesBündel = bündel;
        for (AbrechnungsSchritt<KEY> schritt : this) {
            neuesBündel = schritt.apply(neuesBündel);
        }
        return neuesBündel;

    }

}
