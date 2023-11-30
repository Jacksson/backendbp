package co.jackson.microservices.api.core.accounts;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CuentaService {
    @GetMapping
    Flux<Cuenta> getAllCuentas();

    @GetMapping("/{id}")
    Mono<Cuenta> getCuentaById(@PathVariable Long id);

    @PostMapping
    Mono<Cuenta> createCuenta(@RequestBody Cuenta body);

    @PutMapping("/{id}")
    Mono<Cuenta> updateCuenta(@PathVariable Long id, @RequestBody Cuenta body);

    @DeleteMapping("/{id}")
    Mono<Void> deleteCuenta(@PathVariable Long id);
}
