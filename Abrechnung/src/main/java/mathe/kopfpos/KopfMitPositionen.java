package mathe.kopfpos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mathe.bündel.Bündel;
import mathe.bündel.BündelMap;
import mathe.paare.Paar;

public class KopfMitPositionen<K, KEY, VALUE, BÜNDELTYP extends Bündel<KEY, VALUE>> {
    private K kopf;
    private BÜNDELTYP positionen;

    public KopfMitPositionen(K kopf, BÜNDELTYP positionen) {
        super();
        this.kopf = kopf;
        this.positionen = positionen;
    }

    public Bündel<Paar<K, KEY>, VALUE> einKopfMitPositionenInEinPaarBündelumwandeln(
            KopfMitPositionen<K, KEY, VALUE, BÜNDELTYP> kp) {
        BündelMap<Paar<K, KEY>, VALUE> bündel = new BündelMap<>();
        for (KEY k : kp.positionen.getKeys()) {
            Paar<K, KEY> p = new Paar(kp.kopf, k);
            bündel.put(p, kp.positionen.getValue(k));
        }
        return bündel;
    }

    public Bündel<Paar<K, KEY>, VALUE> mehrereKopfMitPositionenInEinPaarBündelumwandeln(
            List<KopfMitPositionen<K, KEY, VALUE, BÜNDELTYP>> positionen) {
        BündelMap<Paar<K, KEY>, VALUE> bündel = new BündelMap<>();
        inEinPaarBündelFüllen(positionen, bündel);
        return bündel;
    }

    public void inEinPaarBündelFüllen(
            List<KopfMitPositionen<K, KEY, VALUE, BÜNDELTYP>> positionen,
            BündelMap<Paar<K, KEY>, VALUE> bündel) {
        for (KopfMitPositionen<K, KEY, VALUE, BÜNDELTYP> kp : positionen) {
            bündel.erweitern(einKopfMitPositionenInEinPaarBündelumwandeln(kp));
        }
    }

    public KopfMitPositionen<K, KEY, VALUE, BündelMap<KEY, VALUE>> ausEinemPaarBündelEinBündelExtrahieren(
            Bündel<Paar<K, KEY>, VALUE> pBündel, K kopf) {
        BündelMap<KEY, VALUE> kBündel = new BündelMap<>();
        for (Paar<K, KEY> p : pBündel.getKeys()) {
            KEY k = p.getR();
            if (kopf.equals(k)) {
                kBündel.put(k, pBündel.getValue(p));
            }
        }
        return new KopfMitPositionen<>(
                kopf, kBündel);
    }

    public Set<K> ausEinemPaarBündelDieKöpfeExtrahieren(
            Bündel<Paar<K, KEY>, VALUE> pBündel) {
        Set<K> köpfe = new HashSet<>();
        for (Paar<K, KEY> p : pBündel.getKeys()) {
            K k = p.getL();
            köpfe.add(k);
        }
        return köpfe;
    }

    public List<KopfMitPositionen<K, KEY, VALUE, BündelMap<KEY, VALUE>>> einPaarBündelInEineListeVonKöpfenMitPositionenUmwandeln(
            Bündel<Paar<K, KEY>, VALUE> pBündel) {
        Set<K> köpfe = ausEinemPaarBündelDieKöpfeExtrahieren(pBündel);
        List<KopfMitPositionen<K, KEY, VALUE, BündelMap<KEY, VALUE>>> liste = new ArrayList<>();
        for (K k : köpfe) {
            liste.add(ausEinemPaarBündelEinBündelExtrahieren(pBündel, k));
        }
        return liste;
    }

    public K getKopf() {
        return kopf;
    }

    public BÜNDELTYP getPositionen() {
        return positionen;
    }
}
