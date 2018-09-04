package org.nill.zahlungen.actions;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.nill.abrechnung.interfaces.IAusgangsDatei;
import org.nill.abrechnung.interfaces.I�berweisung;
import org.nill.abrechnung.interfaces.Umgebung;
import org.nill.abrechnung.interfaces.UmgebungDelegate;
import org.nill.allgemein.values.TypeReference;
import org.nill.zahlungen.values.IBAN;
import org.nill.zahlungen.vorlagen.BankExportModell;
import org.nill.zahlungen.vorlagen.BankExportVorlage;
import org.springframework.transaction.annotation.Transactional;

/**
 * Erzeugt eine {@link IAusgangsDatei} im Sepa-Format zum �bertragen an die Bank.
 * 
 * @author javaman
 *
 */
public class �berweisungsDatei extends UmgebungDelegate{
    private String ausgangsVerzeichnis;
    private String name;
    private int fileArt;
    private TypeReference protokoll;

    public �berweisungsDatei(Umgebung umgebung,
            String ausgangsVerzeichnis, String name, int fileArt,
            TypeReference protokoll) {
        super(umgebung);
        this.ausgangsVerzeichnis = ausgangsVerzeichnis;
        this.name = name;
        this.fileArt = fileArt;
        this.protokoll = protokoll;
    }

    @Transactional("dbATransactionManager")
    public void markiere�berweisungsDateien(int count) {
        List<IBAN> alle = get�berweisungRepository().getOffeneIBAN();
        for (IBAN iban : alle) {
            markiere�berweisungMitAusgangsDateien(nicht�bertragene�berweisungen(
                    count, iban));
        }
    }

    private List<List<I�berweisung>> nicht�bertragene�berweisungen(int count,
            IBAN iban) {
        List<I�berweisung> alle = get�berweisungRepository()
                .getOffene�berweisungen(iban);
        return erzeugeAbschnitte(count, alle);
    }

    private List<List<I�berweisung>> erzeugeAbschnitte(int count,
            List<I�berweisung> alle) {
        List<List<I�berweisung>> liste = new ArrayList<>();
        List<I�berweisung> abschnitt = new ArrayList<>();
        int anzahl = 0;
        for (I�berweisung � : alle) {
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
            List<List<I�berweisung>> abschnitte) {
        for (List<I�berweisung> abschnitt : abschnitte) {
            markiere�berweisungMitAusgangsDatei(abschnitt);
        }
    }

    private void markiere�berweisungMitAusgangsDatei(List<I�berweisung> abschnitt) {
        IAusgangsDatei ausgangsDatei = createAusgangsDatei();
        ausgangsDatei.setFileArt(fileArt);
        ausgangsDatei.setAngelegt(new Date());
        ausgangsDatei.setProtokoll(protokoll);
        ausgangsDatei = getAusgangsDateiRepository().save(ausgangsDatei);
        for (I�berweisung � : abschnitt) {
            �.setIAusgangsDatei(ausgangsDatei);
            �.setAusbezahlt(new Date());
            get�berweisungRepository().save(�);
        }
    }

    public void dateienMarkierenUndErstellen() throws IOException {
        List<IAusgangsDatei> dateien = getAusgangsDateiRepository()
                .getNichVersendeteDateien(1);
        for (IAusgangsDatei d : dateien) {
            markierenUndErstellen(d);
        }
    }

    @Transactional("dbATransactionManager")
    private void markierenUndErstellen(IAusgangsDatei d) throws IOException {
        d.setGesendet(new Date());
        getAusgangsDateiRepository().save(d);
        dateiErstellen(d);
    }

    private String dateiErstellen(IAusgangsDatei d) throws IOException {
        List<I�berweisung> �berweisungen = get�berweisungRepository()
                .get�berweisungen(d);

        BankExportModell model = new BankExportModell(d.getAusgangsDateiId(),
                name, �berweisungen);

        BankExportVorlage<BankExportModell> vorlage = new BankExportVorlage<>(
                "pain.001.003.03", ausgangsVerzeichnis,
                Charset.defaultCharset(), model);
        return vorlage.erzeugeAusgabe();

    }

}