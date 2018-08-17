package org.nill.abrechnung.interfaces;

import java.util.Date;

import org.nill.allgemein.values.TypeReference;

public interface IAusgangsDatei {

    public long getAusgangsDateiId();

    public void setAusgangsDateiId(long ausgangsDateiId);

    public String getFilename();

    public void setFilename(String filename);

    public int getFileArt();

    public void setFileArt(int fileArt);

    public TypeReference getProtokoll();

    public void setProtokoll(TypeReference protokoll);

    public TypeReference getQuelle();

    public void setQuelle(TypeReference quelle);

    public Date getAngelegt();

    public void setAngelegt(Date angelegt);

    public Date getGesendet();

    public void setGesendet(Date gesendet);

}