package org.nill.abrechnung.interfaces;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import org.nill.abrechnung.aufz�hlungen.AbrechnungsArt;
import org.nill.abrechnung.aufz�hlungen.AbrechnungsStatus;
import org.nill.abrechnung.aufz�hlungen.AbrechnungsTyp;
import org.nill.abrechnung.aufz�hlungen.RunStatus;
import org.nill.allgemein.values.MonatJahr;

public interface IAbrechnung {

    void addBuchung(IBuchung buchung);

    IAbrechnung createOrGetN�chsteAbrechnung(SachKontoProvider provider);

    Optional<IAbrechnung> getVorherigeAbrechnung(
            SachKontoProvider provider);

    IAbrechnung abschlei�en(SachKontoProvider provider);

    void berechneDieGeb�hren(SachKontoProvider provider,
            AbrechnungsKonfigurator konfigurator, AbrechnungsArt abrechnungsArt);

    

    java.lang.Long getAbrechnungId();

    void setAbrechnungId(java.lang.Long abrechnungId);

    MonatJahr getMj();

    void setMj(MonatJahr mj) ;

    AbrechnungsStatus getStatus();

    void setStatus(AbrechnungsStatus status);

    IMandant getMandant() ;
    void setIMandant(IMandant mandant);

    AbrechnungsTyp getTyp() ;

    void setTyp(AbrechnungsTyp typ);

    String getBezeichnung();
    void setBezeichnung(String bezeichnung);

    int getNummer();
    void setNummer(int nummer);

    RunStatus getRunStatus() ;

    void setRunStatus(RunStatus runStatus);

    Date getAngelegt();

    void setAngelegt(Date angelegt) ;

    Set<? extends IBuchung> getBuchung() ;

}