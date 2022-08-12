package ru.job4j.gc.ref;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WeakDemo {

    public static void main(String[] args) throws InterruptedException {
        example1();
        example2();
        example3();
        myExample();
    }

    private static void example1() throws InterruptedException {
        Object object = new Object() {
            @Override
            protected void finalize() {
                System.out.println("Removed");
            }
        };
        System.out.println(object);
        WeakReference<Object> weak = new WeakReference<>(object);
        object = null;
        System.gc();
        TimeUnit.SECONDS.sleep(3);
        System.out.println(weak.get());
    }

    private static void example2() throws InterruptedException {
        List<WeakReference<Object>> objects = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            objects.add(new WeakReference<>(new Object() {
                @Override
                protected void finalize() {
                    System.out.println("Removed!");
                }
            }));
        }
        System.gc();
        TimeUnit.SECONDS.sleep(3);
    }

    private static void example3() throws InterruptedException {
        Object object = new Object() {
            @Override
            protected void finalize() {
                System.out.println("Removed");
            }
        };
        ReferenceQueue<Object> queue = new ReferenceQueue<>();
        WeakReference<Object> weak = new WeakReference<>(object, queue);
        object = null;

//        System.gc();

        TimeUnit.SECONDS.sleep(3);
        System.out.println("from link " + weak.get());
        System.out.println("from queue " + queue.poll());
    }

    private static void myExample() throws InterruptedException {
        ReferenceQueue<Object> queue = new ReferenceQueue<>();
        Object object1 = new Object();
        Object object2 = new Object();
        Object object3 = new Object();
        List<SoftReference<Object>> listSoft = new ArrayList<>();
        List<WeakReference<Object>> listWeak = new ArrayList<>();
        listSoft.add(new SoftReference<>(object1));
        listSoft.add(new SoftReference<>(object2, queue));
        listWeak.add(new WeakReference<>(object1));
        listWeak.add(new WeakReference<>(object2));
        listWeak.add(new WeakReference<>(object3, queue));

        object2 = null;
        object3 = null;

        System.gc();
        TimeUnit.SECONDS.sleep(3);

        for (SoftReference softReference : listSoft) {
            Object softReferenceObject = softReference.get();
            if (softReferenceObject != null) {
                System.out.println("SoftReference: " + softReferenceObject);
            } else {
                System.out.println("SoftReference: Object removed");
            }
        }

        for (WeakReference weakReference : listWeak) {
            Object weakReferenceObject = weakReference.get();
            if (weakReferenceObject != null) {
                System.out.println("WeakReference: " + weakReferenceObject);
            } else {
                System.out.println("WeakReference: Object removed. From queue " + queue.poll());
            }
        }
    }
}