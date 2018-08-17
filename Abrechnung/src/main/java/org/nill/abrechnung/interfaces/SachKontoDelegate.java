package org.nill.abrechnung.interfaces;

import org.nill.abrechnung.aufz‰hlungen.SachKonto;

public class SachKontoDelegate implements SachKontoProvider {
    private SachKontoProvider sachKontoProvider;

    public SachKontoDelegate(SachKontoProvider sachKontoProvider) {
        super();
        this.sachKontoProvider = sachKontoProvider;
    }

    @Override
    public SachKonto GEB‹HR() {
        return sachKontoProvider.GEB‹HR();
    }

    @Override
    public SachKonto GUTHABEN() {
        return sachKontoProvider.GUTHABEN();
    }

    @Override
    public SachKonto AUSZUZAHLEN() {
        return sachKontoProvider.AUSZUZAHLEN();
    }

    @Override
    public SachKonto AUSBEZAHLT() {
        return sachKontoProvider.AUSBEZAHLT();
    }

    @Override
    public SachKonto SCHULDEN() {
        return sachKontoProvider.SCHULDEN();
    }

    @Override
    public SachKonto ZINS() {
        return sachKontoProvider.ZINS();
    }

    @Override
    public SachKonto MWST() {
        return sachKontoProvider.MWST();
    }

    @Override
    public SachKonto sachKontoFrom(int pos) {
        return sachKontoProvider.sachKontoFrom(pos);
    }

    @Override
    public IMandantRepository getMandantRepository() {
        return sachKontoProvider.getMandantRepository();
    }

    @Override
    public IAbrechnungRepository getAbrechnungRepository() {
        return sachKontoProvider.getAbrechnungRepository();
    }

    @Override
    public IBuchungsRepository getBuchungRepository() {
        return sachKontoProvider.getBuchungRepository();
    }

    @Override
    public IZahlungsAuftragRepository getZahlungsAuftragRepository() {
        return sachKontoProvider.getZahlungsAuftragRepository();
    }

    @Override
    public I‹berweisungRepository get‹berweisungRepository() {
        return sachKontoProvider.get‹berweisungRepository();
    }

    @Override
    public IParameterRepository getParameterRepository() {
        return sachKontoProvider.getParameterRepository();
    }

    @Override
    public IAusgangsDateiRepository getAusgangsDateiRepository() {
        return sachKontoProvider.getAusgangsDateiRepository();
    }

    @Override
    public IBuchung createBuchung() {
       return sachKontoProvider.createBuchung();
    }

    @Override
    public IAusgangsDatei createAusgangsDatei() {
        return sachKontoProvider.createAusgangsDatei();
    }

    @Override
    public IZahlungsAuftrag createZahlungsAuftrag() {
      return sachKontoProvider.createZahlungsAuftrag();
    }

    @Override
    public I‹berweisung create‹berweisung() {
        return sachKontoProvider.create‹berweisung();
    }

}
