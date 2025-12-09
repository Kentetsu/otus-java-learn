package ru.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.grpc.services.NumberServiceImpl;

public class GRPCServer {
    private static final Logger logger = LoggerFactory.getLogger(GRPCServer.class);
    public static final int SERVER_PORT = 8190;
    private Server server;
    private NumberServiceImpl numberService;

    private void start() throws IOException {
        numberService = new NumberServiceImpl();
        server = ServerBuilder.forPort(SERVER_PORT)
                .addService(numberService)
                .build()
                .start();

        logger.info("Server started, listening on {}", SERVER_PORT);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.error("*** shutting down gRPC server since JVM is shutting down");
            try {
                this.stop();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Server shutdown was interrupted", e);
            }
            logger.error("*** server shut down");
        }));
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
        if (numberService != null) {
            numberService.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final GRPCServer server = new GRPCServer();
        server.start();
        server.blockUntilShutdown();
    }
}
