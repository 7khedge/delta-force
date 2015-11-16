package com.sf.event.queue;

import java.io.File;

/**
 * Created by adityasofat on 16/11/2015.
 */
public class ChroniclePersistence {
    private final File queueDirectory;
    private final String queueName;
    private String queuePath;

    public ChroniclePersistence(String baseDirectory, String queueName) {
        this.queueName = queueName;
        this.queuePath = baseDirectory + File.separator + queueName;
        this.queueDirectory = new File(queuePath);
    }

    public void createPersistenceSpace() {
        if ( !queueDirectory.exists() ) {
            queueDirectory.mkdir();
        }
    }

    public void removePersistenceSpace() {
        if ( queueDirectory.exists() ) {
            String[]entries = queueDirectory.list();
            for(String fileName: entries){
                File currentFile = new File(queueDirectory.getPath(),fileName);
                currentFile.delete();
            }
            queueDirectory.delete();
        }
    }

    public String getQueueName() {
        return queueName;
    }

    public String getQueuePath() {
        return queuePath + File.separator + queueName;
    }
}
