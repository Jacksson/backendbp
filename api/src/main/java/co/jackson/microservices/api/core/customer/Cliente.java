package co.jackson.microservices.api.core.customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
public class Cliente extends Persona {

    private Long clienteId;
    private String contrasenna;
    private boolean estado;
    private String serviceAddress;

    public Cliente() {}
    public Cliente(
        Long id,
        String nombre,
        String genero,
        int edad,
        String identificacion,
        String direccion,
        String telefono)
    {
        super(id, nombre, genero, edad, identificacion, direccion, telefono);
    }
}
