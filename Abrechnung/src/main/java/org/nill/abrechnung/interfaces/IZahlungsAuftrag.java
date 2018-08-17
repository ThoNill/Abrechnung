package org.nill.abrechnung.interfaces;

import java.util.Date;

import javax.money.MonetaryAmount;

import org.nill.zahlungen.values.BankVerbindung;

public interface IZahlungsAuftrag {

    public void setIAbrechnung(IAbrechnung abrechnung2);

    public Long getZahlungsAuftragsId();

    public void setZahlungsAuftragsId(Long zahlungsAuftragsId);

    public int getBuchungsart();

    public void setBuchungsart(int buchungsart);

    public MonetaryAmount getBetrag();

    public void setBetrag(MonetaryAmount betrag);

    public Date getZuZahlenAm();

    public void setZuZahlenAm(Date zuZahlenAm);

    public Date getAusbezahlt();

    public void setAusbezahlt(Date ausbezahlt);

    public Date getStorniert();

    public void setStorniert(Date storniert);

    public Date getBestätigt();

    public void setBestätigt(Date bestätigt);

    public String getVerwendungszweck();

    public void setVerwendungszweck(String verwendungszweck);

    public IMandant getMandant();

    public void setIMandant(IMandant mandant);

    public IAbrechnung getAbrechnung();

    public BankVerbindung getBank();

    public void setBank(BankVerbindung bank);

}