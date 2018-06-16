package mathe.kopfpos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mathe.b�ndel.B�ndel;
import mathe.b�ndel.B�ndelMap;
import mathe.paare.Paar;

public class KopfMitPositionen<K, KEY, VALUE, B�NDELTYP extends B�ndel<KEY, VALUE>> {
    private K kopf;
    private B�NDELTYP positionen;

    public KopfMitPositionen(K kopf, B�NDELTYP positionen) {
        super();
        this.kopf = kopf;
        this.positionen = positionen;
    }

    public B�ndel<Paar<K, KEY>, VALUE> einKopfMitPositionenInEinPaarB�ndelumwandeln(
            KopfMitPositionen<K, KEY, VALUE, B�NDELTYP> kp) {
        B�ndel<KEY, VALUE> positionen = kp.positionen;
        K kopf = kp.kopf;
        B�ndelMap<Paar<K, KEY>, VALUE> b�ndel = new B�ndelMap<>();
        for (KEY k : positionen.getKeys()) {
            Paar<K, KEY> p = new Paar(kopf, k);
            b�ndel.put(p, positionen.getValue(k));
        }
        return b�ndel;
    }

    public B�ndel<Paar<K, KEY>, VALUE> mehrereKopfMitPositionenInEinPaarB�ndelumwandeln(
            List<KopfMitPositionen<K, KEY, VALUE, B�NDELTYP>> positionen) {
        B�ndelMap<Paar<K, KEY>, VALUE> b�ndel = new B�ndelMap<>();
        inEinPaarB�ndelF�llen(positionen, b�ndel);
        return b�ndel;
    }

    public void inEinPaarB�ndelF�llen(
            List<KopfMitPositionen<K, KEY, VALUE, B�NDELTYP>> positionen,
            B�ndelMap<Paar<K, KEY>, VALUE> b�ndel) {
        for (KopfMitPositionen<K, KEY, VALUE, B�NDELTYP> kp : positionen) {
            b�ndel.erweitern(einKopfMitPositionenInEinPaarB�ndelumwandeln(kp));
        }
    }

    public KopfMitPositionen<K, KEY, VALUE, B�ndelMap<KEY, VALUE>> ausEinemPaarB�ndelEinB�ndelExtrahieren(
            B�ndel<Paar<K, KEY>, VALUE> pB�ndel, K kopf) {
        B�ndelMap<KEY, VALUE> kB�ndel = new B�ndelMap<>();
        for (Paar<K, KEY> p : pB�ndel.getKeys()) {
            KEY k = p.getR();
            if (kopf.equals(k)) {
                kB�ndel.put(k, pB�ndel.getValue(p));
            }
        }
        return new KopfMitPositionen<K, KEY, VALUE, B�ndelMap<KEY, VALUE>>(
                kopf, kB�ndel);
    }

    public Set<K> ausEinemPaarB�ndelDieK�pfeExtrahieren(
            B�ndel<Paar<K, KEY>, VALUE> pB�ndel) {
        Set<K> k�pfe = new HashSet<>();
        for (Paar<K, KEY> p : pB�ndel.getKeys()) {
            K k = p.getL();
            k�pfe.add(k);
        }
        return k�pfe;
    }

    public List<KopfMitPositionen<K, KEY, VALUE, B�ndelMap<KEY, VALUE>>> einPaarB�ndelInEineListeVonK�pfenMitPositionenUmwandeln(
            B�ndel<Paar<K, KEY>, VALUE> pB�ndel) {
        Set<K> k�pfe = ausEinemPaarB�ndelDieK�pfeExtrahieren(pB�ndel);
        List<KopfMitPositionen<K, KEY, VALUE, B�ndelMap<KEY, VALUE>>> liste = new ArrayList<KopfMitPositionen<K, KEY, VALUE, B�ndelMap<KEY, VALUE>>>();
        for (K kopf : k�pfe) {
            liste.add(ausEinemPaarB�ndelEinB�ndelExtrahieren(pB�ndel, kopf));
        }
        return liste;
    }

    public K getKopf() {
        return kopf;
    }

    public B�NDELTYP getPositionen() {
        return positionen;
    }
}
