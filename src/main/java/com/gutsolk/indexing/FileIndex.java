package com.gutsolk.indexing;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface FileIndex extends Runnable{

    void add(@NotNull String path);
    List<String> get(@NotNull String key);
    boolean remove(@NotNull String file);
    void stop();
}