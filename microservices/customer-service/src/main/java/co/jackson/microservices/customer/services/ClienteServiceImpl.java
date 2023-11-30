package co.jackson.microservices.customer.services;

import co.jackson.microservices.api.core.customer.Cliente;
import co.jackson.microservices.api.core.customer.ClienteService;
import co.jackson.microservices.api.exceptions.InvalidInputException;
import co.jackson.microservices.customer.persistence.ClienteEntity;
import co.jackson.microservices.customer.persistence.ClienteRepository;
import co.jackson.microservices.util.http.ServiceUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;

import static java.util.logging.Level.FINE;

@RestController
@RequestMapping("/api/clientes")
public class ClienteServiceImpl implements ClienteService {

    private static final Logger LOG = LoggerFactory.getLogger(ClienteServiceImpl.class);

    private final ClienteRepository repository;
    private final ClienteMapper mapper;
    private final ServiceUtil serviceUtil;
    private final Scheduler jdbcScheduler;

    @Autowired
    public ClienteServiceImpl(
        @Qualifier("jdbcScheduler") Scheduler jdbcScheduler,
        ClienteRepository repository,
        ClienteMapper mapper,
        ServiceUtil serviceUtil) {
        this.jdbcScheduler = jdbcScheduler;
        this.repository = repository;
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Flux<Cliente> getAllClientes(boolean estado) {
        return Mono.fromCallable(() -> internalGetClientes(estado))
            .flatMapMany(Flux::fromIterable)
            .log(LOG.getName(), FINE)
            .subscribeOn(jdbcScheduler);
    }

    private List<Cliente> internalGetClientes(boolean estado) {

        List<ClienteEntity> entityList = repository.findByEstado(estado);
        List<Cliente> list = mapper.entityListToApiList(entityList);
        list.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));

        LOG.debug("Response size: {}", list.size());

        return list;
    }

    @Override
    public Mono<Cliente> getClienteById(Long id) {
        return null;
    }

    @Override
    public Mono<Cliente> createCliente(Cliente body) {
        if (body.getClienteId() < 1) {
            throw new InvalidInputException("Invalid productId: " + body.getClienteId());
        }
        return Mono.fromCallable(() -> internalCreateCliente(body))
            .subscribeOn(jdbcScheduler);
    }

    private Cliente internalCreateCliente(Cliente body) {
        try {
            ClienteEntity entity = mapper.apiToEntity(body);
            ClienteEntity newEntity = repository.save(entity);

            LOG.debug("createCliente: created a cliente entity: {}/{}", body.getClienteId(), body.getIdentificacion());
            return mapper.entityToApi(newEntity);

        } catch (DataIntegrityViolationException dive) {
            throw new InvalidInputException("Duplicate key, cliente Id: " + body.getClienteId() + ", Identificacion:" + body.getIdentificacion());
        }
    }

    @Override
    public Mono<Cliente> updateCliente(Long id, Cliente body) {
        return null;
    }

    @Override
    public Mono<Void> deleteCliente(Long id) {
        if (id < 1) {
            throw new InvalidInputException("Invalid productId: " + id);
        }
        return Mono.fromRunnable(() -> internalDeleteCliente(id)).subscribeOn(jdbcScheduler).then();
    }

    private void internalDeleteCliente(Long id) {
        LOG.debug("deleteCliente: tries to delete cliente: {}", id);
        repository.delete(repository.findById(id).get());
    }
}
