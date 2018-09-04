package org.nill.abrechnung.interfaces;

import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;

/**
 * Eine {@link Geb�hrDefinition} legt die Parameter fest, aufgrund derer eine Geb�hrbuchung berechnet werden soll.
 * 
 */
public interface IGeb�hrDefinition {

    public void addMandant(IMandant mandant);

    public long getGebuehrDefinitionId();

    public void setGebuehrDefinitionId(long gebuehrDefinitionId);

    /**
     * Legt den Parameterwert fest, aufgrund der eine {@link Geb�hrFabrik}
     * eine {@link Geb�hr}} erzeugt.
     *  
     * @return
     */
    public double getParameter();

    public void setParameter(double parameter);

    /**
     * Legt die Art der Geb�hr fest
     * 
     * @return
     */
    public int getArt();

    public void setArt(int art);

    /**
     * Kontonummer f�r das Konto {@link SachKonto} der Geb�hr.
     * 
     * @return
     */
    public int getKontoNr();

    public void setKontoNr(int kontoNr);

    /**
     * Art der Daten aufgrund der ein {@link AbrechnungsKonfigurator} ein {@link Geb�hrRepository} erzeugt.
     * 
     * @return
     */
    public int getDatenArt();

    public void setDatenArt(int datenArt);

    /**
     * Legt die Art der Geb�hr fest, damit ein {@link AbrechnungsKonfigurator} eine {@link Geb�hrFabrik}
     * erzeugen kann.
     * 
     * @return
     */
    public int getGeb�hrArt();

    public void setGeb�hrArt(int geb�hrArt);


    /**
     * Legt die BuchungsArt der Geb�hr fest, damit ein {@link BuchungsAuftrag} mit 
     * dieser Art erzeugt wird.
     * 
     * @return
     */

    public int getBuchungsArt();

    public void setBuchungsArt(int buchungsArt);


    /**
     * Der Name dieser Geb�hr um sie f�r den Benutzer zu bezeichnen.
     * 
     * @return
     */

    public String getName();

    public void setName(String name);

    /**
     * Eine Beschreibung der Geb�hr
     * 
     * @return
     */
    public String getBeschreibung();

    public void setBeschreibung(String beschreibung);

    /**
     * Der Mehrwertsteuersatz der f�r diese Geb�hr anf�llt
     * 
     * @return
     */
    public double getMwstSatz();

    public void setMwstSatz(double mwstSatz);

    /**
     * Die Kontonummer der Mehrwertsteuerkontos
     * @return
     */
    public int getMwstKonto();

    public void setMwstKonto(int mwstKonto);

    /**
     * Der Buchungstext f�r den {@link BuchungsAuftrag}.
     * @return
     */
    public String getBuchungstext();

    public void setBuchungstext(String buchungstext);

}