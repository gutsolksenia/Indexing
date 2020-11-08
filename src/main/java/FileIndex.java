import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public interface FileIndex {

    void add(@NotNull String file) throws IOException;
    List<String> find(@NotNull String key);
}
