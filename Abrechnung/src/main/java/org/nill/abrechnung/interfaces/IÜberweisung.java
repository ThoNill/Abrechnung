package org.nill.abrechnung.interfaces;

import java.util.Date;

import javax.money.MonetaryAmount;

import org.nill.zahlungen.values.BankVerbindung;

public interface IÜberweisung {

    public long getUeberweisungsId();

    public void setUeberweisungsId(long ueberweisungsId);

    public int getBuchungsart();

    public void setBuchungsart(int buchungsart);

    public MonetaryAmount getBetrag();

    public void setBetrag(MonetaryAmount betrag);

    public Date getErstellt();

    public void setErstellt(Date erstellt);

    public BankVerbindung getVon();

    public void setVon(BankVerbindung von);

    public BankVerbindung getAn();

    public void setAn(BankVerbindung an);

    public Date getAusbezahlt();

    public void setAusbezahlt(Date ausbezahlt);

    public Date getBestätigt();

    public void setBestätigt(Date bestätigt);

    public String getVerwendungszweck();

    public void setVerwendungszweck(String verwendungszweck);

    public IAusgangsDatei getAusgangsDatei();

    public void setIAusgangsDatei(IAusgangsDatei ausgangsDatei);

    public IMandant getMandant();

    public void setIMandat(IMandant mandant);

    public IZahlungsAuftrag getZahlungsAuftrag();

    public void setIZahlungsAuftrag(IZahlungsAuftrag auftrag);

}