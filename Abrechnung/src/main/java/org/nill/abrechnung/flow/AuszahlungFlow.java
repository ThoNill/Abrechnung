package org.nill.abrechnung.flow;

import lombok.extern.java.Log;

import org.nill.abrechnung.flow.handler.DateienMarkierenUndErstellen;
import org.nill.abrechnung.flow.handler.Markiere�berweiungsDateien;
import org.nill.abrechnung.interfaces.Umgebung;
import org.nill.allgemein.values.TypeReference;
import org.nill.zahlungen.actions.�berweisungsDatei;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.StandardIntegrationFlow;

@Log
public class AuszahlungFlow {
    
    @Value("${maxEntryInDatei}")
    int count;

    @Bean
    @Qualifier("auszahlungChannel")
    public DirectChannel mandantChannel() {
        return new DirectChannel();
    }

    @Bean
    @Qualifier("auszahlungFlowEndChannel")
    public DirectChannel auszahlungFlowEndChannell() {
        return new DirectChannel();
    }


    @Bean
    @Qualifier("auszahlungFlow")
    public StandardIntegrationFlow processFileFlowBuilder(
            Umgebung umgebung,
            ApplicationContext applicationContext) {
        return IntegrationFlows
                .from("auszahlungChannel")
                .transform(
                        createMarkiere�berweiungsDateien(
                                umgebung,
                                count))
                .transform(
                        createMarkierenUndDateiErstellen(
                                umgebung))
                .channel("auszahlungFlowEndChannel")                      
                .handle(x -> log.info("im Handler: " + x.toString())).get();
    }

    private DateienMarkierenUndErstellen createMarkierenUndDateiErstellen(
            Umgebung provider) {
        return new DateienMarkierenUndErstellen(createManager(provider));
    }

    private Markiere�berweiungsDateien createMarkiere�berweiungsDateien(
            Umgebung provider, int count) {
        return new Markiere�berweiungsDateien(createManager(
                provider), count);
    }

    �berweisungsDatei createManager(
            Umgebung provider) {
        return new �berweisungsDatei(provider, ".", "Test", 1, new TypeReference(1, 1L));
    }
}
