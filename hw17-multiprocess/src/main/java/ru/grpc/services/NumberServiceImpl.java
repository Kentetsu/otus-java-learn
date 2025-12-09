package ru.grpc.services;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.grpc.NumberRangeRequest;
import ru.grpc.NumberResponse;
import ru.grpc.NumberServiceGrpc;

public class NumberServiceImpl extends NumberServiceGrpc.NumberServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(NumberServiceImpl.class);

    private final ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();

    @Override
    public void getNumberStream(NumberRangeRequest request, StreamObserver<NumberResponse> responseObserver) {

        int firstValue = request.getFirstValue();
        int lastValue = request.getLastValue();

        logger.info("Starting number stream from {} to {}", firstValue, lastValue);

        virtualThreadExecutor.execute(() -> streamNumbers(firstValue, lastValue, responseObserver));
    }

    private void streamNumbers(int firstValue, int lastValue, StreamObserver<NumberResponse> responseObserver) {
        try {
            for (int currentNumber = firstValue; currentNumber <= lastValue; currentNumber++) {
                sendNumberWithDelay(responseObserver, currentNumber);
            }

            responseObserver.onCompleted();
            logger.info("Number stream completed");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Number stream interrupted");
            responseObserver.onError(Status.CANCELLED
                    .withDescription("Stream was interrupted")
                    .withCause(e)
                    .asRuntimeException());
        } catch (Exception e) {
            logger.error("Error in number stream: {}", e.getMessage());
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal server error")
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    private void sendNumberWithDelay(StreamObserver<NumberResponse> responseObserver, int number)
            throws InterruptedException {
        NumberResponse response = NumberResponse.newBuilder().setValue(number).build();

        logger.info("Sending number: {}", number);
        responseObserver.onNext(response);

        TimeUnit.MILLISECONDS.sleep(2000);
    }

    public void shutdown() {
        virtualThreadExecutor.shutdown();
        try {
            if (!virtualThreadExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
                virtualThreadExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            virtualThreadExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
