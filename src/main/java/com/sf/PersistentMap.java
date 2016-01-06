package com.sf;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class PersistentMap {
    private final String filePath;
    private final int size;
    private final int entrySize;
    private FileChannel fileChannel;
    private MappedByteBuffer buffer;

    public PersistentMap(String filePath, int size, int entrySize) {
        this.filePath = filePath;
        this.size = size;
        this.entrySize = entrySize;
    }

    public void init() throws IOException {
        this.fileChannel = new RandomAccessFile(new File(filePath), "rw").getChannel();
        this.buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, size);
    }

    public void close() throws IOException {
        this.fileChannel.close();
    }

    public void put(byte[] bytes, int index) {
        if ( bytes.length != entrySize)
            throw new RuntimeException("bytes is the incorrect size [" + bytes.length + "]");
        buffer.position( entrySize  * index);
        buffer.put(bytes);
    }

    public byte[] read(int index){
        byte[] bytes = new byte[entrySize];
        buffer.position(entrySize * index);
        buffer.get(bytes);
        return bytes;
    }
}
