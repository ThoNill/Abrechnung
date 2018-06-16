package tests.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import tests.EinBucher;
import tests.flow.payloads.BuchungAuftragPayload;
import app.repositories.BuchungRepository;
import app.repositories.KontoBewegungRepository;

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
