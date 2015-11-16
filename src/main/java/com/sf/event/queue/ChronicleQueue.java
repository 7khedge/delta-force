package com.sf.event.queue;

import com.sf.Message;
import com.sf.MessageListener;
import net.openhft.chronicle.IndexedChronicle;

import java.io.IOException;
import java.util.List;

/**
 * Created by adityasofat on 13/11/2015.
 */
public class ChronicleQueue {

    private final ChroniclePersistence chroniclePersistence;
    private IndexedChronicle indexedChronicle;
    private PublishChronicleQueue publishChronicleQueue;
    private ListenChronicleQueue listenChronicleQueue;

    public ChronicleQueue(ChroniclePersistence chroniclePersistence) {
        this.chroniclePersistence = chroniclePersistence;
    }

    public void init(){
        try {
            indexedChronicle = new IndexedChronicle(chroniclePersistence.getQueuePath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to create Chronicle Queue [" + chroniclePersistence.getQueuePath() + "]");
        }
        publishChronicleQueue = new PublishChronicleQueue(indexedChronicle);
        publishChronicleQueue.init();
        listenChronicleQueue = new ListenChronicleQueue(indexedChronicle);
        listenChronicleQueue.init();
    }

    public void publishMessages(List<Message<String>> messages) {
        publishChronicleQueue.publishMessages(messages);
    }

    public void readMessages(MessageListener messageListener, Integer fromIndex) {
        listenChronicleQueue.listen(messageListener,fromIndex);
    }

    public void close(){
        publishChronicleQueue.close();
        listenChronicleQueue.close();
        try {
            if ( indexedChronicle != null) {
                indexedChronicle.checkNotClosed();
                indexedChronicle.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to close Chronicle Queue [" + chroniclePersistence.getQueuePath() + "]",e);
        } catch (IllegalStateException ise) {
            throw new RuntimeException("Failed to close Chronicle Queue [" + chroniclePersistence.getQueuePath() + "] as it is already closed",ise);
        }
    }
}
