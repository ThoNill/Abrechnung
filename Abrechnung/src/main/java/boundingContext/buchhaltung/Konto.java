package boundingContext.buchhaltung;

import ddd.Entity;

public interface Konto extends Entity<Long> {
    int getArt();

    int getNummer();

    String getBezeichnung();
}
