package co.jackson.microservices.customer.services;

import java.util.List;

import co.jackson.microservices.api.core.customer.Cliente;
import co.jackson.microservices.customer.persistence.ClienteEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
    @Mappings({
        @Mapping(target = "serviceAddress", ignore = true)
    })
    Cliente entityToApi(ClienteEntity entity);

    ClienteEntity apiToEntity(Cliente api);

    List<Cliente> entityListToApiList(List<ClienteEntity> entity);

    List<ClienteEntity> apiListToEntityList(List<Cliente> api);
}
