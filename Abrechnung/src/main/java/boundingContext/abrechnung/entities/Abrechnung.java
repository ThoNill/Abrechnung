package boundingContext.abrechnung.entities;

import java.util.Date;

import ddd.Entity;

public interface Abrechnung<ID> extends Entity<ID> {
    String getBezeichnung();

    int getNummer();

    AbrechnungsTyp getTyp();

    void setTyp(AbrechnungsTyp typ);

    AbrechnungsStatus getStatus();

    void setStatus(AbrechnungsStatus status);

    RunStatus getRunStatus();

    void setRunStatus(RunStatus runStatus);

    Zeitraum getZeitraum();

    Date getAngelegt();

    Mandant<ID> getMandant();

}
