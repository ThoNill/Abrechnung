package boundingContext.abrechnung.aufzählungen;

public interface SachKontoProvider {
    SachKonto GEBÜHR();

    SachKonto GUTHABEN();

    SachKonto AUSBEZAHLT();

    SachKonto SCHULDEN();

    SachKonto ZINS();

    SachKonto MWST();

    SachKonto sachKontoFrom(int pos);

}
