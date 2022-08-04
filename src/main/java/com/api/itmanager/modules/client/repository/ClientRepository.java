package com.api.itmanager.modules.client.repository;

import com.api.itmanager.modules.client.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByNameIgnoreCaseContaining(String name);

    Optional<Client> findByCnpj(String cnpj);

    boolean existsByNameIgnoreCaseContaining(String name);

    boolean existsByCnpj(String cnpj);
}