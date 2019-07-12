package pers.dxw.thread;

import java.util.concurrent.TimeUnit;

public class ThreadStopStory {

    void normalTerminate() {
        Thread t = new Thread(() -> {
            System.out.println("Normal terminated test!");
        });
        t.start();
    }


    // Please use volatile to declare the control signal
    private volatile boolean stopSign = false;
    private Thread t1 = new Thread(() -> {
        while (!stopSign && !Thread.currentThread().isInterrupted()) {
            // Working
        }
    });

    void controlTerminate() {
        t1.start();
    }


    // InterruptedException can only used in interruptible method
    private Thread t2 = new Thread(() -> {
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("t2 interrupted!");
                break;
            }
        }
    }, "Interrupted Example Thread");

    void interruptTerminate() {
        t2.start();
    }

    private static void show(Thread t) {
        StoryUtil.show(t);
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadStopStory tss = new ThreadStopStory();
        tss.normalTerminate();


        tss.controlTerminate();
        tss.stopSign = true;
        TimeUnit.SECONDS.sleep(1);
        show(tss.t1);


        tss.interruptTerminate();
        show(tss.t2);
        tss.t2.interrupt();
        TimeUnit.SECONDS.sleep(2);
        show(tss.t2);
    }
}