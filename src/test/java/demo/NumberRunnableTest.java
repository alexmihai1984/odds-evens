package demo;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NumberRunnableTest {

    @Test
    void testOneRun() throws InterruptedException {
        // given
        var outputList = Collections.synchronizedList(new ArrayList<Integer>());
        var lock = new Object();
        var firstLatch = new CountDownLatch(1);
        var odds = new Thread(new NumberRunnable(1, lock, true, firstLatch, outputList::add));
        var evens = new Thread(new NumberRunnable(2, lock, false, firstLatch, outputList::add));

        // when
        odds.start();
        evens.start();

        odds.join();
        evens.join();

        // then
        var expected = IntStream.range(1, 101).boxed().toList();
        assertEquals(expected, outputList);
    }

    @Test
    void testMultipleRuns() throws InterruptedException {
        for (int i = 1; i <= 10000; i++) {
            System.out.println("Run: " + i);
            testOneRun();
        }
    }
}