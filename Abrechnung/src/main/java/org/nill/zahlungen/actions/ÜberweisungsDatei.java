package org.nill.zahlungen.actions;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.nill.abrechnung.entities.AusgangsDatei;
import org.nill.abrechnung.entities.�berweisung;
import org.nill.abrechnung.repositories.AusgangsDateiRepository;
import org.nill.abrechnung.repositories.�berweisungRepository;
import org.nill.allgemein.values.TypeReference;
import org.nill.zahlungen.values.IBAN;
import org.nill.zahlungen.vorlagen.BankExportModell;
import org.nill.zahlungen.vorlagen.BankExportVorlage;
import org.springframework.transaction.annotation.Transactional;

public class �berweisungsDatei {
    private AusgangsDateiRepository ausgangsDateiRepository;
    private �berweisungRepository �berweisungsRepository;
    private String ausgangsVerzeichnis;
    private String name;
    private int fileArt;
    private TypeReference protokoll;

    public �berweisungsDatei(AusgangsDateiRepository ausgangsDateiRepository,
            �berweisungRepository �berweisungsRepository,
            String ausgangsVerzeichnis, String name, int fileArt,
            TypeReference protokoll) {
        super();
        this.ausgangsDateiRepository = ausgangsDateiRepository;
        this.�berweisungsRepository = �berweisungsRepository;
        this.ausgangsVerzeichnis = ausgangsVerzeichnis;
        this.name = name;
        this.fileArt = fileArt;
        this.protokoll = protokoll;
    }

    @Transactional("dbATransactionManager")
    public void markiere�berweisungsDateien(int count) {
        List<IBAN> alle = �berweisungsRepository.getOffeneIBAN();
        for (IBAN iban : alle) {
            markiere�berweisungMitAusgangsDateien(nicht�bertragene�berweisungen(
                    count, iban));
        }
    }

    private List<List<�berweisung>> nicht�bertragene�berweisungen(int count,
            IBAN iban) {
        List<�berweisung> alle = �berweisungsRepository
                .getOffene�berweisungen(iban);
        return erzeugeAbschnitte(count, alle);
    }

    private List<List<�berweisung>> erzeugeAbschnitte(int count,
            List<�berweisung> alle) {
        List<List<�berweisung>> liste = new ArrayList<>();
        List<�berweisung> abschnitt = new ArrayList<>();
        int anzahl = 0;
        for (�berweisung � : alle) {
            anzahl++;
            abschnitt.add(�);
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

    private void markiere�berweisungMitAusgangsDateien(
            List<List<�berweisung>> abschnitte) {
        for (List<�berweisung> abschnitt : abschnitte) {
            markiere�berweisungMitAusgangsDatei(abschnitt);
        }
    }

    private void markiere�berweisungMitAusgangsDatei(List<�berweisung> abschnitt) {
        AusgangsDatei ausgangsDatei = new AusgangsDatei();
        ausgangsDatei.setFileArt(fileArt);
        ausgangsDatei.setAngelegt(new Date());
        ausgangsDatei.setProtokoll(protokoll);
        ausgangsDatei = ausgangsDateiRepository.save(ausgangsDatei);
        for (�berweisung � : abschnitt) {
            �.setAusgangsDatei(ausgangsDatei);
            �.setAusbezahlt(new Date());
            �berweisungsRepository.save(�);
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
        List<�berweisung> �berweisungen = �berweisungsRepository
                .get�berweisungen(d);

        BankExportModell model = new BankExportModell(d.getAusgangsDateiId(),
                name, �berweisungen);

        BankExportVorlage<BankExportModell> vorlage = new BankExportVorlage<>(
                "pain.001.003.03", ausgangsVerzeichnis,
                Charset.defaultCharset(), model);
        return vorlage.erzeugeAusgabe();

    }

}