package co.jackson.microservices.core.accounts.services;

import co.jackson.microservices.api.core.accounts.Cuenta;
import co.jackson.microservices.api.core.accounts.CuentaService;
import co.jackson.microservices.api.event.Event;
import co.jackson.microservices.api.exceptions.EventProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class AccountMessageProcessorConfig {

    private static final Logger LOG = LoggerFactory.getLogger(AccountMessageProcessorConfig.class);

    private final CuentaService cuentaService;

    @Autowired
    public AccountMessageProcessorConfig(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @Bean
    public Consumer<Event<Integer, Cuenta>> messageProcessor() {
        return event -> {
            LOG.info("Process message created at {}...", event.getEventCreatedAt());

            switch (event.getEventType()) {

                case CREATE:
                    Cuenta cuenta = event.getData();
                    LOG.info("Create cuenta with ID: {}/{}", cuenta.getId(), cuenta.getTipoCuenta());
                    cuentaService.createCuenta(cuenta).block();
                    break;

                case DELETE:
                    long cuentaId = event.getKey();
                    LOG.info("Delete cuenta: {}", cuentaId);
                    cuentaService.deleteCuenta(cuentaId).block();
                    break;

                default:
                    String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE or DELETE event";
                    LOG.warn(errorMessage);
                    throw new EventProcessingException(errorMessage);
            }

            LOG.info("Message processing done!");
        };
    }
}
