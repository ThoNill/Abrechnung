package boundingContext.abrechnung;

import java.util.ArrayList;
import java.util.Objects;

import ddd.Value;

public class MehrereAbrechnungsSchritte<KEY> extends
        ArrayList<AbrechnungsSchritt<KEY>> implements Value {

    public AbrechnungsBetr�ge<KEY> apply(AbrechnungsBetr�ge<KEY> b�ndel) {
        Objects.requireNonNull(b�ndel);

        AbrechnungsBetr�ge<KEY> neuesB�ndel = b�ndel;
        for (AbrechnungsSchritt<KEY> schritt : this) {
            neuesB�ndel = schritt.apply(neuesB�ndel);
        }
        return neuesB�ndel;

    }

}
