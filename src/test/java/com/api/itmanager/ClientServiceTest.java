package com.api.itmanager;

import com.api.itmanager.modules.client.dto.ClientRequest;
import com.api.itmanager.modules.client.dto.ClientResponse;
import com.api.itmanager.modules.client.model.Client;
import com.api.itmanager.modules.client.repository.ClientRepository;
import com.api.itmanager.modules.client.service.ClientService;
import com.api.itmanager.util.exception.ClientNotFoundException;
import com.api.itmanager.util.exception.ValidationException;
import com.api.itmanager.util.response.Response;
import com.github.javafaker.Faker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RunWith(SpringRunner.class)
public class ClientServiceTest extends ApiItmanagerApplicationTests {

    private static final String EXISTS_CLIENT = "Existe outro cliente igual";
    private static final String EXISTS_CLIENT_CNPJ = "12345678912345";
    private static final String NOT_EXISTS_CLIENT = "Não existe outro cliente igual";
    private static final String NOT_EXISTS_CLIENT_CNPJ = "98765432198765";

    private Faker clientFaker;

    @Autowired
    private ClientService clientService;

    @MockBean
    private ClientRepository clientRepository;

    @Before
    public void setup() {
        this.clientFaker = new Faker(new Locale("pt-BR"));

        Client client1 = createClientFaker(1L);
        client1.setCnpj("12345678912345");

        Client client2 = createClientFaker(2L);

        Client cliet3 = createClientFaker(3L);

        Mockito.when(clientRepository.findAll()).thenReturn(Arrays.asList(client1, client2, cliet3));
        Mockito.when(clientRepository.findById(client1.getId())).thenReturn(Optional.of(client1));
        Mockito.when(clientRepository.findByCnpj(client1.getCnpj())).thenReturn(Optional.of(client1));
        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(client1);
        Mockito.when(clientRepository.existsByNameIgnoreCaseContaining(EXISTS_CLIENT)).thenReturn(true);
        Mockito.when(clientRepository.existsByNameIgnoreCaseContaining(NOT_EXISTS_CLIENT)).thenReturn(false);
        Mockito.when(clientRepository.existsByCnpj(EXISTS_CLIENT_CNPJ)).thenReturn(true);
        Mockito.when(clientRepository.existsByCnpj(NOT_EXISTS_CLIENT_CNPJ)).thenReturn(false);
    }

    private Client createClientFaker(Long id) {
        return Client.builder()
                .id(id)
                .name(clientFaker.company().name())
                .address(clientFaker.address().fullAddress())
                .cnpj(clientFaker.numerify("##############"))
                .build();
    }

    @Test
    public void testListAllClients() {
        List<ClientResponse> listClientsResponse = clientService.listAll();
        Assert.assertEquals(3L, listClientsResponse.size());
    }

    @Test
    public void testFindClientById() throws ClientNotFoundException {
        ClientResponse clientResponse = clientService.findById(1L);
        Assert.assertEquals("12345678912345", clientResponse.getCnpj());
    }

    @Test
    public void testFindClientByCnpj() throws ClientNotFoundException {
        ClientResponse clientResponse = clientService.findByCnpj("12345678912345");
        Assert.assertEquals("12345678912345", clientResponse.getCnpj());
    }

    @Test
    public void testFindClientByIdIfClientNotExists() throws ClientNotFoundException {
        Assert.assertThrows(ClientNotFoundException.class, () -> clientService.findById(2L));
    }

    @Test
    public void testCreateClient() {
        ClientRequest request = ClientRequest.builder()
                .name(NOT_EXISTS_CLIENT)
                .address(clientFaker.address().fullAddress())
                .cnpj(NOT_EXISTS_CLIENT_CNPJ)
                .build();

        Response response = clientService.createClient(request);

        Assert.assertEquals((Integer) HttpStatus.CREATED.value(), response.getStatus());
        Assert.assertEquals("Created client with ID 1", response.getMessage());
    }

    @Test
    public void testCreateClientWithErrorForDataNotInformed() {
        ClientRequest request = new ClientRequest();

        Assert.assertThrows(ValidationException.class, () -> clientService.createClient(request));
    }

    @Test
    public void testCreateClientWithErrorForNameAlreadyExists() {
        ClientRequest request = ClientRequest.builder()
                .name(EXISTS_CLIENT)
                .address(clientFaker.address().fullAddress())
                .cnpj(NOT_EXISTS_CLIENT_CNPJ)
                .build();

        Assert.assertThrows(ValidationException.class, () -> clientService.createClient(request));
    }

    @Test
    public void testCreateClientWithErrorForCnpjAlreadyExists() {
        ClientRequest request = ClientRequest.builder()
                .name(NOT_EXISTS_CLIENT)
                .address(clientFaker.address().fullAddress())
                .cnpj(EXISTS_CLIENT_CNPJ)
                .build();

        Assert.assertThrows(ValidationException.class, () -> clientService.createClient(request));
    }

    @Test
    public void testUpdateClient() throws ClientNotFoundException {
        ClientRequest request = ClientRequest.builder()
                .name(NOT_EXISTS_CLIENT)
                .address(clientFaker.address().fullAddress())
                .cnpj(NOT_EXISTS_CLIENT_CNPJ)
                .build();

        Response response = clientService.updateById(1L, request);

        Assert.assertEquals((Integer) HttpStatus.OK.value(), response.getStatus());
        Assert.assertEquals("Updated client with ID 1", response.getMessage());
    }

    @Test
    public void testDeleteClient() throws ClientNotFoundException {
        Response response = clientService.delete(1L);

        Assert.assertEquals((Integer) HttpStatus.OK.value(), response.getStatus());
        Assert.assertEquals("Deleted client with ID 1", response.getMessage());
    }

}
