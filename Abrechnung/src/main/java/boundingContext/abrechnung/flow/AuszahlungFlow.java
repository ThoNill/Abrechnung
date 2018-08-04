package boundingContext.abrechnung.flow;

import lombok.extern.java.Log;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.StandardIntegrationFlow;

import boundingContext.abrechnung.flow.handler.DateienMarkierenUndErstellen;
import boundingContext.abrechnung.flow.handler.MarkiereÜberweiungsDateien;
import boundingContext.abrechnung.repositories.AusgangsDateiRepository;
import boundingContext.abrechnung.repositories.ÜberweisungRepository;
import boundingContext.zahlungen.actions.ÜberweisungsDatei;
import boundingContext.zahlungen.values.TypeReference;

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
            ÜberweisungRepository überweisungRepository,
            ApplicationContext applicationContext) {
        return IntegrationFlows
                .from("auszahlungChannel")
                .transform(
                        createMarkiereÜberweiungsDateien(
                                ausgangsDateiRepository, überweisungRepository,
                                count))
                .transform(
                        createMarkierenUndDateiErstellen(
                                ausgangsDateiRepository, überweisungRepository))
                .handle(x -> log.info("im Handler: " + x.toString())).get();
    }

    private DateienMarkierenUndErstellen createMarkierenUndDateiErstellen(
            AusgangsDateiRepository ausgangsDateiRepository,
            ÜberweisungRepository überweisungRepository) {
        return new DateienMarkierenUndErstellen(createManager(
                ausgangsDateiRepository, überweisungRepository));
    }

    private MarkiereÜberweiungsDateien createMarkiereÜberweiungsDateien(
            AusgangsDateiRepository ausgangsDateiRepository,
            ÜberweisungRepository überweisungRepository, int count) {
        return new MarkiereÜberweiungsDateien(createManager(
                ausgangsDateiRepository, überweisungRepository), count);
    }

    ÜberweisungsDatei createManager(
            AusgangsDateiRepository ausgangsDateiRepository,
            ÜberweisungRepository überweisungRepository) {
        return new ÜberweisungsDatei(ausgangsDateiRepository,
                überweisungRepository, ".", "Test", 1, new TypeReference(1, 1L));
    }
}
