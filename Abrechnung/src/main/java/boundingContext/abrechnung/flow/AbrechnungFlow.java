package boundingContext.abrechnung.flow;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.StandardIntegrationFlow;

import boundaryContext.abrechnung.flow.handler.BerechneBuchungsauftrag;
import boundaryContext.abrechnung.flow.handler.BucheDenBuchungsauftrag;
import boundaryContext.abrechnung.flow.handler.GebührDefinitionAggregator;
import boundaryContext.abrechnung.flow.handler.GebührDefinitionSplitter;
import boundaryContext.abrechnung.flow.handler.HoleAbrechnung;
import boundaryContext.abrechnung.flow.handler.SchließeDieAbrechnungAb;
import boundaryContext.abrechnung.repositories.AbrechnungRepository;
import boundaryContext.abrechnung.repositories.BuchungRepository;
import boundaryContext.abrechnung.repositories.KontoBewegungRepository;
import boundaryContext.abrechnung.repositories.LeistungRepository;
import boundaryContext.abrechnung.repositories.MandantRepository;

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
    public StandardIntegrationFlow processFileFlowBuilder(
            MandantRepository mandantRepository,
            AbrechnungRepository abrechnungRepository,
            LeistungRepository leistungRepository,
            BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository,
            ApplicationContext applicationContext) {
        return IntegrationFlows
                .from("parameterChannel")
                .transform(
                        holeAbrechnung(mandantRepository, abrechnungRepository))
                .channel("mandantChannel")
                .split(gebührDefinitionSplitter())
                .transform(berechneBuchungsauftrag(leistungRepository))
                .transform(
                        bucheDenBuchungsauftrag(buchungRepository,
                                kontoBewegungRepository))
                .aggregate(a -> a.processor(new GebührDefinitionAggregator()))
                .transform(
                        schließeDieAbrechnungAb(abrechnungRepository,
                                buchungRepository, kontoBewegungRepository))
                .handle(x -> System.out.println("im Handler: " + x.toString()))
                .get();

    }

    private SchließeDieAbrechnungAb schließeDieAbrechnungAb(
            AbrechnungRepository abrechnungRepository,
            BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository) {
        return new SchließeDieAbrechnungAb(abrechnungRepository,
                buchungRepository, kontoBewegungRepository);
    }

    @Bean
    HoleAbrechnung holeAbrechnung(MandantRepository mandantRepository,
            AbrechnungRepository abrechnungRepository) {
        return new HoleAbrechnung(mandantRepository, abrechnungRepository);
    }

    @Bean
    GebührDefinitionSplitter gebührDefinitionSplitter() {
        return new GebührDefinitionSplitter();
    }

    @Bean
    BerechneBuchungsauftrag berechneBuchungsauftrag(
            LeistungRepository leistungRepository) {
        return new BerechneBuchungsauftrag(leistungRepository);
    }

    @Bean
    BucheDenBuchungsauftrag bucheDenBuchungsauftrag(
            BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository) {
        return new BucheDenBuchungsauftrag(buchungRepository,
                kontoBewegungRepository);
    }
}
