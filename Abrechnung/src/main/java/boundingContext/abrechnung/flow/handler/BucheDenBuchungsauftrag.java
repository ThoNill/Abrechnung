package boundingContext.abrechnung.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.flow.payloads.BuchungAuftragPayload;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.buchhaltung.eingang.EinBucher;

public class BucheDenBuchungsauftrag
        extends
        AbstractPayloadTransformer<BuchungAuftragPayload, BuchungAuftragPayload> {

    protected BuchungRepository buchungRepository;
    protected SachKontoProvider sachKontoProvider;
    protected AbrechnungRepository abrechnungRepository;

    public BucheDenBuchungsauftrag(SachKontoProvider sachKontoProvider,
            BuchungRepository buchungRepository,
            AbrechnungRepository abrechnungRepository) {
        super();
        this.sachKontoProvider = sachKontoProvider;
        this.buchungRepository = buchungRepository;
        this.abrechnungRepository = abrechnungRepository;
    }

    private EinBucher erzeugeEinbucher() {
        return new EinBucher(sachKontoProvider);
    }

    @Override
    protected BuchungAuftragPayload transformPayload(
            BuchungAuftragPayload payload) throws Exception {
        erzeugeEinbucher().erzeugeDifferenzBuchung(payload.getAuftrag(),
                payload.getAbrechnung());
        return payload;
    }

}
