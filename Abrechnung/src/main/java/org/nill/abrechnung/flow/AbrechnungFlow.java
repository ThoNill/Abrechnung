package org.nill.abrechnung.flow;

import lombok.extern.java.Log;

import org.nill.abrechnung.flow.handler.BerechneBuchungsauftrag;
import org.nill.abrechnung.flow.handler.BucheDenBuchungsauftrag;
import org.nill.abrechnung.flow.handler.Geb�hrDefinitionAggregator;
import org.nill.abrechnung.flow.handler.Geb�hrDefinitionSplitter;
import org.nill.abrechnung.flow.handler.HoleAbrechnung;
import org.nill.abrechnung.flow.handler.Schlie�eDieAbrechnungAb;
import org.nill.abrechnung.interfaces.AbrechnungsKonfigurator;
import org.nill.abrechnung.interfaces.IAbrechnungRepository;
import org.nill.abrechnung.interfaces.IBuchungsRepository;
import org.nill.abrechnung.interfaces.ILeistungRepository;
import org.nill.abrechnung.interfaces.IMandantRepository;
import org.nill.abrechnung.interfaces.IZahlungsAuftragRepository;
import org.nill.abrechnung.interfaces.SachKontoProvider;
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
            SachKontoProvider sachKontoProvider,
            AbrechnungsKonfigurator konfigurator,
            ApplicationContext applicationContext) {
        return IntegrationFlows
                .from("parameterChannel")
                .transform(holeAbrechnung(sachKontoProvider))
                .channel("mandantChannel")
                .split(geb�hrDefinitionSplitter(sachKontoProvider))
                .transform(
                        berechneBuchungsauftrag(konfigurator, sachKontoProvider))
                .transform(
                        bucheDenBuchungsauftrag(sachKontoProvider))

                .aggregate(a -> a.processor(new Geb�hrDefinitionAggregator()))
                .transform(
                        schlie�eDieAbrechnungAb(sachKontoProvider))
                .channel("abrechnungsFlowEndChannel")        
                .handle(x -> log.info("im Handler: " + x.toString())).get();

    }

    private Schlie�eDieAbrechnungAb schlie�eDieAbrechnungAb(
            SachKontoProvider sachKontoProvider) {
        return new Schlie�eDieAbrechnungAb(sachKontoProvider);
    }

    @Bean
    HoleAbrechnung holeAbrechnung(SachKontoProvider provider) {
        return new HoleAbrechnung(provider);
    }

    @Bean
    Geb�hrDefinitionSplitter geb�hrDefinitionSplitter(SachKontoProvider sachKontoProvider) {
        return new Geb�hrDefinitionSplitter(sachKontoProvider);
    }

    @Bean
    BerechneBuchungsauftrag berechneBuchungsauftrag(
            AbrechnungsKonfigurator konfigurator,
            SachKontoProvider sachKontoProvider) {
        return new BerechneBuchungsauftrag(konfigurator, sachKontoProvider);
    }

    @Bean
    BucheDenBuchungsauftrag bucheDenBuchungsauftrag(
            SachKontoProvider sachKontoProvider) {
        return new BucheDenBuchungsauftrag(sachKontoProvider);
    }
}
