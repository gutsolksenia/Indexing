package com.gutsolk.indexing;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileUpdatesWatcherTest {
    @Test
    public void simpleTest() throws Exception {
        List<String> updated = new CopyOnWriteArrayList<>();
        List<String> deleted = new CopyOnWriteArrayList<>();
        FileUpdatesWatcher fileUpdatesWatcher = new FileUpdatesWatcher(deleted::add, updated::add);
        fileUpdatesWatcher.run();

        //create
        File file1 = createFile("testname1");
        File file2 = createFile("testname2");
        fileUpdatesWatcher.add(file1);
        fileUpdatesWatcher.add(file2);
        assertTrue(updated.isEmpty());
        assertTrue(deleted.isEmpty());

        //update
        updateFile(file1);
        Thread.sleep(2000);
        assertEquals(asList(file1.getName()), updated);
        assertTrue(deleted.isEmpty());

        //delete
        file2.delete();
        Thread.sleep(2000);
        assertEquals(asList(file1.getName()), updated);
        assertEquals(asList(file2.getName()), deleted);

        file1.delete();
        Thread.sleep(2000);
        assertEquals(asList(file1.getName()), updated);
        assertEquals(asList(file2.getName(), file1.getName()), deleted);
        fileUpdatesWatcher.stop();
    }

    private static File createFile(String filename) throws IOException {
        File file = new File(filename);
        file.createNewFile();
        return file;
    }

    private static void updateFile(File file) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write("testtttttt");
        fileWriter.close();
    }
}
