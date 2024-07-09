package co.jackson.microservices.customer.services;

import co.jackson.microservices.api.core.customer.Cliente;
import co.jackson.microservices.customer.persistence.ClienteEntity;
import co.jackson.microservices.customer.persistence.ClienteRepository;
import co.jackson.microservices.util.http.ServiceUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.scheduler.Scheduler;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ClienteServiceImpl.class)
class ClienteServiceImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Scheduler mockJdbcScheduler;
    @MockBean
    private ClienteRepository mockRepository;
    @MockBean
    private ClienteMapper mockMapper;
    @MockBean
    private ServiceUtil mockServiceUtil;

    @Test
    void testGetAllClientes() throws Exception {
        // Setup
        // Configure ClienteRepository.findByEstado(...).
        final ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setClienteId(0L);
        clienteEntity.setContrasenna("contrasenna");
        clienteEntity.setEstado(false);
        final List<ClienteEntity> clienteEntities = List.of(clienteEntity);
        when(mockRepository.findByEstado(false)).thenReturn(clienteEntities);

        // Configure ClienteMapper.entityListToApiList(...).
        final Cliente cliente = new Cliente();
        cliente.setIdentificacion("identificacion");
        cliente.setClienteId(0L);
        cliente.setContrasenna("contrasenna");
        cliente.setEstado(false);
        cliente.setServiceAddress("serviceAddress");
        final List<Cliente> clientes = List.of(cliente);
        final ClienteEntity clienteEntity1 = new ClienteEntity();
        clienteEntity1.setClienteId(0L);
        clienteEntity1.setContrasenna("contrasenna");
        clienteEntity1.setEstado(false);
        final List<ClienteEntity> entity = List.of(clienteEntity1);
        when(mockMapper.entityListToApiList(entity)).thenReturn(clientes);

        when(mockServiceUtil.getServiceAddress()).thenReturn("serviceAddress");

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/api/clientes")
                        .param("estado", "false")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetAllClientes_ClienteRepositoryReturnsNoItems() throws Exception {
        // Setup
        when(mockRepository.findByEstado(false)).thenReturn(Collections.emptyList());

        // Configure ClienteMapper.entityListToApiList(...).
        final Cliente cliente = new Cliente();
        cliente.setIdentificacion("identificacion");
        cliente.setClienteId(0L);
        cliente.setContrasenna("contrasenna");
        cliente.setEstado(false);
        cliente.setServiceAddress("serviceAddress");
        final List<Cliente> clientes = List.of(cliente);
        final ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setClienteId(0L);
        clienteEntity.setContrasenna("contrasenna");
        clienteEntity.setEstado(false);
        final List<ClienteEntity> entity = List.of(clienteEntity);
        when(mockMapper.entityListToApiList(entity)).thenReturn(clientes);

        when(mockServiceUtil.getServiceAddress()).thenReturn("serviceAddress");

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/api/clientes")
                        .param("estado", "false")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetAllClientes_ClienteMapperReturnsNoItems() throws Exception {
        // Setup
        // Configure ClienteRepository.findByEstado(...).
        final ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setClienteId(0L);
        clienteEntity.setContrasenna("contrasenna");
        clienteEntity.setEstado(false);
        final List<ClienteEntity> clienteEntities = List.of(clienteEntity);
        when(mockRepository.findByEstado(false)).thenReturn(clienteEntities);

        // Configure ClienteMapper.entityListToApiList(...).
        final ClienteEntity clienteEntity1 = new ClienteEntity();
        clienteEntity1.setClienteId(0L);
        clienteEntity1.setContrasenna("contrasenna");
        clienteEntity1.setEstado(false);
        final List<ClienteEntity> entity = List.of(clienteEntity1);
        when(mockMapper.entityListToApiList(entity)).thenReturn(Collections.emptyList());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/api/clientes")
                        .param("estado", "false")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetClienteById() throws Exception {
        // Setup
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/api/clientes/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo("");
    }

    @Test
    void testCreateCliente() throws Exception {
        // Setup
        // Configure ClienteMapper.apiToEntity(...).
        final ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setClienteId(0L);
        clienteEntity.setContrasenna("contrasenna");
        clienteEntity.setEstado(false);
        final Cliente api = new Cliente();
        api.setIdentificacion("identificacion");
        api.setClienteId(0L);
        api.setContrasenna("contrasenna");
        api.setEstado(false);
        api.setServiceAddress("serviceAddress");
        when(mockMapper.apiToEntity(api)).thenReturn(clienteEntity);

        // Configure ClienteRepository.save(...).
        final ClienteEntity clienteEntity1 = new ClienteEntity();
        clienteEntity1.setClienteId(0L);
        clienteEntity1.setContrasenna("contrasenna");
        clienteEntity1.setEstado(false);
        final ClienteEntity entity = new ClienteEntity();
        entity.setClienteId(0L);
        entity.setContrasenna("contrasenna");
        entity.setEstado(false);
        when(mockRepository.save(entity)).thenReturn(clienteEntity1);

        // Configure ClienteMapper.entityToApi(...).
        final Cliente cliente = new Cliente();
        cliente.setIdentificacion("identificacion");
        cliente.setClienteId(0L);
        cliente.setContrasenna("contrasenna");
        cliente.setEstado(false);
        cliente.setServiceAddress("serviceAddress");
        final ClienteEntity entity1 = new ClienteEntity();
        entity1.setClienteId(0L);
        entity1.setContrasenna("contrasenna");
        entity1.setEstado(false);
        when(mockMapper.entityToApi(entity1)).thenReturn(cliente);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/clientes")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testUpdateCliente() throws Exception {
        // Setup
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(put("/api/clientes/{id}", 0)
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo("");
    }

    @Test
    void testDeleteCliente() throws Exception {
        // Setup
        // Configure ClienteRepository.findById(...).
        final ClienteEntity clienteEntity1 = new ClienteEntity();
        clienteEntity1.setClienteId(0L);
        clienteEntity1.setContrasenna("contrasenna");
        clienteEntity1.setEstado(false);
        final Optional<ClienteEntity> clienteEntity = Optional.of(clienteEntity1);
        when(mockRepository.findById(0L)).thenReturn(clienteEntity);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(delete("/api/clientes/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");

        // Confirm ClienteRepository.delete(...).
        final ClienteEntity entity = new ClienteEntity();
        entity.setClienteId(0L);
        entity.setContrasenna("contrasenna");
        entity.setEstado(false);
        verify(mockRepository).delete(entity);
    }

    @Test
    void testDeleteCliente_ClienteRepositoryFindByIdReturnsAbsent() throws Exception {
        // Setup
        when(mockRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(delete("/api/clientes/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }
}
