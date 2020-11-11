package com.gutsolk.indexing;

import org.jetbrains.annotations.NotNull;

public class TestBase {
    @NotNull
    String getFile(@NotNull String filename)  {
        return getClass().getClassLoader().getResource(filename).getFile();
    }
}
