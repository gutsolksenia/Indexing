package com.gutsolk.indexing;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class MapFileIndexTest extends TestBase {
    @Test
    public void simpleTest() {
        FileIndex fileIndex = new MapFileIndex();
        //add
        String path1 = getFile("test1");
        String path2 = getFile("test2");
        fileIndex.add(path1);
        fileIndex.add(path2);

        //get
        assertEquals(fileIndex.get("aaaa"), Arrays.asList(path1, path2));
        assertEquals(fileIndex.get("cccc"), Arrays.asList(path1, path2));
        assertEquals(fileIndex.get("dddd"), Arrays.asList(path1, path2));
        assertEquals(fileIndex.get("test"), Arrays.asList(path1));
        assertEquals(fileIndex.get("test2"), Arrays.asList(path2));
        assertTrue(fileIndex.get("a").isEmpty());
        assertTrue((fileIndex.get("a").isEmpty()));
        assertTrue((fileIndex.get("bb").isEmpty()));
        assertTrue((fileIndex.get("cc").isEmpty()));
        assertTrue((fileIndex.get("ddd").isEmpty()));
        assertTrue((fileIndex.get("testt").isEmpty()));

        //remove
        assertTrue(fileIndex.remove(path1));
        assertTrue(fileIndex.remove(path2));
        assertTrue(fileIndex.get("aaaa").isEmpty());
        assertTrue(fileIndex.get("cccc").isEmpty());
        assertTrue(fileIndex.get("dddd").isEmpty());
        assertTrue(fileIndex.get("test").isEmpty());
        assertTrue(fileIndex.get("test2").isEmpty());
        assertFalse(fileIndex.remove(path1));
        assertFalse(fileIndex.remove(path2));
    }

    @Test
    public void testDirectory() {
        String directory = getFile("test_directory");
        String path1 = directory + "/1";
        String path2 = directory + "/2";

        //add
        FileIndex fileIndex = new MapFileIndex();
        fileIndex.add(directory);
        assertEquals(fileIndex.get("1"), Arrays.asList(path1));
        assertEquals(fileIndex.get("2"), Arrays.asList(path2));

        //remove file
        assertTrue(fileIndex.remove(path1));
        assertTrue(fileIndex.get("1").isEmpty());
        assertEquals(fileIndex.get("2"), Arrays.asList(path2));

        //remove dir
        assertTrue(fileIndex.remove(directory));
        assertFalse(fileIndex.remove(path1));
        assertFalse(fileIndex.remove(path2));
    }
}
