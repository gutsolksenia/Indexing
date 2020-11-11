package com.gutsolk.indexing;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface FileIndex {

    void add(@NotNull String path);
    List<String> find(@NotNull String key);
    boolean remove(@NotNull String file);
}