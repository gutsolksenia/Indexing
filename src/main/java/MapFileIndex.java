import org.jetbrains.annotations.NotNull;
import com.google.common.collect.ImmutableList;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MapFileIndex implements FileIndex {
    private final Map<String, Set<String>> index = new ConcurrentHashMap<>();
    private final KeyExtractor keyExtractor;

    public MapFileIndex(@NotNull KeyExtractor keyExtractor) {
        this.keyExtractor = keyExtractor;
    }

    @Override
    public void add(@NotNull String file) throws Exception {
        Set<String> keys = keyExtractor.extract(file);
        for (String key : keys) {
            index.computeIfAbsent(key, k -> ConcurrentHashMap.newKeySet())
                    .add(file);
        }
    }

    @Override
    public List<String> find(@NotNull String key) {
        return ImmutableList.copyOf((index.getOrDefault(key, Collections.emptySet())));
    }
}
