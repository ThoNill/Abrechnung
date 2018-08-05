package org.nill.abrechnung.flow;

import lombok.extern.java.Log;

import org.nill.abrechnung.flow.handler.DateienMarkierenUndErstellen;
import org.nill.abrechnung.flow.handler.Markiere�berweiungsDateien;
import org.nill.abrechnung.repositories.AusgangsDateiRepository;
import org.nill.abrechnung.repositories.�berweisungRepository;
import org.nill.zahlungen.actions.�berweisungsDatei;
import org.nill.zahlungen.values.TypeReference;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.StandardIntegrationFlow;

@Log
public class AuszahlungFlow {

    @Bean
    @Qualifier("auszahlungChannel")
    public DirectChannel mandantChannel() {
        return new DirectChannel();
    };

    @Value("${maxEntryInDatei}")
    int count;

    @Bean
    @Qualifier("auszahlungFlow")
    public StandardIntegrationFlow processFileFlowBuilder(
            AusgangsDateiRepository ausgangsDateiRepository,
            �berweisungRepository �berweisungRepository,
            ApplicationContext applicationContext) {
        return IntegrationFlows
                .from("auszahlungChannel")
                .transform(
                        createMarkiere�berweiungsDateien(
                                ausgangsDateiRepository, �berweisungRepository,
                                count))
                .transform(
                        createMarkierenUndDateiErstellen(
                                ausgangsDateiRepository, �berweisungRepository))
                .handle(x -> log.info("im Handler: " + x.toString())).get();
    }

    private DateienMarkierenUndErstellen createMarkierenUndDateiErstellen(
            AusgangsDateiRepository ausgangsDateiRepository,
            �berweisungRepository �berweisungRepository) {
        return new DateienMarkierenUndErstellen(createManager(
                ausgangsDateiRepository, �berweisungRepository));
    }

    private Markiere�berweiungsDateien createMarkiere�berweiungsDateien(
            AusgangsDateiRepository ausgangsDateiRepository,
            �berweisungRepository �berweisungRepository, int count) {
        return new Markiere�berweiungsDateien(createManager(
                ausgangsDateiRepository, �berweisungRepository), count);
    }

    �berweisungsDatei createManager(
            AusgangsDateiRepository ausgangsDateiRepository,
            �berweisungRepository �berweisungRepository) {
        return new �berweisungsDatei(ausgangsDateiRepository,
                �berweisungRepository, ".", "Test", 1, new TypeReference(1, 1L));
    }
}
