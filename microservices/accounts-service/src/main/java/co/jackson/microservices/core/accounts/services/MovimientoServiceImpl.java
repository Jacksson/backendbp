package co.jackson.microservices.core.accounts.services;

import co.jackson.microservices.api.core.accounts.Movimiento;
import co.jackson.microservices.api.core.accounts.MovimientoService;
import co.jackson.microservices.api.core.accounts.TipoMovimiento;
import co.jackson.microservices.api.exceptions.InvalidInputException;
import co.jackson.microservices.api.exceptions.NotFoundException;
import co.jackson.microservices.api.exceptions.SaldoInsuficienteException;
import co.jackson.microservices.core.accounts.persistence.CuentaRepository;
import co.jackson.microservices.core.accounts.persistence.MovimientoEntity;
import co.jackson.microservices.core.accounts.persistence.MovimientoRepository;
import co.jackson.microservices.util.http.ServiceUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Objects;

import static java.util.logging.Level.FINE;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoServiceImpl implements MovimientoService {
    private static final Logger LOG = LoggerFactory.getLogger(MovimientoServiceImpl.class);

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;
    private final MovimientoMapper movimientoMapper;
    private final ServiceUtil serviceUtil;

    @Autowired
    public MovimientoServiceImpl(
        CuentaRepository cuentaRepository,
        MovimientoRepository movimientoRepository,
        MovimientoMapper movimientoMapper,
        ServiceUtil serviceUtil)
    {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
        this.movimientoMapper = movimientoMapper;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Flux<Movimiento> getMovimientos(HttpHeaders headers) {

        LOG.info("Will get all for account with id={}");

        return movimientoRepository.findAll()
            .log(LOG.getName(), FINE)
            .map(movimientoMapper::entityToApi)
            .map(this::setServiceAddress);
    }

    @Override
    public Mono<Movimiento> getMovimientoById(HttpHeaders headers, Long id, int delay, int faultPercent) {
        LOG.info("Will get all for account with id={}", id);

         return movimientoRepository.findById(id)
             .log(LOG.getName(), FINE)
             .map(movimientoMapper::entityToApi)
             .map(this::setServiceAddress);
    }

    @Override
    public Mono<Movimiento> createMovimiento(Movimiento body) {

        if (body.getId() < 1) {
            throw new InvalidInputException("Invalid Movement: " + body.getId());
        }

        return cuentaRepository.findByNumeroCuenta(body.getNumeroCuenta())
            .flatMap(cuenta -> {
                if (body.getTipoMovimiento().equals(TipoMovimiento.DEPOSITO)) {
                    return internalCreateMovimiento(body);
                } else {
                    BigDecimal nuevoSaldo = cuenta.getSaldoInicial().subtract(BigDecimal.valueOf(body.getValor()));
                    if (nuevoSaldo.compareTo(BigDecimal.ZERO) >= 0) {
                        cuenta.setSaldoInicial(nuevoSaldo);
                        return cuentaRepository.save(cuenta)
                            .then(internalCreateMovimiento(body));
                    } else {
                        return Mono.error(new SaldoInsuficienteException("Saldo no disponible"));
                    }
                }
            });
    }

    private Mono<Movimiento> internalCreateMovimiento(Movimiento body) {
        MovimientoEntity entity = movimientoMapper.apiToEntity(body);
        return movimientoRepository.save(entity)
            .log(LOG.getName(), FINE)
            .onErrorMap(
                DuplicateKeyException.class,
                ex -> new InvalidInputException("Duplicate key, Movement Id: " + body.getId()))
            .map(movimientoMapper::entityToApi);
    }

    @Override
    public Mono<Movimiento> updateMovimiento(Long id, Movimiento body) {
        if (id < 1) {
            throw new InvalidInputException("Invalid Movement: " + body.getId());
        }

        return movimientoRepository.findById(id)
                .log(LOG.getName(), FINE)
                .flatMap(m ->{
                    if(Objects.nonNull(m)){
                        body.setId(m.getId());
                        return createMovimiento(body);
                    }else{
                        return Mono.error(new NotFoundException("Movement Not Found"));
                    }
                });
    }

    @Override
    public Mono<Void> deleteMovimiento(Long id) {
        if (id < 1) {
            throw new InvalidInputException("Invalid id: " + id);
        }

        LOG.debug("deleteMovimiento: tries to delete for id: {}", id);
        return movimientoRepository.deleteById(id);
    }


    private Movimiento setServiceAddress(Movimiento e) {
        e.setServiceAddress(serviceUtil.getServiceAddress());
        return e;
    }
}
