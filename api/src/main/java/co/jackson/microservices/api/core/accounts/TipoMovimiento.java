package co.jackson.microservices.api.core.accounts;

import lombok.Getter;

@Getter
public enum TipoMovimiento {
    DEPOSITO("DEPOSITO"),
    RETIRO("RETIRO"),
    TRANSFERENCIA("TRANSFERENCIA");

    private final String value;

    TipoMovimiento(String value) {
        this.value = value;
    }

}
