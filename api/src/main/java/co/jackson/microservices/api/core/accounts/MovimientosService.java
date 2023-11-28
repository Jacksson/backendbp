package co.jackson.microservices.api.core.accounts;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@RestController
//@RequestMapping("/api/movimientos")
public interface MovimientosService {
    @GetMapping
    Flux<Movimientos> getAllMovimientos();

    @GetMapping("/{id}")
    Mono<Movimientos> getMovimientosById(@PathVariable Long id);

    @PostMapping
    Mono<Movimientos> createMovimientos(@RequestBody Movimientos body);

    @PutMapping("/{id}")
    Mono<Movimientos> updateMovimientos(@PathVariable Long id, @RequestBody Movimientos body);

    @DeleteMapping("/{id}")
    Mono<Void> deleteMovimientos(@PathVariable Long id);
}
