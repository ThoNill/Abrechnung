package tests;

import boundingContext.gemeinsam.DoubleB�ndelMap;

public class DoubleB�ndelFabrik {
    DoubleB�ndelMap<String> create(String prefix, double... werte) {
        DoubleB�ndelMap<String> b�ndel = new DoubleB�ndelMap<>();
        int count = 1;
        for (double wert : werte) {
            String key = prefix + Integer.toString(count);
            b�ndel.put(key, wert);
            count++;
        }
        return b�ndel;
    }

}
