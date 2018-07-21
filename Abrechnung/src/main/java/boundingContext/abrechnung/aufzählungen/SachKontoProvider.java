package boundingContext.abrechnung.aufz�hlungen;

public interface SachKontoProvider {
    SachKonto GEB�HR();

    SachKonto GUTHABEN();
    
    SachKonto AUSZUZAHLEN();

    SachKonto AUSBEZAHLT();

    SachKonto SCHULDEN();

    SachKonto ZINS();

    SachKonto MWST();

    SachKonto sachKontoFrom(int pos);
    
    default int ABGLEICH_GUTHABEN() {
        return BuchungsArt.ABGLEICH_GUTHABEN;
 //       public static final int ABGLEICH_SCHULDEN = 3;
 //       public static final int �BERNAHME_SCHULDEN = 4;
    };
    
    default int ABGLEICH_SCHULDEN() {
        return BuchungsArt.ABGLEICH_SCHULDEN;
 //       public static final int ABGLEICH_SCHULDEN = 3;
 //       public static final int �BERNAHME_SCHULDEN = 4;
    };
 
    default int �BERNAHME_SCHULDEN() {
        return BuchungsArt.�BERNAHME_SCHULDEN;
 //       public static final int ABGLEICH_SCHULDEN = 3;
 //       public static final int �BERNAHME_SCHULDEN = 4;
    };
 

}
