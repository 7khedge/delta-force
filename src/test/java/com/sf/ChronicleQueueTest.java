package com.sf;

import com.sf.event.queue.ChroniclePersistence;
import com.sf.event.queue.ChronicleQueue;
import net.openhft.chronicle.Chronicle;
import net.openhft.chronicle.ExcerptAppender;
import net.openhft.chronicle.ExcerptTailer;
import net.openhft.chronicle.IndexedChronicle;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adityasofat on 12/11/2015.
 */
public class ChronicleQueueTest {

    //Given environment
    private static String queueDirectory = "/tmp";
    private static String queueName = "eventQueue";
    private static ChroniclePersistence chroniclePersistence = new ChroniclePersistence(queueDirectory, queueName);

    @AfterClass
    public static void tearDown() {
        chroniclePersistence.removePersistenceSpace();
    }

    private int readerIndex = 131;

    @Test
    public void shouldReadQueue() throws IOException {
        //Given
        String testMessage = "TestMessage";
        String basePath = System.getProperty("java.io.tmpdir") + "/event-queue2";
        System.out.println(basePath);
        Chronicle chronicle = new IndexedChronicle(basePath);
        // Obtain an ExcerptAppender
        ExcerptAppender appender = chronicle.createAppender();
        // Configure the appender to write up to 100 bytes
        appender.startExcerpt(100);
        // Copy the content of the Object as binary
        appender.writeObject(testMessage);
        // Commit
        appender.finish();
        // Obtain an ExcerptTrailer
        ExcerptTailer reader = chronicle.createTailer();
        // While until there is a new Excerpt to read
        while(!reader.nextIndex());
        // Read the object
        String actual = (String)reader.readObject();
        // Make the reader ready for next readx
        reader.finish();
        //Cleanup
        appender.close();
        reader.close();
        chronicle.close();
        MatcherAssert.assertThat(actual, CoreMatchers.equalTo(testMessage));
    }

    @Test
    public void publishAndReadMessages() throws IOException {
        Chronicle queue = createQueue("event-queue-2");
        publishMessages(queue, getMessages());
        readMessages(queue, new MessageListener() {
            @Override
            public void onMessage(Message message) {
                System.out.println(message.toString());
            }
        });

    }

    private List<Message<String>> getMessages() {
        List<Message<String>> messages = new ArrayList<Message<String>>();
        messages.add(new Message<String>("message0"));
        messages.add(new Message<String>("message1"));
        messages.add(new Message<String>("message2"));
        messages.add(new Message<String>("message3"));
        messages.add(new Message<String>("message4"));
        return messages;
    }

    private Chronicle createQueue(String queueName) throws IOException {
        return new IndexedChronicle(queueDirectory + "/" + queueName);
    }

    private void publishMessages(Chronicle queue, List<Message<String>> messages) throws IOException {
        ExcerptAppender appender = queue.createAppender();
        // Configure the appender to write up to 100 bytes
        appender.startExcerpt(500);
        // Copy the content of the Object as binary
        int index=0;
        for(Message<String> message : messages) {
            message.setIndex(appender.index());
            appender.writeObject(message);
            appender.finish();
        }
        // Commit
        appender.close();
    }

    private void readMessages(Chronicle queue, MessageListener messageListener) throws IOException {
        // Obtain an ExcerptTrailer
        ExcerptTailer reader = queue.createTailer();
        // While until there is a new Excerpt to read
        int index = 0;
        reader.index(readerIndex);
        while(reader.nextIndex()) {
            // Read the object
            Message<String> actual = (Message<String>) reader.readObject();
            messageListener.onMessage(actual);
            // Make the reader ready for next readx
            reader.finish();
        }
        reader.close();
    }

    @Test
    public void shouldCreateChronicleQueue(){
        //Given
        List<Message<String>> expectedMessages = getReadMessages(getMessages());
        final List<Message<String>> actualMessages = new ArrayList<Message<String>>();

        ChronicleQueue chronicleQueue = new ChronicleQueue(chroniclePersistence);
        chronicleQueue.init();
        //When
        chronicleQueue.publishMessages(getMessages());
        chronicleQueue.readMessages(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                actualMessages.add(message);
                System.out.println(message.toString());
            }
        }, -1);
        chronicleQueue.close();
        //Then
        MatcherAssert.assertThat(expectedMessages.toString(),CoreMatchers.is(actualMessages.toString()));

    }

    private List<Message<String>> getReadMessages(List<Message<String>> messages) {
        int index = 0;
        for(Message<String> message : messages) {
            message.setIndex(index++);
        }
        return messages;
    }

}
