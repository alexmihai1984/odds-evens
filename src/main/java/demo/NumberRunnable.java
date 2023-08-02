package demo;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public class NumberRunnable implements Runnable {

    private final int start;
    private final Object lock;
    private final boolean isFirst;
    private final CountDownLatch firstLatch;
    private final Consumer<Integer> outputConsumer;

    public NumberRunnable(
            int start,
            Object lock,
            boolean isFirst,
            CountDownLatch firstLatch,
            Consumer<Integer> outputConsumer
    ) {
        this.start = start;
        this.lock = lock;
        this.isFirst = isFirst;
        this.firstLatch = firstLatch;
        this.outputConsumer = outputConsumer;
    }

    @Override
    public void run() {
        waitForFirst();

        synchronized (lock) {
            for (int i = start; i <= 100; i += 2) {
                outputConsumer.accept(i);
                firstDone(i);


                lock.notify();

                if (i < 99) {
                    waitForOther();
                }
            }
        }
    }

    private void waitForOther() {
        try {
            lock.wait();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private void firstDone(int i) {
        if (isFirst && i == start) {
            firstLatch.countDown();
        }
    }

    private void waitForFirst() {
        if (!isFirst) {
            try {
                firstLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }
}
