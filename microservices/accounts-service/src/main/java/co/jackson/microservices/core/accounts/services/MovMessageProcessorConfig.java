package co.jackson.microservices.core.accounts.services;

import co.jackson.microservices.api.core.accounts.Movimiento;
import co.jackson.microservices.api.core.accounts.MovimientoService;
import co.jackson.microservices.api.event.Event;
import co.jackson.microservices.api.exceptions.EventProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class MovMessageProcessorConfig {

    private static final Logger LOG = LoggerFactory.getLogger(MovMessageProcessorConfig.class);

    private final MovimientoService movimientoService;

    @Autowired
    public MovMessageProcessorConfig(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @Bean
    public Consumer<Event<Integer, Movimiento>> messageProcessor() {
        return event -> {
            LOG.info("Process message created at {}...", event.getEventCreatedAt());

            switch (event.getEventType()) {

                case CREATE:
                    Movimiento movimiento = event.getData();
                    LOG.info("Create movimiento with ID: {}/{}", movimiento.getId(), movimiento.getTipoMovimiento());
                    movimientoService.createMovimiento(movimiento).block();
                    break;

                case DELETE:
                    long movimientoId = event.getKey();
                    LOG.info("Delete movimiento: {}", movimientoId);
                    movimientoService.deleteMovimiento(movimientoId).block();
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
