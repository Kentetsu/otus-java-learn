package ru.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Threads {
    private static final Logger logger = LoggerFactory.getLogger(Threads.class);
    private boolean directionFlag = false;
    private boolean orderFlag = true;
    private int value = 2;
    private final int step = 1;

    private synchronized void action(boolean isFirst, boolean isIncrementing) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (orderFlag != isFirst) {
                    this.wait();
                }

                if (isIncrementing) {
                    value += directionFlag ? step : -step;
                }

                logger.info(String.valueOf(value));
                {
                    if ((value <= 1 || value >= 10) && !isIncrementing) {
                        directionFlag = !directionFlag;
                    }
                }
                orderFlag = !orderFlag;

                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        Threads thread = new Threads();
        new Thread(() -> thread.action(true, true)).start();
        new Thread(() -> thread.action(false, false)).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
