package com.sf.event.queue;

import com.sf.Message;
import net.openhft.chronicle.ExcerptAppender;
import net.openhft.chronicle.IndexedChronicle;

import java.io.IOException;
import java.util.List;

/**
 * Created by adityasofat on 13/11/2015.
 */
public class PublishChronicleQueue {
    private final IndexedChronicle indexedChronicle;
    private ExcerptAppender excerptAppender;

    public PublishChronicleQueue(IndexedChronicle indexedChronicle) {

        this.indexedChronicle = indexedChronicle;
    }

    public void init() {
        try {
            this.excerptAppender = indexedChronicle.createAppender();
        } catch (IOException e) {
            throw new RuntimeException("Failed to chronicle Queue Appender",e);
        }
    }

    public void close() {
        if ( this.excerptAppender != null) {
            this.excerptAppender.close();
        }
    }

    public void publishMessages(List<Message<String>> messages) {
        for(Message<String> message : messages) {
            // Configure the appender to write up to 100 bytes
            this.excerptAppender.startExcerpt(100);
            message.setIndex(this.excerptAppender.index());
            // Copy the content of the Object as binary
            this.excerptAppender.writeObject(message);
            // Commit
            this.excerptAppender.finish();
        }
    }
}
