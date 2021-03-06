package org.nill.zahlungen.vorlagen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.StringRenderer;

public class BankExportVorlage<VORLAGEN_MODELL> {
    private STGroupFile group = null;
    private String zielVerzeichnis;
    private Charset charSet;
    private VORLAGEN_MODELL model;

    public BankExportVorlage(String dateiName, String zielVerzeichnis,
            Charset charSet, VORLAGEN_MODELL model) {
        super();
        this.zielVerzeichnis = zielVerzeichnis;
        this.charSet = charSet;
        this.model = model;
        group = new STGroupFile(dateiName + ".stg", '$', '$');
        group.registerRenderer(String.class, new StringRenderer());
    }

    public String erzeugeAusgabe() throws IOException {
        String dateiName = getZielVerzeichnis() + File.separatorChar
                + getPfadMitDateiName();
        erzeugeEventuellFehlendeVerzeichnisse(dateiName);
        Writer writer = erzeugeWriter(dateiName);
        erzeugeAusgabe(writer);
        writer.close();
        return dateiName;
    }

    private String getZielVerzeichnis() {
        return zielVerzeichnis;
    }

    private String getPfadMitDateiName() {
        return apply("dateiName");
    }

    private void erzeugeEventuellFehlendeVerzeichnisse(String dateiName)
            throws IOException {
        File f = new File(dateiName);
        Files.createDirectories(f.getParentFile().toPath());
    }

    private BufferedWriter erzeugeWriter(String dateName)
            throws FileNotFoundException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                dateName), charSet));
    }

    private void erzeugeAusgabe(Writer writer) throws IOException {
        writer.write(apply("dateiInhalt"));
        writer.flush();
    }

    private String apply(String templateName) {
        ST t = group.getInstanceOf(templateName);
        setzeSTModel(t, model);
        return t.render();
    }

    private void setzeSTModel(ST t, VORLAGEN_MODELL elem) {
        t.add("urmodell", elem);
    }

}
