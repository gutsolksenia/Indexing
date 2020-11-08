import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MapFileIndexTest {
    @Test
    public void simpleTest() throws IOException {
        FileIndex fileIndex = new MapFileIndex(new SplitKeyExtractor());
        String path1 = "test/resources/test1";
        String path2 = "test/resources/test2";
        fileIndex.add(path1);
        fileIndex.add(path2);

        assertEquals(fileIndex.find("aaaa"), Arrays.asList(path1, path2));
        assertEquals(fileIndex.find("cccc"), Arrays.asList(path1, path2));
        assertEquals(fileIndex.find("dddd"), Arrays.asList(path1, path2));
        assertEquals(fileIndex.find("test"), Arrays.asList(path1));
        assertEquals(fileIndex.find("test2"), Arrays.asList(path2));
        assertTrue(fileIndex.find("a").isEmpty());
        assertTrue((fileIndex.find("a").isEmpty()));
        assertTrue((fileIndex.find("bb").isEmpty()));
        assertTrue((fileIndex.find("cc").isEmpty()));
        assertTrue((fileIndex.find("ddd").isEmpty()));
        assertTrue((fileIndex.find("testt").isEmpty()));
    }
}
