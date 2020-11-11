package com.gutsolk.indexing;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface KeyExtractor {

    /**
     * This method can be called from several threads concurrently, implementation should be thread safe
     */
    @NotNull
    Set<String> extract(@NotNull String file) throws Exception;

}
