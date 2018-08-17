package org.nill.abrechnung.interfaces;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import org.nill.abrechnung.aufzählungen.AbrechnungsArt;
import org.nill.abrechnung.aufzählungen.AbrechnungsStatus;
import org.nill.abrechnung.aufzählungen.AbrechnungsTyp;
import org.nill.abrechnung.aufzählungen.RunStatus;
import org.nill.allgemein.values.MonatJahr;

public interface IAbrechnung {

    void addBuchung(IBuchung buchung);

    IAbrechnung createOrGetNächsteAbrechnung(SachKontoProvider provider);

    Optional<IAbrechnung> getVorherigeAbrechnung(
            SachKontoProvider provider);

    IAbrechnung abschleißen(SachKontoProvider provider);

    void berechneDieGebühren(SachKontoProvider provider,
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