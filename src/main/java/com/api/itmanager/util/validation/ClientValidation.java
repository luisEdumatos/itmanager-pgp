package com.api.itmanager.util.validation;

import com.api.itmanager.modules.client.dto.ClientRequest;
import com.api.itmanager.modules.client.dto.ClientResponse;
import com.api.itmanager.util.exception.ValidationException;

import static org.springframework.util.ObjectUtils.isEmpty;

public class ClientValidation {
    private static final int CNPJ_SIZE = 14;
    private static final String NUMERIC_MATCH = "[+-]?\\d*(\\.\\d+)?";

    public static void clientCreateValidation(ClientRequest request, boolean existsByName, boolean existsByCnpj) {
        clientGeneralValidation(request);

        if (existsByName) {
            throw new ValidationException("The client's name already exists and cannot be repeated. ");
        }

        if (existsByCnpj) {
            throw new ValidationException("The client's CNPJ already exists and cannot be repeated. ");
        }
    }

    public static void clientUpdateValidation(ClientRequest request, ClientResponse response, boolean existsByName, boolean existsByCnpj) {
        clientGeneralValidation(request);

        if (!request.getName().equalsIgnoreCase(response.getName())) {
            if (existsByName) {
                throw new ValidationException("The client's name already exists and cannot be repeated. ");
            }
        }

        if (!request.getCnpj().equals(response.getCnpj())) {
            if (existsByCnpj) {
                throw new ValidationException("The client's CNPJ already exists and cannot be repeated. ");
            }
        }
    }

    private static void clientGeneralValidation(ClientRequest request) {
        if (isEmpty(request.getName())) {
            throw new ValidationException("The client's name was not informed.");
        }

        if (isEmpty(request.getCnpj())) {
            throw new ValidationException("The client's CNPJ was not informed.");
        }

        if (request.getCnpj().length() != CNPJ_SIZE) {
            throw new ValidationException("The client's CNPJ must have exactly 14 digits.");
        }

        if (!request.getCnpj().matches(NUMERIC_MATCH)) {
            throw new ValidationException("The client's CNPJ must contain only numbers. ");
        }

        if (isEmpty(request.getAddress())) {
            throw new ValidationException("The client's address was not informed.");
        }
    }

}
