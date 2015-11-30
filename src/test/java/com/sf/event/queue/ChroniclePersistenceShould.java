package com.sf.event.queue;

import org.junit.After;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by adityasofat on 16/11/2015.
 */
public class ChroniclePersistenceShould {

    //Given
    String queueName = "eventQueue";
    String baseDirectory  = "/tmp";
    ChroniclePersistence chroniclePersistence = new ChroniclePersistence(baseDirectory, queueName);

    @After
    public void tearDown() {
        File queueDirectory = new File(baseDirectory + File.separator + queueName);
        if ( queueDirectory.exists()) {
            queueDirectory.delete();
        }
    }

    @Test
    public void createPersistenceSpace() throws Exception {
        //When
        chroniclePersistence.createPersistenceSpace();
        //Then
        assertThatDirectory(true, baseDirectory + File.separator + queueName);
    }

    @Test
    public void removePersistenceSpace() throws Exception {
        //Given
        chroniclePersistence.createPersistenceSpace();
        //When
        chroniclePersistence.removePersistenceSpace();
        //Then
        assertThatDirectory(false, baseDirectory + File.separator + queueName);
    }

    @Test
    public void returnQueueName() throws Exception {
        //When//Then
        assertThat(chroniclePersistence.getQueueName(),equalTo(queueName));
    }

    @Test
    public void returnQueueDirectory() throws Exception {
        //When//Then
        assertThat(chroniclePersistence.getQueuePath(),equalTo(baseDirectory));
    }

    private void assertThatDirectory(boolean exists, String directoryPath) {
        File queueDirectory = new File(directoryPath);
        assertThat(queueDirectory.exists(), equalTo(exists));
    }

}