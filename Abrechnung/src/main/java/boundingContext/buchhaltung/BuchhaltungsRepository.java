package boundingContext.buchhaltung;

import java.util.List;

import mathe.paare.Paar;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.gemeinsam.BetragsBündelMap;
import ddd.Repository;

public interface BuchhaltungsRepository<ID, KEY> extends Repository {
    Buchung searchBuchung(ID buchungsid);

    List<Buchung> getAlleBuchungen();

    void insert(Buchung buchung);

    void update(Buchung buchung);

    Konto searchKonto(ID kontoid);

    Konto searchWithKontoNummer(int kontenart, int kontonummer);

    List<Konto> getAlleKonten();

    void insert(Konto konto);

    void update(Konto konto);

    BetragsBündelMap<Paar<Beschreibung, KEY>> getSalden(
            Abrechnung<ID> abrechnung);

}
