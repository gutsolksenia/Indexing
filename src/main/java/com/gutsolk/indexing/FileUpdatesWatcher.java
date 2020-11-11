package com.gutsolk.indexing;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

import static java.nio.file.Files.getLastModifiedTime;
import static java.util.concurrent.TimeUnit.SECONDS;

public class FileUpdatesWatcher implements Runnable {
    volatile boolean isRunning = false;

    private static final int DELAY_IN_SECONDS = 1;

    private final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(DELAY_IN_SECONDS);
    private final Map<Path, FileTime> fileToLastModified = new ConcurrentHashMap<>();
    private final Consumer<String> onDelete;
    private final Consumer<String> onUpdate;

    public FileUpdatesWatcher(@NotNull Consumer<String> onDelete,
                              @NotNull Consumer<String> onUpdate) {
        this.onDelete = onDelete;
        this.onUpdate = onUpdate;
    }

    public void add(@NotNull File file) {
        Path path = file.toPath();
        fileToLastModified.put(path, fileTime(path));
    }

    public void remove(@NotNull File file) {
        Path path = file.toPath();
        fileToLastModified.remove(path);
    }

    private static FileTime fileTime(Path path) {
        try {
            return getLastModifiedTime(path);
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
    }

    private void checkUpdates() {
        List<String> deleted = new ArrayList<>();
        List<String> updated = new ArrayList<>();
        for (Path path: fileToLastModified.keySet()) {
            if (!path.toFile().exists()) {
                deleted.add(path.toString());
                continue;
            }
            FileTime fileTime = fileTime(path);
            FileTime oldFileTime = fileToLastModified.get(path);
            if (oldFileTime != null &&
                    fileTime.compareTo(oldFileTime) > 0) {
                updated.add(path.toString());
                fileToLastModified.put(path, fileTime);
            }
        }
        deleted.forEach(name -> this.remove(new File(name)));
        for (String file : deleted) {
            onDelete.accept(file);
        }
        updated.forEach(onUpdate);
    }

    @Override
    public void run() {
        if (!isRunning) {
            executor.scheduleWithFixedDelay(this::checkUpdates, DELAY_IN_SECONDS, DELAY_IN_SECONDS, SECONDS);
        }
        isRunning = true;
    }

    public void stop() {
        executor.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!executor.awaitTermination(60, SECONDS)) {
                executor.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!executor.awaitTermination(60, SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            executor.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();

        }
        isRunning = false;
    }
}