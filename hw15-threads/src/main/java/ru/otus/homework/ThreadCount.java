package ru.otus.homework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadCount {
    private static final Logger logger = LoggerFactory.getLogger(ThreadCount.class);
    private String last = "Thread1";
    private String direction = "UP";
    private int count = 1;

    public static void main(String[] args) {
        ThreadCount treadCount = new ThreadCount();
        new Thread(() -> treadCount.action("Thread0")).start();
        new Thread(() -> treadCount.action("Thread1")).start();
    }

    private synchronized void action(String message) {

        while (!Thread.currentThread().isInterrupted()) {

            try {
                while (last.equals(message)) {
                    this.wait();
                }

                last = message;

                logger.info(message + ": " + count);

                if (message.equals("Thread1")) {
                    if (count == 10) {
                        direction = "DOWN";
                    } else if (count == 1) {
                        direction = "UP";
                    }
                    changeCount(direction);
                }

                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void changeCount(String direction) {
        if (direction.equals("UP")) {
            count++;
        } else {
            count--;
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
