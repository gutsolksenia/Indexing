import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MapFileIndex implements FileIndex {
    private final Map<String, List<String>> index = new ConcurrentHashMap<>();
    private final KeyExtractor keyExtractor;

    public MapFileIndex(@NotNull KeyExtractor keyExtractor) {
        this.keyExtractor = keyExtractor;
    }

    @Override
    public void add(@NotNull String file) throws IOException {
        Set<String> keys = keyExtractor.extract(file);
        for (String key : keys) {
            index.computeIfAbsent(key, k -> new ArrayList<>())
                    .add(file);
        }
    }

    @Override
    public List<String> find(@NotNull String key) {
        return index.getOrDefault(key, Collections.emptyList());
    }
}
