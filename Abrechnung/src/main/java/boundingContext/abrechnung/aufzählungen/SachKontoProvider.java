package boundingContext.abrechnung.aufzählungen;

import ordinal.OrdinalTyp;

public interface SachKontoProvider {
        SachKonto GEBÜHR();
        SachKonto GUTHABEN();
        SachKonto SCHULDEN();
        SachKonto ZINS();
        SachKonto MWST();
        SachKonto sachKontoFrom(int pos);
        
}
