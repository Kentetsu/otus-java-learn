package ru.web.webServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.web.webServer.dao.UserDao;
import ru.web.webServer.dao.dbClientDao;
import ru.web.webServer.server.ClientWebServer;
import ru.web.webServer.server.UsersWebServer;
import ru.web.webServer.services.TemplateProcessor;
import ru.web.webServer.services.TemplateProcessorImpl;
import ru.web.webServer.services.UserAuthService;
import ru.web.webServer.services.UserAuthServiceImpl;

public class WebServer {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        UserDao userDao = new dbClientDao();
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        UsersWebServer usersWebServer =
                new ClientWebServer(WEB_SERVER_PORT, authService, userDao, gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
}
