package co.jackson.microservices.core.accounts.persistence;


import lombok.Data;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Data
@Table(name = "cuentas")
public class CuentaEntity {

    @Id @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Version
    private int version;

    @Column(name = "numero_cuenta")
    private String numeroCuenta;

    @Column(name = "tipo_cuenta")
    private String tipoCuenta;

    @Column(name = "saldo_inicial")
    private BigDecimal saldoInicial;

    @Column(name = "estado")
    private Boolean estado;
}
