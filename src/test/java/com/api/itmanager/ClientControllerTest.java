package com.api.itmanager;

import com.api.itmanager.modules.client.controller.ClientController;
import com.api.itmanager.modules.client.dto.ClientRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Locale;

@TestPropertySource(locations="classpath:test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientControllerTest extends ApiItmanagerApplicationTests {

    private MockMvc mockMvc;

    private Faker clientFaker;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientController clientController;

    @BeforeAll
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
        this.clientFaker = new Faker(new Locale("pt-BR"));
    }

    @Test
    public void testListAll() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/client")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testCreateClient() throws Exception {
        ClientRequest request = createClientRequestFaker();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/client")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    private ClientRequest createClientRequestFaker() {
        return ClientRequest.builder()
                .name(clientFaker.company().name())
                .address(clientFaker.address().fullAddress())
                .cnpj(clientFaker.numerify("##############"))
                .build();
    }

    @Test
    public void testCreateClientWithError() throws Exception {
        ClientRequest request = createClientRequestFaker();

        clientController.createClient(request);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/client")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testFindByIdIfClientExists() throws Exception {
        ClientRequest request = createClientRequestFaker();

        clientController.createClient(request);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/client/1")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testFindByIdIfClientNotExists() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/client/25")).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testUpdateClient() throws Exception{
        ClientRequest request = createClientRequestFaker();

        clientController.createClient(request);

        ClientRequest requestToUpdate = createClientRequestFaker();

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/client/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestToUpdate)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUpdateClientWithError() throws Exception{
        ClientRequest request = createClientRequestFaker();

        clientController.createClient(request);

        ClientRequest request2 = createClientRequestFaker();

        clientController.createClient(request2);

        request.setName(request2.getName());

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/client/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testDeleteByIdClientIfExists() throws Exception {
        ClientRequest request = createClientRequestFaker();

        clientController.createClient(createClientRequestFaker());

        clientController.createClient(createClientRequestFaker());

        clientController.createClient(request);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/client/3"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeleteByIdClientIfNotExists() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/client/25"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}