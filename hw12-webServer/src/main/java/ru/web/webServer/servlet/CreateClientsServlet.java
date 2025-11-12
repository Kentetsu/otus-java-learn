package ru.web.webServer.servlet;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;
import ru.web.database.crm.model.Client;
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

            if (name == null || name.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("{\"error\": \"Имя клиента обязательно\"}");
                return;
            }

            Client newClient = new Client(null, name.trim());
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
