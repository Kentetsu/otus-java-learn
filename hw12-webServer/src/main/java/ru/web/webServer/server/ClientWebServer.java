package ru.web.webServer.server;

import com.google.gson.Gson;
import java.util.Arrays;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import ru.web.webServer.dao.UserDao;
import ru.web.webServer.helpers.FileSystemHelper;
import ru.web.webServer.services.TemplateProcessor;
import ru.web.webServer.services.UserAuthService;
import ru.web.webServer.servlet.AuthorizationFilter;
import ru.web.webServer.servlet.ClientsServlet;
import ru.web.webServer.servlet.CreateClientsServlet;
import ru.web.webServer.servlet.LoginServlet;

public class ClientWebServer implements UsersWebServer {
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";

    private final UserDao userDao;
    private final Gson gson;
    protected final TemplateProcessor templateProcessor;
    private final Server server;
    private final UserAuthService authService;

    public ClientWebServer(
            int port, UserAuthService authService, UserDao userDao, Gson gson, TemplateProcessor templateProcessor) {
        this.userDao = userDao;
        this.gson = gson;
        this.templateProcessor = templateProcessor;
        server = new Server(port);
        this.authService = authService;
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().isEmpty()) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private void initContext() {

        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        Handler.Sequence sequence = new Handler.Sequence();
        sequence.addHandler(resourceHandler);
        sequence.addHandler(applySecurity(servletContextHandler, "/clients", "/api/clients"));

        server.setHandler(sequence);
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths)
                .forEachOrdered(
                        path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirAllowed(false);
        resourceHandler.setWelcomeFiles(START_PAGE_NAME);
        resourceHandler.setBaseResourceAsString(
                FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new ClientsServlet(templateProcessor, userDao)), "/clients");
        servletContextHandler.addServlet(new ServletHolder(new CreateClientsServlet(userDao, gson)), "/api/clients");
        return servletContextHandler;
    }
}
