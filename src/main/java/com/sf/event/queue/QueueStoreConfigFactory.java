package com.sf.event.queue;

import com.hazelcast.config.QueueStoreConfig;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by adityasofat on 08/11/2015.
 */
public class QueueStoreConfigFactory {

    public static QueueStoreConfig getJdbcBackedQueueConfig(DataSource dataSource, String queueName) {
        return new QueueStoreConfig()
                .setEnabled(true)
                .setStoreImplementation(new DBStoreConfig(new JdbcTemplate(dataSource),queueName));
    }
}
