package com.sf;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by adityasofat on 05/01/2016.
 */
public class PersistentMapShould {

    @Test
    public void testDeleteFile() throws IOException {
        File file = new File("/tmp/persistentMap.map");
        file.createNewFile();
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 128 * 1 * 1);
        buffer.put("queue00=000002".getBytes());
        fileChannel.close();
        randomAccessFile.close();
        File file2 = new File("/tmp/persistentMap.map");
        if ( file2.delete() ) {
            System.out.println("File deleted");
        }
        MatcherAssert.assertThat(file2.exists(), CoreMatchers.equalTo(false));
    }



    @Test
    public void writePersistentMap() throws IOException {
        // Create file object
        int entry_size = 14;
        File file = new File("/tmp/persistentMap.map");
        //Delete the file; we will create a new file
        file.delete();
        // Get file channel in readonly mode
        FileChannel fileChannel = new RandomAccessFile(file, "rw").getChannel();
        // Get direct byte buffer access using channel.map() operation
        MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 128 * 1 * 1);
        int startPosition = buffer.position();
        //Write the content using put methods
        buffer.put("queue00=000002".getBytes());
        buffer.position(0);
        buffer.put("queue00=000003".getBytes());
        buffer.put("queue01=000003".getBytes());
        buffer.put("queue02=000040".getBytes());
        buffer.position(entry_size * 1);
        buffer.put("queue01=000004".getBytes());
        buffer.position(entry_size * 2);
        buffer.put("queue02=000041".getBytes());
        int currentPosistion = buffer.position();
        byte[] bytes = new byte[14];
        buffer.isLoaded();
        buffer.position(1 * 14);
        buffer.get(bytes);
        String queue0 = new String(bytes);
        fileChannel.close();
    }

    @Test
    public void writeAndReadPersistentMap() throws IOException {
        PersistentMap persistentMap = new PersistentMap("/tmp/persistentMap.map",1024,14);
        persistentMap.init();
        persistentMap.put(getQueueEntry(0,1).getBytes(), 0);
        persistentMap.put(getQueueEntry(0,2).getBytes(), 0);
        persistentMap.put(getQueueEntry(1,1).getBytes(), 1);
        String queue0 = new String(persistentMap.read(0));
        String queue1 = new String(persistentMap.read(1));
        persistentMap.close();
    }

    @Test
    public void readPersistentMap() throws IOException {
        PersistentMap persistentMap = new PersistentMap("/tmp/persistentMap.map",1024,14);
        persistentMap.init();
        String queue0 = new String(persistentMap.read(0));
        String queue1 = new String(persistentMap.read(1));
        persistentMap.close();
    }

    private String getQueueEntry(int queueNumber, int index){
        return String.format("queue%02d=%06d",queueNumber,index);
    }

}
