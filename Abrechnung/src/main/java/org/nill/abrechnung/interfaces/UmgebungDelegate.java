package org.nill.abrechnung.interfaces;

import org.nill.abrechnung.aufz‰hlungen.SachKonto;

public class UmgebungDelegate implements Umgebung {
    private Umgebung umgebung;

    public UmgebungDelegate(Umgebung umgebung) {
        super();
        this.umgebung = umgebung;
    }

    @Override
    public SachKonto GEB‹HR() {
        return umgebung.GEB‹HR();
    }

    @Override
    public SachKonto GUTHABEN() {
        return umgebung.GUTHABEN();
    }

    @Override
    public SachKonto AUSZUZAHLEN() {
        return umgebung.AUSZUZAHLEN();
    }

    @Override
    public SachKonto AUSBEZAHLT() {
        return umgebung.AUSBEZAHLT();
    }

    @Override
    public SachKonto SCHULDEN() {
        return umgebung.SCHULDEN();
    }

    @Override
    public SachKonto ZINS() {
        return umgebung.ZINS();
    }

    @Override
    public SachKonto MWST() {
        return umgebung.MWST();
    }

    @Override
    public SachKonto sachKontoFrom(int pos) {
        return umgebung.sachKontoFrom(pos);
    }

    @Override
    public IMandantRepository getMandantRepository() {
        return umgebung.getMandantRepository();
    }

    @Override
    public IAbrechnungRepository getAbrechnungRepository() {
        return umgebung.getAbrechnungRepository();
    }

    @Override
    public IBuchungsRepository getBuchungRepository() {
        return umgebung.getBuchungRepository();
    }

    @Override
    public IZahlungsAuftragRepository getZahlungsAuftragRepository() {
        return umgebung.getZahlungsAuftragRepository();
    }

    @Override
    public I‹berweisungRepository get‹berweisungRepository() {
        return umgebung.get‹berweisungRepository();
    }

    @Override
    public IParameterRepository getParameterRepository() {
        return umgebung.getParameterRepository();
    }

    @Override
    public IAusgangsDateiRepository getAusgangsDateiRepository() {
        return umgebung.getAusgangsDateiRepository();
    }

    @Override
    public IBuchung createBuchung() {
       return umgebung.createBuchung();
    }

    @Override
    public IAusgangsDatei createAusgangsDatei() {
        return umgebung.createAusgangsDatei();
    }

    @Override
    public IZahlungsAuftrag createZahlungsAuftrag() {
      return umgebung.createZahlungsAuftrag();
    }

    @Override
    public I‹berweisung create‹berweisung() {
        return umgebung.create‹berweisung();
    }

}
