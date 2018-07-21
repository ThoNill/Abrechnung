package boundingContext.abrechnung.aufzählungen;

public interface SachKontoProvider {
    SachKonto GEBÜHR();

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
 //       public static final int ÜBERNAHME_SCHULDEN = 4;
    };
    
    default int ABGLEICH_SCHULDEN() {
        return BuchungsArt.ABGLEICH_SCHULDEN;
 //       public static final int ABGLEICH_SCHULDEN = 3;
 //       public static final int ÜBERNAHME_SCHULDEN = 4;
    };
 
    default int ÜBERNAHME_SCHULDEN() {
        return BuchungsArt.ÜBERNAHME_SCHULDEN;
 //       public static final int ABGLEICH_SCHULDEN = 3;
 //       public static final int ÜBERNAHME_SCHULDEN = 4;
    };
 

}
