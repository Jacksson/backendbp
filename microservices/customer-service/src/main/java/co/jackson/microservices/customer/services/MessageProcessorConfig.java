package co.jackson.microservices.customer.services;

import java.util.function.Consumer;

import co.jackson.microservices.api.core.customer.Cliente;
import co.jackson.microservices.api.event.Event;
import co.jackson.microservices.api.exceptions.EventProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.jackson.microservices.api.core.customer.ClienteService;

@Configuration
public class MessageProcessorConfig {

    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessorConfig.class);

    private final ClienteService clienteService;

    @Autowired
    public MessageProcessorConfig(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Bean
    public Consumer<Event<Integer, Cliente>> messageProcessor() {
        return event -> {
            LOG.info("Process message created at {}...", event.getEventCreatedAt());

            switch (event.getEventType()) {

                case CREATE:
                    Cliente cliente = event.getData();
                    LOG.info("Create cliente with ID: {}/{}", cliente.getClienteId(), cliente.getIdentificacion());
                    clienteService.createCliente(cliente).block();
                    break;

                case DELETE:
                    long clienteId = event.getKey();
                    LOG.info("Delete cliente: {}", clienteId);
                    clienteService.deleteCliente(clienteId).block();
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
