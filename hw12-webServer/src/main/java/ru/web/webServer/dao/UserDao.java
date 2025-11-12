package ru.web.webServer.dao;

import java.util.List;
import java.util.Optional;
import ru.web.database.crm.model.Client;

public interface UserDao {

    Client saveClient(Client client);

    Optional<Client> findById(long id);

    List<Client> findByName(String name);

    List<Client> findAll();
}
