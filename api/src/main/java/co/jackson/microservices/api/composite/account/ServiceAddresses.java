package co.jackson.microservices.api.composite.account;

public class ServiceAddresses {
  private final String cmp;
  private final String pro;
  private final String rev;
  private final String rec;

  public ServiceAddresses() {
    cmp = null;
    pro = null;
    rev = null;
    rec = null;
  }

  public ServiceAddresses(
    String compositeAddress,
    String cuentasAddress,
    String clientesAddress,
    String movimientosAddress) {

    this.cmp = compositeAddress;
    this.pro = cuentasAddress;
    this.rev = clientesAddress;
    this.rec = movimientosAddress;
  }

  public String getCmp() {
    return cmp;
  }

  public String getPro() {
    return pro;
  }

  public String getRev() {
    return rev;
  }

  public String getRec() {
    return rec;
  }
}
