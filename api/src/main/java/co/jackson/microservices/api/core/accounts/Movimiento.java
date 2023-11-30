package co.jackson.microservices.api.core.accounts;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class Movimiento {

    private Long id;
    private Date fecha;
    private String tipoMovimiento;
    private String numeroCuenta;
    private double valor;
    private double saldo;
    private String serviceAddress;

}
