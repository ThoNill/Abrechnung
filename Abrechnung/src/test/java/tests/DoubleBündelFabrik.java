package tests;

import boundingContext.gemeinsam.DoubleBŁndelMap;

public class DoubleBŁndelFabrik {
    DoubleBŁndelMap<String> create(String prefix, double... werte) {
        DoubleBŁndelMap<String> bŁndel = new DoubleBŁndelMap<>();
        int count = 1;
        for (double wert : werte) {
            String key = prefix + Integer.toString(count);
            bŁndel.put(key, wert);
            count++;
        }
        return bŁndel;
    }

}
