package ru.web.database.crm.service;

import java.util.List;
import java.util.Optional;
import ru.web.database.crm.model.Client;

public interface DBServiceClient {

    Client saveClient(Client client);

    Optional<Client> getClient(long id);

    List<Client> findByEntityField(String fieldName, Object fieldValue);

    List<Client> findAll();
}
