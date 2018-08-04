package boundingContext.abrechnung.flow;

import lombok.extern.java.Log;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.StandardIntegrationFlow;

import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.flow.handler.AbrechnungsKonfigurator;
import boundingContext.abrechnung.flow.handler.BerechneBuchungsauftrag;
import boundingContext.abrechnung.flow.handler.BucheDenBuchungsauftrag;
import boundingContext.abrechnung.flow.handler.GebührDefinitionAggregator;
import boundingContext.abrechnung.flow.handler.GebührDefinitionSplitter;
import boundingContext.abrechnung.flow.handler.HoleAbrechnung;
import boundingContext.abrechnung.flow.handler.SchließeDieAbrechnungAb;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.LeistungRepository;
import boundingContext.abrechnung.repositories.MandantRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;

@Log
public class AbrechnungFlow {

    @Bean
    @Qualifier("parameterChannel")
    public DirectChannel parameterChannel() {
        return new DirectChannel();
    };

    @Bean
    @Qualifier("mandantChannel")
    public DirectChannel mandantChannel() {
        return new DirectChannel();
    };

    @Bean
    @Qualifier("abrechnungFlow")
    public StandardIntegrationFlow processFileFlowBuilder(
            SachKontoProvider sachKontoProvider,
            MandantRepository mandantRepository,
            AbrechnungRepository abrechnungRepository,
            LeistungRepository leistungRepository,
            BuchungRepository buchungRepository,
            ZahlungsAuftragRepository zahlungsAuftragRepository,
            AbrechnungsKonfigurator konfigurator,
            ApplicationContext applicationContext) {
        return IntegrationFlows
                .from("parameterChannel")
                .transform(holeAbrechnung(sachKontoProvider))
                .channel("mandantChannel")
                .split(gebührDefinitionSplitter())
                .transform(
                        berechneBuchungsauftrag(konfigurator, sachKontoProvider))
                .transform(
                        bucheDenBuchungsauftrag(sachKontoProvider,
                                buchungRepository, abrechnungRepository))

                .aggregate(a -> a.processor(new GebührDefinitionAggregator()))
                .transform(
                        schließeDieAbrechnungAb(sachKontoProvider,
                                abrechnungRepository, buchungRepository,
                                zahlungsAuftragRepository))
                .handle(x -> log.info("im Handler: " + x.toString())).get();

    }

    private SchließeDieAbrechnungAb schließeDieAbrechnungAb(
            SachKontoProvider sachKontoProvider,
            AbrechnungRepository abrechnungRepository,
            BuchungRepository buchungRepository,
            ZahlungsAuftragRepository zahlungsAuftragRepository) {
        return new SchließeDieAbrechnungAb(sachKontoProvider);
    }

    @Bean
    HoleAbrechnung holeAbrechnung(SachKontoProvider provider) {
        return new HoleAbrechnung(provider);
    }

    @Bean
    GebührDefinitionSplitter gebührDefinitionSplitter() {
        return new GebührDefinitionSplitter();
    }

    @Bean
    BerechneBuchungsauftrag berechneBuchungsauftrag(
            AbrechnungsKonfigurator konfigurator,
            SachKontoProvider sachKontoProvider) {
        return new BerechneBuchungsauftrag(konfigurator, sachKontoProvider);
    }

    @Bean
    BucheDenBuchungsauftrag bucheDenBuchungsauftrag(
            SachKontoProvider sachKontoProvider,
            BuchungRepository buchungRepository,
            AbrechnungRepository abrechnungRepository) {
        return new BucheDenBuchungsauftrag(sachKontoProvider,
                buchungRepository, abrechnungRepository);
    }
}
