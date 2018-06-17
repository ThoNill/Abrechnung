package boundingContext.abrechnung.flow;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.StandardIntegrationFlow;

import boundaryContext.abrechnung.flow.handler.BerechneBuchungsauftrag;
import boundaryContext.abrechnung.flow.handler.BucheDenBuchungsauftrag;
import boundaryContext.abrechnung.flow.handler.Geb�hrDefinitionAggregator;
import boundaryContext.abrechnung.flow.handler.Geb�hrDefinitionSplitter;
import boundaryContext.abrechnung.flow.handler.HoleAbrechnung;
import boundaryContext.abrechnung.flow.handler.Schlie�eDieAbrechnungAb;
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
                .split(geb�hrDefinitionSplitter())
                .transform(berechneBuchungsauftrag(leistungRepository))
                .transform(
                        bucheDenBuchungsauftrag(buchungRepository,
                                kontoBewegungRepository))
                .aggregate(a -> a.processor(new Geb�hrDefinitionAggregator()))
                .transform(
                        schlie�eDieAbrechnungAb(abrechnungRepository,
                                buchungRepository, kontoBewegungRepository))
                .handle(x -> System.out.println("im Handler: " + x.toString()))
                .get();

    }

    private Schlie�eDieAbrechnungAb schlie�eDieAbrechnungAb(
            AbrechnungRepository abrechnungRepository,
            BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository) {
        return new Schlie�eDieAbrechnungAb(abrechnungRepository,
                buchungRepository, kontoBewegungRepository);
    }

    @Bean
    HoleAbrechnung holeAbrechnung(MandantRepository mandantRepository,
            AbrechnungRepository abrechnungRepository) {
        return new HoleAbrechnung(mandantRepository, abrechnungRepository);
    }

    @Bean
    Geb�hrDefinitionSplitter geb�hrDefinitionSplitter() {
        return new Geb�hrDefinitionSplitter();
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
