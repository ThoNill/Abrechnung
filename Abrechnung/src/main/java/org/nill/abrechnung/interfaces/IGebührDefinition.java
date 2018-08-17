package org.nill.abrechnung.interfaces;


public interface IGebührDefinition {

    public void addMandant(IMandant mandant);

    public long getGebuehrDefinitionId();

    public void setGebuehrDefinitionId(long gebuehrDefinitionId);

    public double getParameter();

    public void setParameter(double parameter);

    public int getArt();

    public void setArt(int art);

    public int getKontoNr();

    public void setKontoNr(int kontoNr);

    public int getDatenArt();

    public void setDatenArt(int datenArt);

    public int getGebührArt();

    public void setGebührArt(int gebührArt);

    public int getBuchungsArt();

    public void setBuchungsArt(int buchungsArt);

    public String getName();

    public void setName(String name);

    public String getBeschreibung();

    public void setBeschreibung(String beschreibung);

    public double getMwstSatz();

    public void setMwstSatz(double mwstSatz);

    public int getMwstKonto();

    public void setMwstKonto(int mwstKonto);

    public String getBuchungstext();

    public void setBuchungstext(String buchungstext);

}