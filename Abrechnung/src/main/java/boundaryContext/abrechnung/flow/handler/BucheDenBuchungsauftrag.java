package boundaryContext.abrechnung.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import boundaryContext.abrechnung.flow.payloads.BuchungAuftragPayload;
import boundaryContext.abrechnung.helper.EinBucher;
import boundaryContext.abrechnung.repositories.BuchungRepository;
import boundaryContext.abrechnung.repositories.KontoBewegungRepository;

public class BucheDenBuchungsauftrag
        extends
        AbstractPayloadTransformer<BuchungAuftragPayload, BuchungAuftragPayload> {

    protected BuchungRepository buchungRepository;

    protected KontoBewegungRepository kontoBewegungRepository;

    public BucheDenBuchungsauftrag(BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository) {
        super();
        this.buchungRepository = buchungRepository;
        this.kontoBewegungRepository = kontoBewegungRepository;
    }

    private EinBucher erzeugeEinbucher() {
        return new EinBucher(buchungRepository, kontoBewegungRepository);
    }

    @Override
    protected BuchungAuftragPayload transformPayload(
            BuchungAuftragPayload payload) throws Exception {
        erzeugeEinbucher().erzeugeDifferenzBuchung(payload.getAuftrag(),
                payload.getAbrechnung());
        return payload;
    }

}
