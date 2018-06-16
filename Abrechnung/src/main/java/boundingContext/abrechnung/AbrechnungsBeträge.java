package boundingContext.abrechnung;

import javax.money.MonetaryAmount;

import mathe.bündel.Bündel;
import boundingContext.gemeinsam.BetragsBündelMap;

public class AbrechnungsBeträge<KEY> extends BetragsBündelMap<KEY> {

    public AbrechnungsBeträge() {
        super();
    }

    public AbrechnungsBeträge(Bündel<KEY, MonetaryAmount> map) {
        super(map);
    }

}
