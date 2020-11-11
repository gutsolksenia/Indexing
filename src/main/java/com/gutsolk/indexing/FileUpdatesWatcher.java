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

public class FileUpdatesWatcher {
    private static final int DELAY_IN_SECONDS = 1;

    private final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(DELAY_IN_SECONDS);
    private final Map<Path, FileTime> lastUpdate = new ConcurrentHashMap<>();
    private final Consumer<String> onDelete;
    private final Consumer<String> onUpdate;

    public FileUpdatesWatcher(@NotNull Consumer<String> onDelete,
                              @NotNull Consumer<String> onUpdate) {
        this.onDelete = onDelete;
        this.onUpdate = onUpdate;
        executor.scheduleWithFixedDelay(this::checkUpdates, DELAY_IN_SECONDS, DELAY_IN_SECONDS, SECONDS);
    }

    public void add(@NotNull File file) {
        Path path = file.toPath();
        lastUpdate.put(path, fileTime(path));
    }

    public void remove(@NotNull File file) {
        Path path = file.toPath();
        lastUpdate.remove(path);
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
        for (Path path: lastUpdate.keySet()) {
            if (!path.toFile().exists()) {
                deleted.add(path.toString());
                continue;
            }
            FileTime fileTime = fileTime(path);
            FileTime oldFileTime = lastUpdate.get(path);
            if (oldFileTime != null &&
                    fileTime.compareTo(oldFileTime) > 0) {
                updated.add(path.toString());
                lastUpdate.put(path, fileTime);
            }
        }
        deleted.forEach(name -> this.remove(new File(name)));
        for (String file : deleted) {
            onDelete.accept(file);
        }
        updated.forEach(onUpdate);
    }
}