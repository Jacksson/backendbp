package co.jackson.microservices.api.core.customer;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@RestController
//@RequestMapping("/api/clientes")
public interface ClienteService {
    @GetMapping
    Flux<Cliente> getAllClientes();

    @GetMapping("/{id}")
    Mono<Cliente> getClienteById(@PathVariable Long id);

    @PostMapping
    Mono<Cliente> createCliente(@RequestBody Cliente body);

    @PutMapping("/{id}")
    Mono<Cliente> updateCliente(@PathVariable Long id, @RequestBody Cliente body);

    @DeleteMapping("/{id}")
    Mono<Void> deleteCliente(@PathVariable Long id);
}
