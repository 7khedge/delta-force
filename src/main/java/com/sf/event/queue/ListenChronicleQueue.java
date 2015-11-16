package com.sf.event.queue;

import com.sf.Message;
import com.sf.MessageListener;
import net.openhft.chronicle.ExcerptTailer;
import net.openhft.chronicle.IndexedChronicle;

import java.io.IOException;

/**
 * Created by adityasofat on 16/11/2015.
 */
public class ListenChronicleQueue {

    private final IndexedChronicle indexedChronicle;
    private ExcerptTailer excerptTailer;

    public ListenChronicleQueue(IndexedChronicle indexedChronicle) {
        this.indexedChronicle = indexedChronicle;
    }

    public void init() {
        try {
            this.excerptTailer = indexedChronicle.createTailer();
        } catch (IOException e) {
            throw new RuntimeException("Failed to chronicle Queue Tailer",e);
        }
    }

    public void close() {
        if ( this.excerptTailer !=null ){
            this.excerptTailer.close();
        }
    }

    public void listen(MessageListener messageListener, Integer fromIndex) {
        // While until there is a new Excerpt to read
        this.excerptTailer.index(fromIndex);
        while(this.excerptTailer.nextIndex()) {
            // Read the object
            Message<String> actual = (Message<String>) this.excerptTailer.readObject();
            messageListener.onMessage(actual);
            // Make the reader ready for next read
            this.excerptTailer.finish();
        }
    }
}
