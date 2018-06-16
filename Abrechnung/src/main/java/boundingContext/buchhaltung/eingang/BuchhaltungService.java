package boundingContext.buchhaltung.eingang;

import java.util.Optional;

import javax.money.MonetaryAmount;

import mathe.b�ndel.GruppenB�ndel;
import mathe.paare.Paar;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.AbrechnungsRepository;
import boundingContext.buchhaltung.BuchhaltungsRepository;
import boundingContext.gemeinsam.BetragsB�ndelMap;
import ddd.Service;

public class BuchhaltungService<ID, KEY> implements Service {
    private BuchhaltungsRepository buchhaltungsRepository;
    private AbrechnungsRepository<ID> abrechnungsRepository;

    public BuchhaltungService(BuchhaltungsRepository buchhaltungsRepository) {
        super();
        this.buchhaltungsRepository = buchhaltungsRepository;
    }

    public void einbuchen(BuchungsAuftr�ge<ID, KEY> auftr�ge) {
        Abrechnung<ID> abrechnung = (Abrechnung<ID>) auftr�ge.getBeteiligte()
                .getValue("Abrechung");

        BetragsB�ndelMap<Paar<Beschreibung, KEY>> neueSalden = new BetragsB�ndelMap<>(
                auftr�ge.inEinPaarB�ndelumwandeln());
        BetragsB�ndelMap<Paar<Beschreibung, KEY>> salden = buchhaltungsRepository
                .getSalden(abrechnung);

        GruppenB�ndel<Paar<Beschreibung, KEY>, MonetaryAmount> differenz = neueSalden
                .subtract(salden, neueSalden);
        // begrenzte Differenz;

        GruppenB�ndel<Paar<Beschreibung, KEY>, MonetaryAmount> neuerKontostand = salden
                .erweitern(neueSalden);
        MonetaryAmount saldo = neuerKontostand.getSumme();

        if (saldo.isNegative()) {
            �berzahlung(saldo, neuerKontostand);
            �berzahlungInDieN�chsteAbrechnung(saldo,
                    abrechnungsRepository.getFolgendeAbrechnung(abrechnung));
        } else {
            erzeugeRestzahlungen(saldo, neuerKontostand);
        }

        // berechne.erweitern(konvertiert);

        // salden.subtract(salden, b);
    }

    private void �berzahlungInDieN�chsteAbrechnung(MonetaryAmount saldo,
            Optional<Abrechnung<ID>> folgendeAbrechnung) {
        // TODO Auto-generated method stub

    }

    private void erzeugeRestzahlungen(
            MonetaryAmount saldo,
            GruppenB�ndel<Paar<Beschreibung, KEY>, MonetaryAmount> neuerKontostand) {
        // TODO Auto-generated method stub

    }

    private void �berzahlung(
            MonetaryAmount saldo,
            GruppenB�ndel<Paar<Beschreibung, KEY>, MonetaryAmount> neuerKontostand) {
        // TODO Auto-generated method stub

    };
}
