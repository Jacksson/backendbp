package co.jackson.microservices.api.composite.account;

import co.jackson.microservices.api.core.accounts.Cuenta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@SecurityRequirement(name = "security_auth")
@Tag(name = "AccountComposite", description = "REST API for composite Account information.")
public interface AccountCompositeService {

    /**
     * Sample usage, see below.
     *
     * curl -X POST $HOST:$PORT/accounts-composite \
     *   -H "Content-Type: application/json" --data \
     *   '{"numero":123,"tipo":"","estado":1, "movimiento": "Retiro de 575"}'
     *
     * @param body A JSON representation of the new composite account
     */
    @Operation(
        summary = "${api.accounts-composite.create-composite.description}",
        description = "${api.accounts-composite.create-composite.notes}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
        @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping(
        value    = "/accounts-composite",
        consumes = "application/json")
    Mono<Void> createCuenta(@RequestBody Cuenta body);


    /**
     * Sample usage: "curl $HOST:$PORT/accounts-composite/1".
     *
     * @param accountsId Id of the accounts
     * @return the composite accounts info, if found, else null
     */
    @Operation(
        summary = "${api.accounts-composite.get-composite.description}",
        description = "${api.accounts-composite.get-composite.notes}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
        @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
        @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
        @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @GetMapping(
        value = "/accounts-composite/{accountsId}",
        produces = "application/json")
    Mono<Cuenta> getCuenta(
        @RequestHeader HttpHeaders headers,
        @PathVariable int accountsId,
        @RequestParam(value = "delay", required = false, defaultValue = "0") int delay,
        @RequestParam(value = "faultPercent", required = false, defaultValue = "0") int faultPercent
    );

    /**
     * Sample usage: "curl -X DELETE $HOST:$PORT/accounts-composite/1".
     *
     * @param accountsId Id of the accounts
     */
    @Operation(
        summary = "${api.accounts-composite.delete-composite.description}",
        description = "${api.accounts-composite.delete-composite.notes}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
        @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping(value = "/accounts-composite/{accountsId}")
    Mono<Void> deleteCuenta(@PathVariable int accountsId);
}
