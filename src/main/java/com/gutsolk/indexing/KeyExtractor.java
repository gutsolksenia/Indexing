package com.gutsolk.indexing;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface KeyExtractor {

    @NotNull
    Set<String> extract(@NotNull String file) throws Exception;

}