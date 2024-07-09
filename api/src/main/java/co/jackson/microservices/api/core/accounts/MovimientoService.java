package co.jackson.microservices.api.core.accounts;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface MovimientoService {
    @GetMapping
    Flux<Movimiento> getMovimientos(HttpHeaders headers);

    @GetMapping("/{id}")
    Mono<Movimiento> getMovimientoById(
        @RequestHeader HttpHeaders headers,
        @PathVariable Long id,
        @RequestParam(value = "delay", required = false, defaultValue = "0") int delay,
        @RequestParam(value = "faultPercent", required = false, defaultValue = "0") int faultPercent
    );

    @PostMapping
    Mono<Movimiento> createMovimiento(@RequestBody Movimiento body);

    @PutMapping("/{id}")
    Mono<Movimiento> updateMovimiento(@PathVariable Long id, @RequestBody Movimiento body);

    @DeleteMapping("/{id}")
    Mono<Void> deleteMovimiento(@PathVariable Long id);
}
