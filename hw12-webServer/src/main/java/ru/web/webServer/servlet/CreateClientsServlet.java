package ru.web.webServer.servlet;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import ru.web.database.crm.model.Address;
import ru.web.database.crm.model.Client;
import ru.web.database.crm.model.Phone;
import ru.web.webServer.dao.UserDao;

public class CreateClientsServlet extends HttpServlet {

    private final UserDao userDao;
    private final Gson gson;

    public CreateClientsServlet(UserDao userDao, Gson gson) {
        this.userDao = userDao;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var clients = userDao.findAll();
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().println(gson.toJson(clients));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestBody = req.getReader().lines().collect(Collectors.joining());

        try {
            var jsonObject = gson.fromJson(requestBody, java.util.Map.class);
            String name = (String) jsonObject.get("name");
            String address = (String) jsonObject.get("address");
            String phone = (String) jsonObject.get("phone");

            Client newClient = new Client(null, name.trim());

            if (address != null && !address.trim().isEmpty()) {
                Address clientAddress = new Address(null, address.trim());
                newClient.setAddress(clientAddress);
            }

            if (phone != null && !phone.trim().isEmpty()) {
                List<Phone> phones = new ArrayList<>();
                Phone clientPhone = new Phone(null, phone.trim());
                phones.add(clientPhone);
                newClient.setPhones(phones);
            }

            Client savedClient = userDao.saveClient(newClient);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().println(gson.toJson(savedClient));

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("{\"error\": \"Ошибка при создании клиента: " + e.getMessage() + "\"}");
        }
    }
}
