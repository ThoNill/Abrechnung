package boundingContext.gemeinsam;

import javax.money.MonetaryAmount;

import mathe.b�ndel.B�ndel;
import mathe.b�ndel.GruppenB�ndelMap;
import betrag.Geld;

public class BetragsB�ndelMap<KEY> extends
        GruppenB�ndelMap<KEY, MonetaryAmount> implements BetragsB�ndel<KEY> {

    public BetragsB�ndelMap(B�ndel<KEY, MonetaryAmount> map) {
        super(Geld.getGruppe(), map);
    }

    public BetragsB�ndelMap() {
        super(Geld.getGruppe());
    }

    @Override
    public String toString() {
        return "Werte [entrySet=" + entrySet() + "]";
    }

    public BetragsB�ndelMap<KEY> copy() {
        return (BetragsB�ndelMap<KEY>) transformElements((MonetaryAmount x) -> x);
    }

    @Override
    public BetragsB�ndelMap<KEY> unit() {
        return new BetragsB�ndelMap<KEY>();
    }
}
