package boundingContext.abrechnung;

import javax.money.MonetaryAmount;

import mathe.b�ndel.B�ndel;
import boundingContext.gemeinsam.BetragsB�ndelMap;

public class AbrechnungsBetr�ge<KEY> extends BetragsB�ndelMap<KEY> {

    public AbrechnungsBetr�ge() {
        super();
    }

    public AbrechnungsBetr�ge(B�ndel<KEY, MonetaryAmount> map) {
        super(map);
    }

}
