package org.nill.allgemein.values;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class MonatJahr implements Comparable<MonatJahr> {
    private int mj;

    public MonatJahr(int mj) {
        super();
        this.mj = mj;
    }

    public MonatJahr(int monat, int jahr) {
        super();
        this.mj = 12 * jahr + (monat - 1);
    }

    public int getMonat() {
        return (mj % 12) + 1;
    }

    public int getJahr() {
        return (mj / 12);
    }

    public int getMJ() {
        return mj;
    }

    @Override
    public int compareTo(MonatJahr o) {
        return mj - o.mj;
    }

}
