import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;


public class SplitKeyExtractorTest {
    @Test
    public void simpleTest() throws IOException {
        KeyExtractor keyExtractor = new SplitKeyExtractor();
        String path = "test/resources/test1";
        Set<String> extracted = keyExtractor.extract(path);
        assertTrue(extracted.contains("aaaa"));
        assertTrue(extracted.contains("bbbb"));
        assertTrue(extracted.contains("cccc"));
        assertTrue(extracted.contains("dddd"));
        assertTrue(extracted.contains("test"));
        assertFalse(extracted.contains("a"));
        assertFalse(extracted.contains("a"));
        assertFalse(extracted.contains("bb"));
        assertFalse(extracted.contains("cc"));
        assertFalse(extracted.contains("ddd"));
        assertFalse(extracted.contains("testt"));
    }


}
