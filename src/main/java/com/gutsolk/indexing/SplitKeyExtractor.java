package com.gutsolk.indexing;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SplitKeyExtractor implements KeyExtractor {
    private static final String WHITESPACES = "\\s+";
    private final String regexp;

    public SplitKeyExtractor() {
        this(WHITESPACES);
    }

    public SplitKeyExtractor(@NotNull String regexp) {
        this.regexp = regexp;
    }

    @NotNull
    @Override
    public Set<String> extract(@NotNull String file) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            Set<String> keys = new HashSet<>();
            try {
                while ((line = reader.readLine()) != null) {
                    keys.addAll(Arrays.asList(line.split(regexp)));
                }
                return keys;
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
