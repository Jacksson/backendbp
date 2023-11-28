package co.jackson.microservices.api.core.accounts;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class Movimientos {

    private Long id;
    private Date fecha;
    private String tipoMovimiento;
    private double valor;
    private double saldo;

}
