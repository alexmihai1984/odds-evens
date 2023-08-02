package demo;

import java.util.concurrent.CountDownLatch;

public class Main {
    public static void main(String[] args) {
        var lock = new Object();
        var firstLatch = new CountDownLatch(1);
        var odds = new Thread(new NumberRunnable(1, lock, true, firstLatch, System.out::println));
        var evens = new Thread(new NumberRunnable(2, lock, false, firstLatch, System.out::println));

        odds.start();
        evens.start();
    }
}
