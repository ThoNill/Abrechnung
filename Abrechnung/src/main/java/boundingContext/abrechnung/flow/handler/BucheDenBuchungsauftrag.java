package boundingContext.abrechnung.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.flow.payloads.BuchungAuftragPayload;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.buchhaltung.eingang.EinBucher;

public class BucheDenBuchungsauftrag
        extends
        AbstractPayloadTransformer<BuchungAuftragPayload, BuchungAuftragPayload> {

    protected BuchungRepository buchungRepository;
    protected KontoBewegungRepository kontoBewegungRepository;
    protected SachKontoProvider sachKontoProvider;

    public BucheDenBuchungsauftrag(SachKontoProvider sachKontoProvider,
            BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository) {
        super();
        this.sachKontoProvider = sachKontoProvider;
        this.buchungRepository = buchungRepository;
        this.kontoBewegungRepository = kontoBewegungRepository;
    }

    private EinBucher erzeugeEinbucher() {
        return new EinBucher(sachKontoProvider, buchungRepository,
                kontoBewegungRepository);
    }

    @Override
    protected BuchungAuftragPayload transformPayload(
            BuchungAuftragPayload payload) throws Exception {
        erzeugeEinbucher().erzeugeDifferenzBuchung(payload.getAuftrag(),
                payload.getAbrechnung());
        return payload;
    }

}
