package ru.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GRPCClient implements NumberObserver {
    private static final Logger logger = LoggerFactory.getLogger(GRPCClient.class);

    private final ManagedChannel channel;
    private final NumberServiceGrpc.NumberServiceStub asyncStub;
    private final AtomicInteger currentValue = new AtomicInteger(0);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final CountDownLatch finishLatch = new CountDownLatch(1);

    public GRPCClient(String host, int port) {
        this.channel =
                ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.asyncStub = NumberServiceGrpc.newStub(channel);
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }

        if (channel != null) {
            channel.shutdown();
            try {
                if (!channel.awaitTermination(5, TimeUnit.SECONDS)) {
                    channel.shutdownNow();
                }
            } catch (InterruptedException e) {
                channel.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void onNumberReceived(int number) {
        int newCurrentValue = currentValue.updateAndGet(value -> value + number + 1);
        logger.info("new value:{} -> currentValue:{}", number, newCurrentValue);
    }

    public void startClientLogic() throws InterruptedException {
        logger.info("Numbers Client is starting...");

        startServerStream();

        startClientLoop();

        try {
            if (!finishLatch.await(60, TimeUnit.SECONDS)) {
                logger.warn("Client timed out");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Client was interrupted");
        }
    }

    private void startServerStream() {
        NumberRangeRequest request = NumberRangeRequest.newBuilder()
                .setFirstValue(0)
                .setLastValue(30)
                .build();

        StreamObserver<NumberResponse> responseObserver = new StreamObserver<>() {

            @Override
            public void onNext(NumberResponse response) {
                int newValue = response.getValue();
                onNumberReceived(newValue);
            }

            @Override
            public void onError(Throwable t) {
                logger.warn("Server stream failed: {}", t.getMessage());
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                logger.info("Server stream completed");
                finishLatch.countDown();
            }
        };

        asyncStub.getNumberStream(request, responseObserver);
    }

    private void startClientLoop() {
        final AtomicInteger iteration = new AtomicInteger(0);

        scheduler.scheduleAtFixedRate(
                () -> {
                    int iter = iteration.getAndIncrement();
                    if (iter > 50) {
                        scheduler.shutdown();
                        finishLatch.countDown();
                        return;
                    }

                    int newValue = currentValue.updateAndGet(value -> value + 1);
                    logger.info("currentValue:{}", newValue);
                },
                0,
                1,
                TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        GRPCClient client = new GRPCClient("localhost", 8190);

        try {
            client.startClientLogic();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Main thread was interrupted");
        } finally {
            client.shutdown();
        }
    }
}
