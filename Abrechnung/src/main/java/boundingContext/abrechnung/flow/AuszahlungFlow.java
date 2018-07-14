package boundingContext.abrechnung.flow;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.StandardIntegrationFlow;

import boundingContext.abrechnung.flow.handler.DateienMarkierenUndErstellen;
import boundingContext.abrechnung.flow.handler.Markiere�berweiungsDateien;
import boundingContext.abrechnung.repositories.AusgangsDateiRepository;
import boundingContext.abrechnung.repositories.�berweisungRepository;
import boundingContext.zahlungen.helper.�berweisungenManager;
import boundingContext.zahlungen.values.TypeReference;

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
                        createMarkiere�berweiungsDateien(ausgangsDateiRepository, �berweisungRepository, count))
                .transform(
                        createMarkierenUndDateiErstellen(
                                ausgangsDateiRepository, �berweisungRepository))
                .handle(x -> System.out.println("im Handler: " + x.toString()))
                .get();
    }

    private DateienMarkierenUndErstellen createMarkierenUndDateiErstellen(
            AusgangsDateiRepository ausgangsDateiRepository,
            �berweisungRepository �berweisungRepository) {
        return new DateienMarkierenUndErstellen(createManager(
                ausgangsDateiRepository, �berweisungRepository));
    }

    private Markiere�berweiungsDateien createMarkiere�berweiungsDateien(
            AusgangsDateiRepository ausgangsDateiRepository,
            �berweisungRepository �berweisungRepository,int count) {
        return new Markiere�berweiungsDateien(createManager(
                ausgangsDateiRepository, �berweisungRepository),count);
    }

    
    �berweisungenManager createManager(
            AusgangsDateiRepository ausgangsDateiRepository,
            �berweisungRepository �berweisungRepository) {
        return new �berweisungenManager(ausgangsDateiRepository,
                �berweisungRepository, ".", "Test", 1, new TypeReference(1, 1L));
    }
}
