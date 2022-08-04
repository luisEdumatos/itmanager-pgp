package com.api.itmanager.modules.client.dto;

import com.api.itmanager.modules.client.model.Client;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class ClientResponse {

    private Long id;
    private String name;
    private String cnpj;
    private String address;

    public static ClientResponse of(Client client) {
        var response = new ClientResponse();
        BeanUtils.copyProperties(client, response);
        return  response;
    }
}
