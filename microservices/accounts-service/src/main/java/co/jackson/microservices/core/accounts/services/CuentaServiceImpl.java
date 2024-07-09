package co.jackson.microservices.core.accounts.services;

import co.jackson.microservices.api.core.accounts.Cuenta;
import co.jackson.microservices.api.core.accounts.CuentaService;
import co.jackson.microservices.api.exceptions.InvalidInputException;
import co.jackson.microservices.api.exceptions.NotFoundException;
import co.jackson.microservices.core.accounts.persistence.CuentaEntity;
import co.jackson.microservices.core.accounts.persistence.CuentaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static java.util.logging.Level.FINE;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaServiceImpl implements CuentaService {

    private static final Logger LOG = LoggerFactory.getLogger(CuentaServiceImpl.class);

    private final CuentaRepository cuentaRepository;
    private final CuentaMapper cuentaMapper;

    @Autowired
    public CuentaServiceImpl(
        CuentaRepository cuentaRepository,
        CuentaMapper cuentaMapper)
    {
        this.cuentaRepository = cuentaRepository;
        this.cuentaMapper = cuentaMapper;
    }

    @Override
    public Flux<Cuenta> getAllCuentas() {
        LOG.info("Will get all accounts");

        return cuentaRepository.findAll()
            .log(LOG.getName(), FINE)
            .map(cuentaMapper::entityToApi);
    }

    @Override
    public Mono<Cuenta> getCuentaById(Long id) {
        LOG.info("Will get all for account with id={}", id);

        return cuentaRepository.findById(id)
            .log(LOG.getName(), FINE)
            .map(cuentaMapper::entityToApi);
    }

    @Override
    public Mono<Cuenta> createCuenta(Cuenta body) {
        if (body.getId() < 1) {
            throw new InvalidInputException("Invalid Account: " + body.getId());
        }

        CuentaEntity entity = cuentaMapper.apiToEntity(body);
        return cuentaRepository.save(entity)
            .log(LOG.getName(), FINE)
            .onErrorMap(
                DuplicateKeyException.class,
                ex -> new InvalidInputException("Duplicate key, Account Id: " + body.getId()))
            .map(cuentaMapper::entityToApi);
    }

    @Override
    public Mono<Cuenta> updateCuenta(Long id, Cuenta body) {
        if (id < 1) {
            throw new InvalidInputException("Invalid Account: " + body.getId());
        }

        return cuentaRepository.findById(id)
            .log(LOG.getName(), FINE)
            .flatMap(m ->{
                if(Objects.nonNull(m)){
                    body.setId(m.getId());
                    return createCuenta(body);
                }else{
                    return Mono.error(new NotFoundException("Account Not Found"));
                }
            });
    }

    @Override
    public Mono<Void> deleteCuenta(Long id) {
        if (id < 1) {
            throw new InvalidInputException("Invalid id: " + id);
        }

        LOG.debug("deleteCuenta: tries to delete for id: {}", id);
        return cuentaRepository.deleteById(id);
    }
}
