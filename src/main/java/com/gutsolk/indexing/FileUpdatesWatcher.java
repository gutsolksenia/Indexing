package com.gutsolk.indexing;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;


public abstract class FileUpdatesWatcher extends Thread {

    private final AtomicBoolean stop = new AtomicBoolean(false);
    protected final Set<File> files = ConcurrentHashMap.newKeySet();
    protected final Consumer<String> onDelete;
    protected final Consumer<String> onChange;

    public FileUpdatesWatcher(@NotNull Consumer<String> onDelete,
                              @NotNull Consumer<String> onChange) {
        this.onDelete = onDelete;
        this.onChange = onChange;
    }

    public abstract void add(@NotNull File file) throws Exception;
    public abstract void remove(@NotNull File file);

    public boolean isStopped() {
        return stop.get();
    }

    public void stopThread() {
        stop.set(true);
    }
}