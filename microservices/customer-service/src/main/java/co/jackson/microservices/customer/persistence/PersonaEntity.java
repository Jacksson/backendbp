package co.jackson.microservices.customer.persistence;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
public class PersonaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String genero;
    private int edad;
    private String identificacion;
    private String direccion;
    private String telefono;
}
