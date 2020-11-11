package com.gutsolk.indexing;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MapFileIndex implements FileIndex {
    private final Map<String, Set<String>> index = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> filesToKeys = new ConcurrentHashMap<>();
    private final FileUpdatesWatcher fileUpdatesWatcher = new FileUpdatesWatcher(this::remove, this::update);

    private final KeyExtractor keyExtractor;

    public MapFileIndex() {
        this(new SplitKeyExtractor());
    }

    public MapFileIndex(@NotNull KeyExtractor keyExtractor) {
        this.keyExtractor = keyExtractor;
    }

    @Override
    public void add(@NotNull String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            addDirectory(file);
        } else {
            fileUpdatesWatcher.add(file);
            Set<String> keys = keyExtractor.extract(path);
            for (String key : keys) {
                index.computeIfAbsent(key, k -> ConcurrentHashMap.newKeySet())
                        .add(path);
                filesToKeys.put(path, keys);
            }
        }
    }

    @Override
    public List<String> get(@NotNull String key) {
        return ImmutableList.copyOf(index.getOrDefault(key, Collections.emptySet()));
    }

    @Override
    public boolean remove(@NotNull String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            return removeDirectory(file);
        }
        Set<String> keys = filesToKeys.remove(path);
        if (keys == null || keys.isEmpty()) {
            return false;
        }
        for (String key : keys) {
            Set<String> files = index.get(key);
            if (files != null) {
                files.remove(path);
            }
            if (files.isEmpty()) {
                index.remove(key);
            }
        }
        fileUpdatesWatcher.remove(file);
        return true;
    }

    @Override
    public void stop() {
        fileUpdatesWatcher.stop();
    }

    private void update(@NotNull String file) {
        remove(file);
        add(file);
    }

    private void addDirectory(File dir) {
        for (File file : dir.listFiles()) {
            add(file.getPath());
        }
    }

    private boolean removeDirectory(File dir) {
        boolean res = false;
        for (File file : dir.listFiles()) {
            res |= remove(file.getPath());
        }
        return res;
    }

    @Override
    public void run() {
        fileUpdatesWatcher.run();
    }


}
