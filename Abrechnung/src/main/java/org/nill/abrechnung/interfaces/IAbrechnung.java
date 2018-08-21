package org.nill.abrechnung.interfaces;

import java.util.Date;
import java.util.Optional;

import org.nill.abrechnung.aufzählungen.AbrechnungsArt;
import org.nill.abrechnung.aufzählungen.AbrechnungsStatus;
import org.nill.abrechnung.aufzählungen.AbrechnungsTyp;
import org.nill.abrechnung.aufzählungen.RunStatus;
import org.nill.allgemein.values.MonatJahr;

public interface IAbrechnung {

    IAbrechnung createOrGetNächsteAbrechnung(Umgebung provider);

    Optional<IAbrechnung> getVorherigeAbrechnung(
            Umgebung provider);

    IAbrechnung abschleißen(Umgebung provider);

    void berechneDieGebühren(Umgebung provider,
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

}