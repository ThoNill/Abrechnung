package org.nill.abrechnung.interfaces;

import java.util.Date;
import java.util.Optional;

import org.nill.abrechnung.aufz�hlungen.AbrechnungsArt;
import org.nill.abrechnung.aufz�hlungen.AbrechnungsStatus;
import org.nill.abrechnung.aufz�hlungen.AbrechnungsTyp;
import org.nill.abrechnung.aufz�hlungen.RunStatus;
import org.nill.allgemein.values.MonatJahr;

/**
 * Dieses Interface repr�sentiert ein Abrechnung f�r einen {@link IMandant} f�r
 * eine Zeitperiode, aktuell innerhalb eines Monat, die getNummer erm�glich auch
 * feinere Abrechnungen innerhalb eines Monats.
 * 
 * @author javaman
 *
 */
public interface IAbrechnung {

    IAbrechnung createOrGetN�chsteAbrechnung(Umgebung provider);

    Optional<IAbrechnung> getVorherigeAbrechnung(Umgebung provider);

    /**
     * Abschlu� einer {@link IAbrechnung} am Ende des Abrechnungszeitraumes nach
     * Berechnung der Geb�hren.
     * 
     * @param provider
     * @return
     */
    IAbrechnung abschlie�en(Umgebung provider);

    /**
     * Berechnen der Geb�hren mithilfe eines {@link AbrechnungsKonfigurator} in
     * einer {@link Umgebung}
     * 
     * @param provider
     * @param konfigurator
     * @param abrechnungsArt
     */
    void berechneDieGeb�hren(Umgebung provider,
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