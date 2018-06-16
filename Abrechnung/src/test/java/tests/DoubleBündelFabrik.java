package tests;

import boundingContext.gemeinsam.DoubleBündelMap;

public class DoubleBündelFabrik {
    DoubleBündelMap<String> create(String prefix, double... werte) {
        DoubleBündelMap<String> bündel = new DoubleBündelMap<>();
        int count = 1;
        for (double wert : werte) {
            String key = prefix + Integer.toString(count);
            bündel.put(key, wert);
            count++;
        }
        return bündel;
    }

}
