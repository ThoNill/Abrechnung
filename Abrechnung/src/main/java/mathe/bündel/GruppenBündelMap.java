package mathe.bündel;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;

import mathe.gruppen.Gruppe;

public class GruppenBündelMap<KEY, ELEMENT> extends BündelMap<KEY, ELEMENT>
        implements GruppenBündel<KEY, ELEMENT> {

    public GruppenBündelMap(Gruppe<ELEMENT> gruppe, Bündel<KEY, ELEMENT> map) {
        super(map);
        this.gruppe = gruppe;
    }

    private Gruppe<ELEMENT> gruppe;

    public GruppenBündelMap(Gruppe<ELEMENT> gruppe) {
        super();
        this.gruppe = gruppe;
    }

    @Override
    public GruppenBündelMap<KEY, ELEMENT> unit() {
        return new GruppenBündelMap<>(gruppe);
    }

    @Override
    public boolean isElement(GruppenBündel<KEY, ELEMENT> b) {
        return true;
    }

    @Override
    public boolean isUnit(GruppenBündel<KEY, ELEMENT> b) {
        Set<KEY> alle = b.getKeys();
        for (KEY k : alle) {
            if (!gruppe.isUnit(null2unit(getValue(k)))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ELEMENT getSumme(Iterable<KEY> keys) {
        ELEMENT summe = gruppe.unit();
        for (KEY key : keys) {
            ELEMENT v = getBetrag(key);
            summe = gruppe.add(summe, v);
        }
        return summe;
    }

    @Override
    public ELEMENT getSumme(KEY... positionen) {
        return getSumme(Arrays.asList(positionen));
    };

    @Override
    public ELEMENT getSumme() {
        return getSumme(getKeys());
    }

    @Override
    public ELEMENT getValue(KEY position) {
        return this.get(position);
    }

    public <TO_KEY> GruppenBündelMap<TO_KEY, ELEMENT> transformiereUndSummiereKeys(
            Function<KEY, TO_KEY> transformation) {
        return transformiereUndSummiereKeys(gruppe, transformation);
    }

    public <K> void summiere(GruppenBündelMap<K, ELEMENT> bündel, K key,
            ELEMENT a) {
        summiere(gruppe, bündel, key, a);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 3;
        for (KEY k : getKeys()) {
            ELEMENT b = getBetrag(k);
            if (!gruppe.isUnit(b)) {
                result += getBetrag(k).hashCode();
            }
        }
        result = prime * result;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        // if (!super.equals(obj))
        // return false;
        if (!(obj instanceof GruppenBündelMap))
            return false;
        GruppenBündelMap other = (GruppenBündelMap) obj;
        if (gruppe == null) {
            if (other.gruppe != null)
                return false;
        }
        GruppenBündelMap<KEY, ELEMENT> bündel = (GruppenBündelMap<KEY, ELEMENT>) obj;
        Set<KEY> keys = alleKeys(this, bündel);
        for (KEY k : keys) {
            if (!getBetrag(k).equals(bündel.getBetrag(k))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Gruppe<ELEMENT> getGruppe() {
        return gruppe;
    }

}
