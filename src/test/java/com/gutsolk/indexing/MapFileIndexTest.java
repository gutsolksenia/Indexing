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

        //remove
        assertTrue(fileIndex.remove(path1));
        assertTrue(fileIndex.remove(path2));
        assertTrue(fileIndex.find("aaaa").isEmpty());
        assertTrue(fileIndex.find("cccc").isEmpty());
        assertTrue(fileIndex.find("dddd").isEmpty());
        assertTrue(fileIndex.find("test").isEmpty());
        assertTrue(fileIndex.find("test2").isEmpty());
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
        assertEquals(fileIndex.find("1"), Arrays.asList(path1));
        assertEquals(fileIndex.find("2"), Arrays.asList(path2));

        //remove file
        assertTrue(fileIndex.remove(path1));
        assertTrue(fileIndex.find("1").isEmpty());
        assertEquals(fileIndex.find("2"), Arrays.asList(path2));

        //remove dir
        assertTrue(fileIndex.remove(directory));
        assertFalse(fileIndex.remove(path1));
        assertFalse(fileIndex.remove(path2));
    }
}
