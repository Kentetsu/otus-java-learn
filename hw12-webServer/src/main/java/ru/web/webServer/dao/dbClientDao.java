package ru.web.webServer.dao;

import java.util.List;
import java.util.Optional;
import ru.web.database.crm.model.Client;
import ru.web.database.crm.service.DBServiceClient;
import ru.web.database.dbInit;

public class dbClientDao implements UserDao {
    private final DBServiceClient dbServiceClient;

    public dbClientDao() {
        dbInit tempInit = new dbInit();
        this.dbServiceClient = tempInit.getServiceClient();
        createDefaultAdmin();
    }

    private void createDefaultAdmin() {
        List<Client> existingAdmins = dbServiceClient.findByEntityField("isAdmin", true);
        if (existingAdmins.isEmpty()) {
            Client admin = new Client(null, "admin", "admin123", true);
            dbServiceClient.saveClient(admin);
            System.out.println("Создан администратор по умолчанию: admin / admin123");
        }
    }

    @Override
    public Client saveClient(Client client) {
        return dbServiceClient.saveClient(client);
    }

    @Override
    public Optional<Client> findById(long id) {
        return dbServiceClient.getClient(id);
    }

    @Override
    public List<Client> findByName(String name) {
        return dbServiceClient.findByEntityField("name", name);
    }

    @Override
    public List<Client> findAll() {
        return dbServiceClient.findAll();
    }
}
