package boundingContext.buchhaltung;

import java.util.List;

import ddd.Entity;

public interface Buchung extends Entity<Long> {
    int getArt();

    String getText();

    void stornieren();

    Buchung getStornoBuchung();

    List<? extends KontoBewegung> getBewegungen();

    List<Entity<?>> getReferenzen();
}
