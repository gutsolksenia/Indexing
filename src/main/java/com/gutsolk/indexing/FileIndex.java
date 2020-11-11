package com.gutsolk.indexing;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface FileIndex {

    void add(@NotNull String file) throws Exception;
    List<String> find(@NotNull String key);
    boolean remove(@NotNull String file);
}
