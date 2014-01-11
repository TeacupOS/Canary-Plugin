package com.teacupos.tapps.canary.util;

public abstract class PRunnable<T> implements Runnable {
    private final T t;

    public PRunnable(T instance) {
        t = instance;
    }

    @Override
    public void run() {
        run(t);
    }

    public abstract void run(T t);

}
