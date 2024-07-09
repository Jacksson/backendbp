package co.jackson.microservices.core.accounts.services;

import java.util.List;

import co.jackson.microservices.api.core.accounts.Movimiento;
import co.jackson.microservices.core.accounts.persistence.MovimientoEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MovimientoMapper {

    @Mappings({
        @Mapping(target = "serviceAddress", ignore = true)
    })
    Movimiento entityToApi(MovimientoEntity entity);

    MovimientoEntity apiToEntity(Movimiento api);

    List<Movimiento> entityListToApiList(List<MovimientoEntity> entity);

    List<MovimientoEntity> apiListToEntityList(List<Movimiento> api);
}
