package org.nill.abrechnung.flow;

import lombok.extern.java.Log;

import org.nill.abrechnung.flow.handler.BerechneBuchungsauftrag;
import org.nill.abrechnung.flow.handler.BucheDenBuchungsauftrag;
import org.nill.abrechnung.flow.handler.Geb�hrDefinitionAggregator;
import org.nill.abrechnung.flow.handler.Geb�hrDefinitionSplitter;
import org.nill.abrechnung.flow.handler.HoleAbrechnung;
import org.nill.abrechnung.flow.handler.Schlie�eDieAbrechnungAb;
import org.nill.abrechnung.interfaces.AbrechnungsKonfigurator;
import org.nill.abrechnung.interfaces.Umgebung;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.StandardIntegrationFlow;

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
    @Qualifier("abrechnungsFlowEndChannel")
    public DirectChannel abrechnungsFlowEndChannel() {
        return new DirectChannel();
    };

    
    @Bean
    @Qualifier("abrechnungFlow")
    public StandardIntegrationFlow processFileFlowBuilder(
            Umgebung umgebung,
            AbrechnungsKonfigurator konfigurator,
            ApplicationContext applicationContext) {
        return IntegrationFlows
                .from("parameterChannel")
                .transform(holeAbrechnung(umgebung))
                .channel("mandantChannel")
                .split(geb�hrDefinitionSplitter(umgebung))
                .transform(
                        berechneBuchungsauftrag(konfigurator, umgebung))
                .transform(
                        bucheDenBuchungsauftrag(umgebung))

                .aggregate(a -> a.processor(new Geb�hrDefinitionAggregator()))
                .transform(
                        schlie�eDieAbrechnungAb(umgebung))
                .channel("abrechnungsFlowEndChannel")        
                .handle(x -> log.info("im Handler: " + x.toString())).get();

    }

    private Schlie�eDieAbrechnungAb schlie�eDieAbrechnungAb(
            Umgebung umgebung) {
        return new Schlie�eDieAbrechnungAb(umgebung);
    }

    @Bean
    HoleAbrechnung holeAbrechnung(Umgebung provider) {
        return new HoleAbrechnung(provider);
    }

    @Bean
    Geb�hrDefinitionSplitter geb�hrDefinitionSplitter(Umgebung umgebung) {
        return new Geb�hrDefinitionSplitter(umgebung);
    }

    @Bean
    BerechneBuchungsauftrag berechneBuchungsauftrag(
            AbrechnungsKonfigurator konfigurator,
            Umgebung umgebung) {
        return new BerechneBuchungsauftrag(konfigurator, umgebung);
    }

    @Bean
    BucheDenBuchungsauftrag bucheDenBuchungsauftrag(
            Umgebung umgebung) {
        return new BucheDenBuchungsauftrag(umgebung);
    }
}
