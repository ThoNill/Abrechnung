package boundingContext.zahlungen.vorlagen;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.money.MonetaryAmount;

import betrag.Geld;
import boundingContext.abrechnung.entities.Überweisung;
import boundingContext.zahlungen.values.BankVerbindung;

public class STModel {
    private int anzahl;
    private long referenz;
    private String name;
    private List<Überweisung> überweisungen;
    private MonetaryAmount summe;
    
  

    public STModel(long referenz,String name, List<Überweisung> überweisungen) {
        super();
        this.anzahl = überweisungen.size();
        this.referenz = referenz;
        this.überweisungen = überweisungen;
        this.summe = berechneSumme(überweisungen);
        this.name = name;
    }

    private MonetaryAmount berechneSumme(List<Überweisung> überweisungen) {
       MonetaryAmount summe = Geld.getNull();
       for(Überweisung ü : überweisungen) {
           summe = summe.add(ü.getBetrag());
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

    public List<Überweisung> getUeberweisungen() {
        return überweisungen;
    }

    public MonetaryAmount getSumme() {
        return summe;
    }

    public BankVerbindung getVon() {
        return überweisungen.get(0).getVon();
    }
    
    public String getAuszahlungsDatum() {
        Date d = überweisungen.get(0).getAusbezahlt();
        // 2010-11-25
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(d);
   
    }


    public String getName() {
        return name;
    }


}
