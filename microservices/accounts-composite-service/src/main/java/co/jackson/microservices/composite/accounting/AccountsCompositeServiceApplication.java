package co.jackson.microservices.composite.accounting;

import brave.baggage.BaggagePropagation;
import brave.baggage.BaggagePropagationCustomizer;
import brave.propagation.B3Propagation;
import brave.propagation.Propagation;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Hooks;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
@ComponentScan("co.jackson")
public class AccountsCompositeServiceApplication {


    private static final Logger LOG = LoggerFactory.getLogger(AccountsCompositeServiceApplication.class);

    @Value("${api.common.version}")         String apiVersion;
    @Value("${api.common.title}")           String apiTitle;
    @Value("${api.common.description}")     String apiDescription;
    @Value("${api.common.termsOfService}")  String apiTermsOfService;
    @Value("${api.common.license}")         String apiLicense;
    @Value("${api.common.licenseUrl}")      String apiLicenseUrl;
    @Value("${api.common.externalDocDesc}") String apiExternalDocDesc;
    @Value("${api.common.externalDocUrl}")  String apiExternalDocUrl;
    @Value("${api.common.contact.name}")    String apiContactName;
    @Value("${api.common.contact.url}")     String apiContactUrl;
    @Value("${api.common.contact.email}")   String apiContactEmail;

    /**
     * Will exposed on $HOST:$PORT/swagger-ui.html
     *
     * @return the common OpenAPI documentation
     */
    @Bean
    public OpenAPI getOpenApiDocumentation() {
        return new OpenAPI()
            .info(new Info().title(apiTitle)
                .description(apiDescription)
                .version(apiVersion)
                .contact(new Contact()
                    .name(apiContactName)
                    .url(apiContactUrl)
                    .email(apiContactEmail))
                .termsOfService(apiTermsOfService)
                .license(new License()
                    .name(apiLicense)
                    .url(apiLicenseUrl)))
            .externalDocs(new ExternalDocumentation()
                .description(apiExternalDocDesc)
                .url(apiExternalDocUrl));
    }

    private final Integer threadPoolSize;
    private final Integer taskQueueSize;

    @Autowired
    public AccountsCompositeServiceApplication(
        @Value("${app.threadPoolSize:10}") Integer threadPoolSize,
        @Value("${app.taskQueueSize:100}") Integer taskQueueSize
    ) {
        this.threadPoolSize = threadPoolSize;
        this.taskQueueSize = taskQueueSize;
    }

    @Bean
    public Scheduler publishEventScheduler() {
        LOG.info("Creates a messagingScheduler with connectionPoolSize = {}", threadPoolSize);
        return Schedulers.newBoundedElastic(threadPoolSize, taskQueueSize, "publish-pool");
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }

    @Bean
    BaggagePropagation.FactoryBuilder myPropagationFactoryBuilder(
        ObjectProvider<BaggagePropagationCustomizer> baggagePropagationCustomizers) {
        Propagation.Factory delegate = B3Propagation.newFactoryBuilder().injectFormat(B3Propagation.Format.MULTI).build();
        BaggagePropagation.FactoryBuilder builder = BaggagePropagation.newFactoryBuilder(delegate);
        baggagePropagationCustomizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
        return builder;
    }

	public static void main(String[] args) {
        Hooks.enableAutomaticContextPropagation();
		SpringApplication.run(AccountsCompositeServiceApplication.class, args);
	}

}
