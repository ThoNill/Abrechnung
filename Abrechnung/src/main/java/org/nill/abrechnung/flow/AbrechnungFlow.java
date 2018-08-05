package org.nill.abrechnung.flow;

import lombok.extern.java.Log;

import org.nill.abrechnung.aufz�hlungen.SachKontoProvider;
import org.nill.abrechnung.flow.handler.AbrechnungsKonfigurator;
import org.nill.abrechnung.flow.handler.BerechneBuchungsauftrag;
import org.nill.abrechnung.flow.handler.BucheDenBuchungsauftrag;
import org.nill.abrechnung.flow.handler.Geb�hrDefinitionAggregator;
import org.nill.abrechnung.flow.handler.Geb�hrDefinitionSplitter;
import org.nill.abrechnung.flow.handler.HoleAbrechnung;
import org.nill.abrechnung.flow.handler.Schlie�eDieAbrechnungAb;
import org.nill.abrechnung.repositories.AbrechnungRepository;
import org.nill.abrechnung.repositories.BuchungRepository;
import org.nill.abrechnung.repositories.LeistungRepository;
import org.nill.abrechnung.repositories.MandantRepository;
import org.nill.abrechnung.repositories.ZahlungsAuftragRepository;
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
                .split(geb�hrDefinitionSplitter())
                .transform(
                        berechneBuchungsauftrag(konfigurator, sachKontoProvider))
                .transform(
                        bucheDenBuchungsauftrag(sachKontoProvider,
                                buchungRepository, abrechnungRepository))

                .aggregate(a -> a.processor(new Geb�hrDefinitionAggregator()))
                .transform(
                        schlie�eDieAbrechnungAb(sachKontoProvider,
                                abrechnungRepository, buchungRepository,
                                zahlungsAuftragRepository))
                .handle(x -> log.info("im Handler: " + x.toString())).get();

    }

    private Schlie�eDieAbrechnungAb schlie�eDieAbrechnungAb(
            SachKontoProvider sachKontoProvider,
            AbrechnungRepository abrechnungRepository,
            BuchungRepository buchungRepository,
            ZahlungsAuftragRepository zahlungsAuftragRepository) {
        return new Schlie�eDieAbrechnungAb(sachKontoProvider);
    }

    @Bean
    HoleAbrechnung holeAbrechnung(SachKontoProvider provider) {
        return new HoleAbrechnung(provider);
    }

    @Bean
    Geb�hrDefinitionSplitter geb�hrDefinitionSplitter() {
        return new Geb�hrDefinitionSplitter();
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
