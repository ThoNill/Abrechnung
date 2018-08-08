package org.nill.zahlungen.actions;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.nill.abrechnung.entities.AusgangsDatei;
import org.nill.abrechnung.entities.Überweisung;
import org.nill.abrechnung.repositories.AusgangsDateiRepository;
import org.nill.abrechnung.repositories.ÜberweisungRepository;
import org.nill.allgemein.values.TypeReference;
import org.nill.zahlungen.values.IBAN;
import org.nill.zahlungen.vorlagen.BankExportModell;
import org.nill.zahlungen.vorlagen.BankExportVorlage;
import org.springframework.transaction.annotation.Transactional;

public class ÜberweisungsDatei {
    private AusgangsDateiRepository ausgangsDateiRepository;
    private ÜberweisungRepository überweisungsRepository;
    private String ausgangsVerzeichnis;
    private String name;
    private int fileArt;
    private TypeReference protokoll;

    public ÜberweisungsDatei(AusgangsDateiRepository ausgangsDateiRepository,
            ÜberweisungRepository überweisungsRepository,
            String ausgangsVerzeichnis, String name, int fileArt,
            TypeReference protokoll) {
        super();
        this.ausgangsDateiRepository = ausgangsDateiRepository;
        this.überweisungsRepository = überweisungsRepository;
        this.ausgangsVerzeichnis = ausgangsVerzeichnis;
        this.name = name;
        this.fileArt = fileArt;
        this.protokoll = protokoll;
    }

    @Transactional("dbATransactionManager")
    public void markiereÜberweisungsDateien(int count) {
        List<IBAN> alle = überweisungsRepository.getOffeneIBAN();
        for (IBAN iban : alle) {
            markiereÜberweisungMitAusgangsDateien(nichtÜbertrageneÜberweisungen(
                    count, iban));
        }
    }

    private List<List<Überweisung>> nichtÜbertrageneÜberweisungen(int count,
            IBAN iban) {
        List<Überweisung> alle = überweisungsRepository
                .getOffeneÜberweisungen(iban);
        return erzeugeAbschnitte(count, alle);
    }

    private List<List<Überweisung>> erzeugeAbschnitte(int count,
            List<Überweisung> alle) {
        List<List<Überweisung>> liste = new ArrayList<>();
        List<Überweisung> abschnitt = new ArrayList<>();
        int anzahl = 0;
        for (Überweisung ü : alle) {
            anzahl++;
            abschnitt.add(ü);
            if (anzahl == count) {
                liste.add(abschnitt);
                abschnitt = new ArrayList<>();
                anzahl = 0;
            }
        }
        if (anzahl > 0) {
            liste.add(abschnitt);
        }
        return liste;
    }

    private void markiereÜberweisungMitAusgangsDateien(
            List<List<Überweisung>> abschnitte) {
        for (List<Überweisung> abschnitt : abschnitte) {
            markiereÜberweisungMitAusgangsDatei(abschnitt);
        }
    }

    private void markiereÜberweisungMitAusgangsDatei(List<Überweisung> abschnitt) {
        AusgangsDatei ausgangsDatei = new AusgangsDatei();
        ausgangsDatei.setFileArt(fileArt);
        ausgangsDatei.setAngelegt(new Date());
        ausgangsDatei.setProtokoll(protokoll);
        ausgangsDatei = ausgangsDateiRepository.save(ausgangsDatei);
        for (Überweisung ü : abschnitt) {
            ü.setAusgangsDatei(ausgangsDatei);
            ü.setAusbezahlt(new Date());
            überweisungsRepository.save(ü);
        }
    }

    public void dateienMarkierenUndErstellen() throws IOException {
        List<AusgangsDatei> dateien = ausgangsDateiRepository
                .getNichVersendeteDateien(1);
        for (AusgangsDatei d : dateien) {
            markierenUndErstellen(d);
        }
    }

    @Transactional("dbATransactionManager")
    private void markierenUndErstellen(AusgangsDatei d) throws IOException {
        d.setGesendet(new Date());
        ausgangsDateiRepository.save(d);
        dateiErstellen(d);
    }

    private String dateiErstellen(AusgangsDatei d) throws IOException {
        List<Überweisung> überweisungen = überweisungsRepository
                .getÜberweisungen(d);

        BankExportModell model = new BankExportModell(d.getAusgangsDateiId(),
                name, überweisungen);

        BankExportVorlage<BankExportModell> vorlage = new BankExportVorlage<>(
                "pain.001.003.03", ausgangsVerzeichnis,
                Charset.defaultCharset(), model);
        return vorlage.erzeugeAusgabe();

    }

}