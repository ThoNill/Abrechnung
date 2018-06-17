package boundingContext.abrechnung.aufz�hlungen;

import ordinal.OrdinalTyp;

public interface SachKontoProvider {
        SachKonto GEB�HR();
        SachKonto GUTHABEN();
        SachKonto SCHULDEN();
        SachKonto ZINS();
        SachKonto MWST();
        SachKonto sachKontoFrom(int pos);
        
}
