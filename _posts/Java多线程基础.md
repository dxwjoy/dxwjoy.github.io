---
layout: post
title: Java多线程基础
---


## 线程状态

```java
package pers.dxw.thread;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Thread status includes NEW,RUNNABLE,BLOCKED,WAITING,TIMED_WAITING,TERMINATED
 */
public class ThreadStatusStory {

    private static volatile boolean stopSign = false;

    public static void main(String[] args) throws InterruptedException {
        Thread tSample = new Thread(()->{
            Stream.of(1,2,3,4,5,6).collect(Collectors.toList()).forEach(x->System.out.print(x));
            System.out.println();
        });
        tSample.start();


        // New
        Thread tNew = new Thread("tNew");
        show(tNew);


        // RUNNABLE
        Thread tRun = new Thread(()->{
            show(Thread.currentThread());
        }, "tRun");
        tRun.start();


        // BLOCKED
        String sig = "signal";
        Thread tTimeWait = new Thread(()->{
            synchronized (sig){
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"tTimeWait");
        tTimeWait.start();
        Thread tBlocked = new Thread(()->{
            synchronized (sig){
                System.out.println("Thread <"+ Thread.currentThread().getName()+ "> Block finished!");
            }
        },"tBlocked");
        tBlocked.start();
        TimeUnit.SECONDS.sleep(1);
        show(tTimeWait);
        show(tBlocked);


        // WAITING
        String sig2="signal";
        Thread tWait = new Thread(()->{
            synchronized (sig2){
                try {
                    sig2.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"tWait");
        new Thread(()->{while(true){
            if(tWait.getState().equals(Thread.State.WAITING)){
                show(tWait);
                break;
            }
        }}).start();
        tWait.start();
//        show(tWait);
        synchronized (sig2){
            sig2.notify();
        }


        // TERMINATED
        show(tRun);
    }

    public static void show(Thread t){
        System.out.println("The thread <" + t.getName() + "> state is " + t.getState());
    }

}
```

