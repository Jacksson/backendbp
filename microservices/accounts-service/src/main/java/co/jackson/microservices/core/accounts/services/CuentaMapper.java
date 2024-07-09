package co.jackson.microservices.core.accounts.services;

import co.jackson.microservices.api.core.accounts.Cuenta;
import co.jackson.microservices.core.accounts.persistence.CuentaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CuentaMapper {

    Cuenta entityToApi(CuentaEntity entity);

    CuentaEntity apiToEntity(Cuenta api);

    List<Cuenta> entityListToApiList(List<CuentaEntity> entity);

    List<CuentaEntity> apiListToEntityList(List<Cuenta> api);
}
