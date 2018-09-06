package org.nill.abrechnung.interfaces;

import java.util.Date;
import java.util.Optional;

import org.nill.abrechnung.aufzählungen.AbrechnungsArt;
import org.nill.abrechnung.aufzählungen.AbrechnungsStatus;
import org.nill.abrechnung.aufzählungen.AbrechnungsTyp;
import org.nill.abrechnung.aufzählungen.RunStatus;
import org.nill.allgemein.values.MonatJahr;

/**
 * Dieses Interface repräsentiert ein Abrechnung für einen {@link IMandant} für
 * eine Zeitperiode, aktuell innerhalb eines Monat, die getNummer ermöglich auch
 * feinere Abrechnungen innerhalb eines Monats.
 * 
 * @author javaman
 *
 */
public interface IAbrechnung {

    IAbrechnung createOrGetNächsteAbrechnung(Umgebung provider);

    Optional<IAbrechnung> getVorherigeAbrechnung(Umgebung provider);

    /**
     * Abschluß einer {@link IAbrechnung} am Ende des Abrechnungszeitraumes nach
     * Berechnung der Gebühren.
     * 
     * @param provider
     * @return
     */
    IAbrechnung abschließen(Umgebung provider);

    /**
     * Berechnen der Gebühren mithilfe eines {@link AbrechnungsKonfigurator} in
     * einer {@link Umgebung}
     * 
     * @param provider
     * @param konfigurator
     * @param abrechnungsArt
     */
    void berechneDieGebühren(Umgebung provider,
            AbrechnungsKonfigurator konfigurator, AbrechnungsArt abrechnungsArt);

    java.lang.Long getAbrechnungId();

    void setAbrechnungId(java.lang.Long abrechnungId);

    MonatJahr getMj();

    void setMj(MonatJahr mj);

    AbrechnungsStatus getStatus();

    void setStatus(AbrechnungsStatus status);

    IMandant getMandant();

    void setIMandant(IMandant mandant);

    AbrechnungsTyp getTyp();

    void setTyp(AbrechnungsTyp typ);

    String getBezeichnung();

    void setBezeichnung(String bezeichnung);

    int getNummer();

    void setNummer(int nummer);

    RunStatus getRunStatus();

    void setRunStatus(RunStatus runStatus);

    Date getAngelegt();

    void setAngelegt(Date angelegt);

}