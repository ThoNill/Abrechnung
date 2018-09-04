package org.nill.abrechnung.interfaces;

import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;

/**
 * Eine {@link GebührDefinition} legt die Parameter fest, aufgrund derer eine Gebührbuchung berechnet werden soll.
 * 
 */
public interface IGebührDefinition {

    public void addMandant(IMandant mandant);

    public long getGebuehrDefinitionId();

    public void setGebuehrDefinitionId(long gebuehrDefinitionId);

    /**
     * Legt den Parameterwert fest, aufgrund der eine {@link GebührFabrik}
     * eine {@link Gebühr}} erzeugt.
     *  
     * @return
     */
    public double getParameter();

    public void setParameter(double parameter);

    /**
     * Legt die Art der Gebühr fest
     * 
     * @return
     */
    public int getArt();

    public void setArt(int art);

    /**
     * Kontonummer für das Konto {@link SachKonto} der Gebühr.
     * 
     * @return
     */
    public int getKontoNr();

    public void setKontoNr(int kontoNr);

    /**
     * Art der Daten aufgrund der ein {@link AbrechnungsKonfigurator} ein {@link GebührRepository} erzeugt.
     * 
     * @return
     */
    public int getDatenArt();

    public void setDatenArt(int datenArt);

    /**
     * Legt die Art der Gebühr fest, damit ein {@link AbrechnungsKonfigurator} eine {@link GebührFabrik}
     * erzeugen kann.
     * 
     * @return
     */
    public int getGebührArt();

    public void setGebührArt(int gebührArt);


    /**
     * Legt die BuchungsArt der Gebühr fest, damit ein {@link BuchungsAuftrag} mit 
     * dieser Art erzeugt wird.
     * 
     * @return
     */

    public int getBuchungsArt();

    public void setBuchungsArt(int buchungsArt);


    /**
     * Der Name dieser Gebühr um sie für den Benutzer zu bezeichnen.
     * 
     * @return
     */

    public String getName();

    public void setName(String name);

    /**
     * Eine Beschreibung der Gebühr
     * 
     * @return
     */
    public String getBeschreibung();

    public void setBeschreibung(String beschreibung);

    /**
     * Der Mehrwertsteuersatz der für diese Gebühr anfällt
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
     * Der Buchungstext für den {@link BuchungsAuftrag}.
     * @return
     */
    public String getBuchungstext();

    public void setBuchungstext(String buchungstext);

}