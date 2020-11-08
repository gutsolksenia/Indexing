import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.util.Set;

public interface KeyExtractor {

    @NotNull
    Set<String> extract(@NotNull String file) throws IOException;

}
