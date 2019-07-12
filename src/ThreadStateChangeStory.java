package pers.dxw.thread;

import java.util.concurrent.TimeUnit;

import static pers.dxw.thread.StoryUtil.show;

public class ThreadStateChangeStory {

    static void twaitToRun() {
        new Thread(() -> {
            try {
                TimeUnit.HOURS.sleep(1);
            } catch (InterruptedException e) {
                show(Thread.currentThread());
            }
        }, "LongSleepThread").start();


        ThreadGroup tg = Thread.currentThread().getThreadGroup().getParent();
        Thread[] tArray = new Thread[tg.activeCount()];
        tg.enumerate(tArray);
        for (Thread t : tArray) {
            if (t.getName().equals("LongSleepThread")) {
                t.interrupt();
            }
        }
    }

    static void blockToRun() throws InterruptedException {
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

        tWait.start();
        TimeUnit.MICROSECONDS.sleep(200);
        show(tWait);
        tWait.interrupt();
        TimeUnit.SECONDS.sleep(1);
        show(tWait);
    }

    public static void main(String[] args) throws InterruptedException {
        twaitToRun();
        blockToRun();
    }
}
