package com.gutsolk.indexing;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FileUpdatesWatcherImpl extends FileUpdatesWatcher {
    private final Map<Path, FileTime> lastUpdate = new HashMap<>();

    public FileUpdatesWatcherImpl(@NotNull Consumer<String> onDelete, @NotNull Consumer<String> onChange) {
        super(onDelete, onChange);
    }


    @Override
    public void add(@NotNull File file) {
        Path path = file.toPath();
        lastUpdate.put(path, fileTime(path));
    }

    @Override
    public void remove(@NotNull File file) {
        Path path = file.toPath();
        lastUpdate.remove(path);
    }

    @Override
    public void run() {
        while (!isStopped()) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException exception) {
                return;
            }
            checkUpdates();
        }
    }

    private static FileTime fileTime(Path path) {
        try {
            return Files.getLastModifiedTime(path);
        } catch (IOException e) {
            return null; //TODO
        }
    }

    private void checkUpdates() {
        List<String> toRemove = new ArrayList<>();
        List<String> toChange = new ArrayList<>();
        for (Path path: lastUpdate.keySet()) {
            FileTime fileTime = fileTime(path);
            if (!path.toFile().exists()) {
                toRemove.add(path.toString());
            } else if (fileTime.compareTo(lastUpdate.get(path)) > 0) {
                toChange.add(path.toString());
            }
        }
        toRemove.forEach(onDelete);
        toChange.forEach(onChange);
    }
}