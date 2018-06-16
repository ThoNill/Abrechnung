package boundingContext.buchhaltung.eingang;

import java.util.ArrayList;
import java.util.List;

import javax.money.MonetaryAmount;

import mathe.b�ndel.B�ndel;
import mathe.b�ndel.B�ndelMap;
import mathe.kopfpos.KopfMitPositionen;
import mathe.paare.Paar;
import boundingContext.gemeinsam.BetragsB�ndel;
import ddd.Entity;
import ddd.Value;

public class BuchungsAuftr�ge<ID, KEY> extends ArrayList<BuchungsAuftrag<KEY>>
        implements Value {
    private B�ndelMap<String, Entity<ID>> beteiligte;

    public BuchungsAuftr�ge(B�ndelMap<String, Entity<ID>> beteiligte) {
        super();
        this.beteiligte = beteiligte;
    }

    public B�ndelMap<String, Entity<ID>> getBeteiligte() {
        return beteiligte;
    }

    public B�ndel<Paar<Beschreibung, KEY>, MonetaryAmount> inEinPaarB�ndelumwandeln() {
        List<KopfMitPositionen<Beschreibung, KEY, MonetaryAmount, BetragsB�ndel<KEY>>> nPositionen = new ArrayList<>();
        for (BuchungsAuftrag<KEY> auftrag : this) {
            nPositionen.add(auftrag);
        }
        BuchungsAuftrag<KEY> dummy = get(0);
        return dummy
                .mehrereKopfMitPositionenInEinPaarB�ndelumwandeln(nPositionen);
    }

    public void inEinPaarB�ndelF�llen(
            B�ndelMap<Paar<Beschreibung, KEY>, MonetaryAmount> b�ndel) {
        List<KopfMitPositionen<Beschreibung, KEY, MonetaryAmount, BetragsB�ndel<KEY>>> nPositionen = new ArrayList<>();
        for (BuchungsAuftrag<KEY> auftrag : this) {
            nPositionen.add(auftrag);
        }
        BuchungsAuftrag<KEY> dummy = get(0);
        dummy.inEinPaarB�ndelF�llen(nPositionen, b�ndel);
    }
}
