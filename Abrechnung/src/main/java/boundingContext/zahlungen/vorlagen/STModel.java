package boundingContext.zahlungen.vorlagen;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.money.MonetaryAmount;

import betrag.Geld;
import boundingContext.abrechnung.entities.�berweisung;
import boundingContext.zahlungen.values.BankVerbindung;

public class STModel {
    private int anzahl;
    private long referenz;
    private String name;
    private List<�berweisung> �berweisungen;
    private MonetaryAmount summe;
    
  

    public STModel(long referenz,String name, List<�berweisung> �berweisungen) {
        super();
        this.anzahl = �berweisungen.size();
        this.referenz = referenz;
        this.�berweisungen = �berweisungen;
        this.summe = berechneSumme(�berweisungen);
        this.name = name;
    }

    private MonetaryAmount berechneSumme(List<�berweisung> �berweisungen) {
       MonetaryAmount summe = Geld.getNull();
       for(�berweisung � : �berweisungen) {
           summe = summe.add(�.getBetrag());
       }
       return summe;
    }
    
    
    public String getAktuelleZeit() {
        Date jetzt = new Date();
        // 2010-11-11T09:30:47.000Z
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return format.format(jetzt);
    }

    public int getAnzahl() {
        return anzahl;
    }

    public long getReferenz() {
        return referenz;
    }

    public List<�berweisung> getUeberweisungen() {
        return �berweisungen;
    }

    public MonetaryAmount getSumme() {
        return summe;
    }

    public BankVerbindung getVon() {
        return �berweisungen.get(0).getVon();
    }
    
    public String getAuszahlungsDatum() {
        Date d = �berweisungen.get(0).getAusbezahlt();
        // 2010-11-25
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(d);
   
    }


    public String getName() {
        return name;
    }


}
