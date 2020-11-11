package com.gutsolk.indexing;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SplitKeyExtractorTest extends TestBase {
    @Test
    public void simpleTest() throws Exception {
        KeyExtractor keyExtractor = new SplitKeyExtractor();
        String path = getFile("test1");
        Set<String> extracted = keyExtractor.extract(path);
        assertTrue(extracted.contains(""));
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
        assertFalse(extracted.contains(" "));
    }
}
