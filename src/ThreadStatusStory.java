package pers.dxw.thread;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Thread status includes NEW,RUNNABLE,BLOCKED,WAITING,TIMED_WAITING,TERMINATED
 */
public class ThreadStatusStory {

    static void sample() {
        Thread tSample = new Thread(() -> {
            Stream.of(1, 2, 3, 4, 5, 6).collect(Collectors.toList()).forEach(x -> System.out.print(x));
            System.out.println();
        });
        tSample.start();
    }

    static void stateNew() {
        // New
        Thread tNew = new Thread("tNew");
        show(tNew);
    }

    static void stateRunAndTerminated() throws InterruptedException {
        // RUNNABLE
        Thread tRun = new Thread(() -> {
            show(Thread.currentThread());
        }, "tRun");
        tRun.start();
        TimeUnit.SECONDS.sleep(1);
        // TERMINATED
        show(tRun);
    }

    static void stateBlockAndTimeWait() throws InterruptedException {
        // BLOCKED
        String sig = "signal";
        Thread tTimeWait = new Thread(() -> {
            synchronized (sig) {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "tTimeWait");
        tTimeWait.start();
        TimeUnit.SECONDS.sleep(1);
        Thread tBlocked = new Thread(() -> {
            synchronized (sig) {
                System.out.println("Thread <" + Thread.currentThread().getName() + "> Block finished!");
            }
        }, "tBlocked");
        tBlocked.start();
        TimeUnit.SECONDS.sleep(1);
        show(tTimeWait);
        show(tBlocked);
    }

    static void stateWaiting() throws InterruptedException {
        // WAITING
        String sig = "signal";
        Thread tWait = new Thread(() -> {
            synchronized (sig) {
                try {
                    sig.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "tWait");

        new Thread(() -> {
            while (true) {
                if (tWait.getState().equals(Thread.State.WAITING)) {
                    System.out.println(tWait.getName() + " is WAITING");
                    break;
                }
            }
        }).start();

        new Thread(() -> {
            while (true) {
                if (tWait.getState().equals(Thread.State.BLOCKED)) {
                    System.out.println(tWait.getName() + " is BLOCKED");
                    break;
                }
            }
        }).start();

        tWait.start();
        TimeUnit.SECONDS.sleep(2);
        new Thread(() -> {
            synchronized (sig) {
                sig.notify();
            }
        }).start();
    }

    public static void main(String[] args) throws InterruptedException {
        sample();
        stateNew();
        stateRunAndTerminated();
        stateBlockAndTimeWait();
        stateWaiting();
    }

    private static void show(Thread t) {
        StoryUtil.show(t);
    }
}