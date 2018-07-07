package boundingContext.abrechnung.aufz�hlungen;

public interface SachKontoProvider {
    SachKonto GEB�HR();

    SachKonto GUTHABEN();

    SachKonto AUSBEZAHLT();

    SachKonto SCHULDEN();

    SachKonto ZINS();

    SachKonto MWST();

    SachKonto sachKontoFrom(int pos);

}
