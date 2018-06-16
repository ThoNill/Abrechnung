package tests;

import java.util.List;

import mathe.paare.Paar;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.buchhaltung.BuchhaltungsRepository;
import boundingContext.buchhaltung.Buchung;
import boundingContext.buchhaltung.Konto;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.gemeinsam.BetragsBündelMap;

public class TestBuchhaltungRepository implements
        BuchhaltungsRepository<Long, Position> {

    @Override
    public Buchung searchBuchung(Long buchungsid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Buchung> getAlleBuchungen() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void insert(Buchung buchung) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(Buchung buchung) {
        // TODO Auto-generated method stub

    }

    @Override
    public Konto searchKonto(Long kontoid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Konto searchWithKontoNummer(int kontenart, int kontonummer) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List getAlleKonten() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void insert(Konto konto) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(Konto konto) {
        // TODO Auto-generated method stub

    }

    @Override
    public BetragsBündelMap<Paar<Beschreibung, Position>> getSalden(
            Abrechnung<Long> abrechnung) {
        // TODO Auto-generated method stub
        return null;
    }

}
