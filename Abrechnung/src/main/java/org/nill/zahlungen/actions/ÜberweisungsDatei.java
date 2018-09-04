package org.nill.zahlungen.actions;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.nill.abrechnung.interfaces.IAusgangsDatei;
import org.nill.abrechnung.interfaces.IÜberweisung;
import org.nill.abrechnung.interfaces.Umgebung;
import org.nill.abrechnung.interfaces.UmgebungDelegate;
import org.nill.allgemein.values.TypeReference;
import org.nill.zahlungen.values.IBAN;
import org.nill.zahlungen.vorlagen.BankExportModell;
import org.nill.zahlungen.vorlagen.BankExportVorlage;
import org.springframework.transaction.annotation.Transactional;

/**
 * Erzeugt eine {@link IAusgangsDatei} im Sepa-Format zum Übertragen an die Bank.
 * 
 * @author javaman
 *
 */
public class ÜberweisungsDatei extends UmgebungDelegate{
    private String ausgangsVerzeichnis;
    private String name;
    private int fileArt;
    private TypeReference protokoll;

    public ÜberweisungsDatei(Umgebung umgebung,
            String ausgangsVerzeichnis, String name, int fileArt,
            TypeReference protokoll) {
        super(umgebung);
        this.ausgangsVerzeichnis = ausgangsVerzeichnis;
        this.name = name;
        this.fileArt = fileArt;
        this.protokoll = protokoll;
    }

    @Transactional("dbATransactionManager")
    public void markiereÜberweisungsDateien(int count) {
        List<IBAN> alle = getÜberweisungRepository().getOffeneIBAN();
        for (IBAN iban : alle) {
            markiereÜberweisungMitAusgangsDateien(nichtÜbertrageneÜberweisungen(
                    count, iban));
        }
    }

    private List<List<IÜberweisung>> nichtÜbertrageneÜberweisungen(int count,
            IBAN iban) {
        List<IÜberweisung> alle = getÜberweisungRepository()
                .getOffeneÜberweisungen(iban);
        return erzeugeAbschnitte(count, alle);
    }

    private List<List<IÜberweisung>> erzeugeAbschnitte(int count,
            List<IÜberweisung> alle) {
        List<List<IÜberweisung>> liste = new ArrayList<>();
        List<IÜberweisung> abschnitt = new ArrayList<>();
        int anzahl = 0;
        for (IÜberweisung ü : alle) {
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
            List<List<IÜberweisung>> abschnitte) {
        for (List<IÜberweisung> abschnitt : abschnitte) {
            markiereÜberweisungMitAusgangsDatei(abschnitt);
        }
    }

    private void markiereÜberweisungMitAusgangsDatei(List<IÜberweisung> abschnitt) {
        IAusgangsDatei ausgangsDatei = createAusgangsDatei();
        ausgangsDatei.setFileArt(fileArt);
        ausgangsDatei.setAngelegt(new Date());
        ausgangsDatei.setProtokoll(protokoll);
        ausgangsDatei = getAusgangsDateiRepository().save(ausgangsDatei);
        for (IÜberweisung ü : abschnitt) {
            ü.setIAusgangsDatei(ausgangsDatei);
            ü.setAusbezahlt(new Date());
            getÜberweisungRepository().save(ü);
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
        List<IÜberweisung> überweisungen = getÜberweisungRepository()
                .getÜberweisungen(d);

        BankExportModell model = new BankExportModell(d.getAusgangsDateiId(),
                name, überweisungen);

        BankExportVorlage<BankExportModell> vorlage = new BankExportVorlage<>(
                "pain.001.003.03", ausgangsVerzeichnis,
                Charset.defaultCharset(), model);
        return vorlage.erzeugeAusgabe();

    }

}