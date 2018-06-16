package boundingContext.gemeinsam;

import javax.money.MonetaryAmount;

import mathe.bündel.Bündel;
import mathe.bündel.GruppenBündelMap;
import betrag.Geld;

public class BetragsBündelMap<KEY> extends
        GruppenBündelMap<KEY, MonetaryAmount> implements BetragsBündel<KEY> {

    public BetragsBündelMap(Bündel<KEY, MonetaryAmount> map) {
        super(Geld.getGruppe(), map);
    }

    public BetragsBündelMap() {
        super(Geld.getGruppe());
    }

    @Override
    public String toString() {
        return "Werte [entrySet=" + entrySet() + "]";
    }

    public BetragsBündelMap<KEY> copy() {
        return (BetragsBündelMap<KEY>) transformElements((MonetaryAmount x) -> x);
    }

    @Override
    public BetragsBündelMap<KEY> unit() {
        return new BetragsBündelMap<KEY>();
    }
}
