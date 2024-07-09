package co.jackson.microservices.customer.persistence;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class ClienteEntity extends PersonaEntity {

    private Long clienteId;
    private String contrasenna;
    private boolean estado;
}
