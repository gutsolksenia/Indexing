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
    public void add(@NotNull String file) throws Exception {
        fileUpdatesWatcher.add(new File(file));
        Set<String> keys = keyExtractor.extract(file);
        for (String key : keys) {
            index.computeIfAbsent(key, k -> ConcurrentHashMap.newKeySet())
                    .add(file);
            filesToKeys.put(file, keys);
        }
    }

    @Override
    public List<String> find(@NotNull String key) {
        return ImmutableList.copyOf(index.getOrDefault(key, Collections.emptySet()));
    }

    @Override
    public boolean remove(@NotNull String file) {
        Set<String> keys = filesToKeys.remove(file);
        if (keys == null || keys.isEmpty()) {
            return false;
        }
        for (String key : keys) {
            Set<String> files = index.get(key);
            if (files != null) {
                files.remove(file);
            }
        }
        fileUpdatesWatcher.remove(new File(file));
        return true;
    }

    private void update(@NotNull String file) {
        remove(file);
        try {
            add(file);
        } catch (Exception e) {
            //TODO
        }

    }
}
