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

public class FileUpdatesWatcher {
    private static final int TIMEOUT_IN_SECONDS = 1;

    private final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);
    private final Consumer<String> onDelete;
    private final Consumer<String> onChange;
    private final Map<Path, FileTime> lastUpdate = new ConcurrentHashMap<>();

    public FileUpdatesWatcher(@NotNull Consumer<String> onDelete,
                              @NotNull Consumer<String> onChange) {
        this.onDelete = onDelete;
        this.onChange = onChange;
        executor.scheduleWithFixedDelay(this::checkUpdates, 0, TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
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
        List<String> toRemove = new ArrayList<>();
        List<String> toChange = new ArrayList<>();
        for (Path path: lastUpdate.keySet()) {
            if (!path.toFile().exists()) {
                toRemove.add(path.toString());
                continue;
            }
            FileTime fileTime = fileTime(path);
            if (fileTime.compareTo(lastUpdate.get(path)) > 0) {
                toChange.add(path.toString());
                lastUpdate.put(path, fileTime);
            }
        }
        toRemove.forEach(name -> this.remove(new File(name)));
        toRemove.forEach(onDelete);
        toChange.forEach(onChange);
    }
}