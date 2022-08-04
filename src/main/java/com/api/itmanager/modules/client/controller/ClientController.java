package com.api.itmanager.modules.client.controller;

import com.api.itmanager.modules.client.dto.ClientRequest;
import com.api.itmanager.modules.client.dto.ClientResponse;
import com.api.itmanager.modules.client.service.ClientService;
import com.api.itmanager.util.exception.ClientNotFoundException;
import com.api.itmanager.util.response.Response;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/client")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ClientController {

    private ClientService clientService;

    @ApiOperation(value = "Retorna a lista de clientes existentes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna a lista de clientes, caso não existir, retorna lista vazia")
    })
    @GetMapping(produces = "application/json")
    public List<ClientResponse> listAll() {
        return clientService.listAll();
    }

    @ApiOperation(value = "Retorna o cliente informado por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna o cliente referente ao ID informado"),
            @ApiResponse(code = 400, message = "Erro de passagem de parâmetro"),
            @ApiResponse(code = 404, message = "Cliente não encontrado para o ID informado"),
    })
    @GetMapping(value = "/{id}", produces = "application/json")
    public ClientResponse findById(@PathVariable Long id) throws ClientNotFoundException {
        return clientService.findById(id);
    }

    @ApiOperation(value = "Cria um novo cliente")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Cliente criado com sucesso"),
            @ApiResponse(code = 400, message = "Erro na validação dos campos informados"),
    })
    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Response createClient(@RequestBody @Valid ClientRequest request) {
        return clientService.createClient(request);
    }

    @ApiOperation(value = "Atualiza dados de um cliente existente")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cliente atualizado com sucesso"),
            @ApiResponse(code = 400, message = "Erro de passagem de parâmetro ou na validação dos campos"),
            @ApiResponse(code = 405, message = "Falta de ID no parâmetro"),
            @ApiResponse(code = 404, message = "Cliente não encontrado para o ID informado"),
    })
    @PutMapping(value = "/{id}", produces = "application/json")
    public Response updateById(@PathVariable Long id, @RequestBody @Valid ClientRequest request) throws ClientNotFoundException {
        return clientService.updateById(id, request);
    }

    @ApiOperation(value = "Deleta cliente informado pelo ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cliente deletado com sucesso"),
            @ApiResponse(code = 405, message = "Falta de ID no parâmetro"),
            @ApiResponse(code = 404, message = "Cliente não encontrado para o ID informado"),
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteById(@PathVariable Long id) throws ClientNotFoundException {
        return clientService.delete(id);
    }
}
