package boundingContext.buchhaltung.eingang;

import java.util.ArrayList;
import java.util.List;

import javax.money.MonetaryAmount;

import mathe.bündel.Bündel;
import mathe.bündel.BündelMap;
import mathe.kopfpos.KopfMitPositionen;
import mathe.paare.Paar;
import boundingContext.gemeinsam.BetragsBündel;
import ddd.Entity;
import ddd.Value;

public class BuchungsAufträge<ID, KEY> extends ArrayList<BuchungsAuftrag<KEY>>
        implements Value {
    private BündelMap<String, Entity<ID>> beteiligte;

    public BuchungsAufträge(BündelMap<String, Entity<ID>> beteiligte) {
        super();
        this.beteiligte = beteiligte;
    }

    public BündelMap<String, Entity<ID>> getBeteiligte() {
        return beteiligte;
    }

    public Bündel<Paar<Beschreibung, KEY>, MonetaryAmount> inEinPaarBündelumwandeln() {
        List<KopfMitPositionen<Beschreibung, KEY, MonetaryAmount, BetragsBündel<KEY>>> nPositionen = new ArrayList<>();
        for (BuchungsAuftrag<KEY> auftrag : this) {
            nPositionen.add(auftrag);
        }
        BuchungsAuftrag<KEY> dummy = get(0);
        return dummy
                .mehrereKopfMitPositionenInEinPaarBündelumwandeln(nPositionen);
    }

    public void inEinPaarBündelFüllen(
            BündelMap<Paar<Beschreibung, KEY>, MonetaryAmount> bündel) {
        List<KopfMitPositionen<Beschreibung, KEY, MonetaryAmount, BetragsBündel<KEY>>> nPositionen = new ArrayList<>();
        for (BuchungsAuftrag<KEY> auftrag : this) {
            nPositionen.add(auftrag);
        }
        BuchungsAuftrag<KEY> dummy = get(0);
        dummy.inEinPaarBündelFüllen(nPositionen, bündel);
    }
}
