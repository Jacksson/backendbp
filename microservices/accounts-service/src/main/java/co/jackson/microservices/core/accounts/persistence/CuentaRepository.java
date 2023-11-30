package co.jackson.microservices.core.accounts.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface CuentaRepository extends ReactiveCrudRepository<CuentaEntity, Long> {
    Mono<CuentaEntity> findByNumeroCuenta(String numeroCuenta);
}
