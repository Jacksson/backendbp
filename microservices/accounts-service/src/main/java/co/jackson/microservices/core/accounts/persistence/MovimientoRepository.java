package co.jackson.microservices.core.accounts.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface MovimientoRepository extends ReactiveCrudRepository<MovimientoEntity, Long> {
    Flux<MovimientoEntity> findByFechaIsBetween(LocalDateTime fechaInicial, LocalDateTime fechaFinal);
}
