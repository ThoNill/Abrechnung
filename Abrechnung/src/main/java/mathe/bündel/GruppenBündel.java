package mathe.bündel;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import mathe.gruppen.Gruppe;

public interface GruppenBündel<KEY, ELEMENT> extends Bündel<KEY, ELEMENT>,
        Gruppe<GruppenBündel<KEY, ELEMENT>> {

    @Override
    Set<KEY> getKeys();

    @Override
    ELEMENT getValue(KEY position);

    @Override
    boolean isElement(GruppenBündel<KEY, ELEMENT> x);

    @Override
    boolean isUnit(GruppenBündel<KEY, ELEMENT> x);

    @Override
    GruppenBündel<KEY, ELEMENT> unit();

    @Override
    default GruppenBündel<KEY, ELEMENT> negate(GruppenBündel<KEY, ELEMENT> x) {
        return Objects.requireNonNull(x).negate();
    };

    ELEMENT getSumme(Iterable<KEY> keys);

    ELEMENT getSumme(KEY... positionen);

    ELEMENT getSumme();

    default <TO_KEY> GruppenBündelMap<TO_KEY, ELEMENT> transformiereUndSummiereKeys(
            Gruppe<ELEMENT> g, Function<KEY, TO_KEY> transformation) {
        Objects.requireNonNull(g);
        Objects.requireNonNull(transformation);

        GruppenBündelMap<TO_KEY, ELEMENT> bündel = new GruppenBündelMap<>(g);
        for (KEY k : getKeys()) {
            TO_KEY tk = transformation.apply(k);
            ELEMENT a = this.getValue(k);
            summiere(g, bündel, tk, a);
        }
        return bündel;
    }

    default <K> void summiere(Gruppe<ELEMENT> g,
            GruppenBündelMap<K, ELEMENT> bündel, K key, ELEMENT a) {
        Objects.requireNonNull(g);
        Objects.requireNonNull(a);
        Objects.requireNonNull(key);
        Objects.requireNonNull(g);
        Objects.requireNonNull(bündel);

        ELEMENT sum = g.add(bündel.getValue(key), a);
        bündel.put(key, sum);
    }

    public Gruppe<ELEMENT> getGruppe();

    @Override
    public default GruppenBündel<KEY, ELEMENT> add(
            GruppenBündel<KEY, ELEMENT> a, GruppenBündel<KEY, ELEMENT> b) {
        Gruppe<ELEMENT> gruppe = getGruppe();
        GruppenBündel<KEY, ELEMENT> ergebnis = unit();
        Set<KEY> alle = alleKeys(this, b);

        for (KEY k : alle) {
            ELEMENT s = gruppe.add(null2unit(a.getValue(k)),
                    null2unit(b.getValue(k)));
            if (!gruppe.isUnit(s)) {
                ergebnis.put(k, s);
            }
        }
        return ergebnis;
    }

    default ELEMENT null2unit(ELEMENT amount) {
        if (amount == null) {
            return getGruppe().unit();
        }
        return amount;
    }

    default ELEMENT getBetrag(KEY position) {
        return null2unit(getValue(position));
    }

    default GruppenBündel<KEY, ELEMENT> subtract(GruppenBündel<KEY, ELEMENT> a,
            GruppenBündel<KEY, ELEMENT> b) {
        Gruppe<ELEMENT> gruppe = getGruppe();
        GruppenBündel<KEY, ELEMENT> ergebnis = unit();
        Set<KEY> alle = alleKeys(a, b);

        for (KEY k : alle) {
            ELEMENT s = gruppe.add(null2unit(a.getValue(k)),
                    gruppe.negate(null2unit(b.getValue(k))));
            if (!gruppe.isUnit(s)) {
                ergebnis.put(k, s);
            }
        }
        return ergebnis;
    }

    default GruppenBündel<KEY, ELEMENT> negate() {
        Gruppe<ELEMENT> gruppe = getGruppe();
        GruppenBündel<KEY, ELEMENT> ergebnis = unit();
        Set<KEY> alle = getKeys();
        for (KEY k : alle) {
            ELEMENT s = gruppe.negate(getBetrag(k));
            if (gruppe.isUnit(s)) {
                ergebnis.put(k, s);
            }
        }
        return ergebnis;
    }

}