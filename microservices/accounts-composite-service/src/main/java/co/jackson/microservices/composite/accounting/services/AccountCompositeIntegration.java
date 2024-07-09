package co.jackson.microservices.composite.accounting.services;

import co.jackson.microservices.api.core.accounts.TipoMovimiento;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

import co.jackson.microservices.api.core.accounts.Movimiento;
import co.jackson.microservices.api.core.accounts.MovimientoService;
import co.jackson.microservices.api.core.customer.Cliente;
import co.jackson.microservices.api.core.customer.ClienteService;
import co.jackson.microservices.api.event.Event;
import co.jackson.microservices.api.exceptions.*;
import co.jackson.microservices.util.http.ServiceUtil;
import co.jackson.microservices.util.http.HttpErrorInfo;

import java.io.IOException;
import java.net.URI;
import java.util.Date;

import static co.jackson.microservices.api.event.Event.Type.CREATE;
import static co.jackson.microservices.api.event.Event.Type.DELETE;
import static java.util.logging.Level.FINE;
import static reactor.core.publisher.Flux.empty;

@Component
public class AccountCompositeIntegration implements ClienteService, MovimientoService  {

    private static final Logger LOG = LoggerFactory.getLogger(AccountCompositeIntegration.class);

    private static final String ACCOUNT_SERVICE_URL = "http://accounts-service";
    private static final String CUSTOMER_SERVICE_URL = "http://customer-service";

    private final Scheduler publishEventScheduler;
    private final WebClient webClient;
    private final ObjectMapper mapper;
    private final StreamBridge streamBridge;
    private final ServiceUtil serviceUtil;

    @Autowired
    public AccountCompositeIntegration(
        @Qualifier("publishEventScheduler") Scheduler publishEventScheduler,
        WebClient webClient,
        ObjectMapper mapper,
        StreamBridge streamBridge,
        ServiceUtil serviceUtil)
    {
        this.publishEventScheduler = publishEventScheduler;
        this.webClient = webClient;
        this.mapper = mapper;
        this.streamBridge = streamBridge;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Flux<Movimiento> getMovimientos(HttpHeaders headers) {
        URI url = UriComponentsBuilder.fromUriString(ACCOUNT_SERVICE_URL + "/movimientos?id={id}").build(11);

        LOG.debug("Will call the getMovimientos API on URL: {}", url);

        // Return an empty result if something goes wrong to make it possible for the composite service to return partial responses
        return webClient.get().uri(url)
            .headers(h -> h.addAll(headers))
            .retrieve().bodyToFlux(Movimiento.class).log(LOG.getName(), FINE)
            .onErrorResume(error -> empty());
    }

    @Override
    @Retry(name = "movimientos")
    @TimeLimiter(name = "movimientos")
    @CircuitBreaker(name = "movimientos", fallbackMethod = "getMovimientosFallbackValue")
    public Mono<Movimiento> getMovimientoById(HttpHeaders headers, Long id, int delay, int faultPercent) {
        URI url = UriComponentsBuilder.fromUriString(ACCOUNT_SERVICE_URL
            + "/movimientos/{id}?delay={delay}&faultPercent={faultPercent}").build(id, delay, faultPercent);
        LOG.debug("Will call the getProduct API on URL: {}", url);

        return webClient.get().uri(url)
            .headers(h -> h.addAll(headers))
            .retrieve().bodyToMono(Movimiento.class).log(LOG.getName(), FINE)
            .onErrorMap(WebClientResponseException.class, this::handleException);
    }

    private Mono<Movimiento> getMovimientosFallbackValue(HttpHeaders headers, Long id, int delay, int faultPercent, CallNotPermittedException ex) {

        LOG.warn("Creating a fail-fast fallback movement for id = {}, delay = {}, faultPercent = {} and exception = {} ",
            id, delay, faultPercent, ex.toString());

        if (id < 1) {
            throw new InvalidInputException("Invalid id: " + id);
        }

        if (id == 13) {
            String errMsg = "Movement Id: " + id + " not found in fallback cache!";
            LOG.warn(errMsg);
            throw new NotFoundException(errMsg);
        }

        return Mono.just(
            new Movimiento(
                id,
                new Date(),
                "Ahorros",
                TipoMovimiento.DEPOSITO.getValue(),
                0,0,
                serviceUtil.getServiceAddress()
            )
        );
    }

    @Override
    public Mono<Movimiento> createMovimiento(Movimiento body) {
        return Mono.fromCallable(() -> {
            sendMessage("movement-out-0", new Event(CREATE, body.getId(), body));
            return body;
        }).subscribeOn(publishEventScheduler);
    }

    @Override
    public Mono<Movimiento> updateMovimiento(Long id, Movimiento body) {
        return null;
    }

    @Override
    public Mono<Void> deleteMovimiento(Long id) {
        return Mono.fromRunnable(() -> sendMessage("movement-out-0", new Event(DELETE, id, null)))
            .subscribeOn(publishEventScheduler).then();
    }

    @Override
    public Flux<Cliente> getAllClientes(boolean estado) {
        URI url = UriComponentsBuilder.fromUriString(CUSTOMER_SERVICE_URL + "/cliente?id={id}").build(11);

        LOG.debug("Will call the getAllClientes API on URL: {}", url);

        // Return an empty result if something goes wrong to make it possible for the composite service to return partial responses
        return webClient.get().uri(url)
            .retrieve().bodyToFlux(Cliente.class).log(LOG.getName(), FINE)
            .onErrorResume(error -> empty());
    }

    @Override
    public Mono<Cliente> getClienteById(Long id) {
        URI url = UriComponentsBuilder.fromUriString(CUSTOMER_SERVICE_URL
            + "/cliente/{id}").build(id);
        LOG.debug("Will call the getClienteById API on URL: {}", url);

        return webClient.get().uri(url)
            .retrieve().bodyToMono(Cliente.class).log(LOG.getName(), FINE)
            .onErrorMap(WebClientResponseException.class, this::handleException);
    }

    @Override
    public Mono<Cliente> createCliente(Cliente body) {
        return Mono.fromCallable(() -> {
            sendMessage("customer-out-0", new Event(CREATE, body.getId(), body));
            return body;
        }).subscribeOn(publishEventScheduler);
    }

    @Override
    public Mono<Cliente> updateCliente(Long id, Cliente body) {
        return null;
    }

    @Override
    public Mono<Void> deleteCliente(Long id) {
        return Mono.fromRunnable(() -> sendMessage("customer-out-0", new Event(DELETE, id, null)))
            .subscribeOn(publishEventScheduler).then();
    }

    private void sendMessage(String bindingName, Event event) {
        LOG.debug("Sending a {} message to {}", event.getEventType(), bindingName);
        Message message = MessageBuilder.withPayload(event)
            .setHeader("partitionKey", event.getKey())
            .build();
        streamBridge.send(bindingName, message);
    }


    private Throwable handleException(Throwable ex) {

        if (!(ex instanceof WebClientResponseException)) {
            LOG.warn("Got a unexpected error: {}, will rethrow it", ex.toString());
            return ex;
        }

        WebClientResponseException wcre = (WebClientResponseException)ex;

        switch (HttpStatus.resolve(wcre.getStatusCode().value())) {

            case NOT_FOUND:
                return new NotFoundException(getErrorMessage(wcre));

            case UNPROCESSABLE_ENTITY:
                return new InvalidInputException(getErrorMessage(wcre));

            default:
                LOG.warn("Got an unexpected HTTP error: {}, will rethrow it", wcre.getStatusCode());
                LOG.warn("Error body: {}", wcre.getResponseBodyAsString());
                return ex;
        }
    }

    private String getErrorMessage(WebClientResponseException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }
}
