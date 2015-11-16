package com.sf;

import com.hazelcast.config.Config;
import com.hazelcast.config.QueueConfig;
import com.hazelcast.config.QueueStoreConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.QueueStore;
import com.sf.event.queue.DBStoreConfig;
import com.sf.event.queue.QueueStoreConfigFactory;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import org.apache.ibatis.jdbc.SQL;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

import static com.sf.QueueHelper.dataSource;

/**
 * Created by adityasofat on 08/11/2015.
 */
public class QueueTest {

    private final static String queueName = "jobEventQueue";

    @AfterClass
    public static void tearDown() {
        shouldTruncateQueueStoreTable(queueName);
    }


    @Test
    public void shouldCreateQueue() throws InterruptedException {
        //Given
        int numberOfEvents = 5;
        Config config = new Config();
        QueueStoreConfig jdbcBackedQueueConfig = QueueStoreConfigFactory.getJdbcBackedQueueConfig(dataSource(), queueName);
        QueueConfig messageQueue = config.getQueueConfig(queueName);
        messageQueue.setQueueStoreConfig(jdbcBackedQueueConfig);
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        //When
        QueueStore storeImplementation = jdbcBackedQueueConfig.getStoreImplementation();
        for(long i=0; i < numberOfEvents; i++ ) {
            storeImplementation.store(i,String.valueOf(i));
        }
        IQueue<String> iQueue = hazelcastInstance.getQueue(queueName);
        MatcherAssert.assertThat(iQueue.size(), CoreMatchers.equalTo(numberOfEvents));
        String actual = iQueue.take();
        MatcherAssert.assertThat(actual,CoreMatchers.equalTo("0"));
        hazelcastInstance.shutdown();
    }

    @Test
    @Ignore
    public void shouldCreateQueueStore() {
        //Given
        String queueName = "jobEventQueue";
        int numberOfEvents = 5;
        QueueStoreConfig jdbcBackedQueueConfig = QueueStoreConfigFactory.getJdbcBackedQueueConfig(dataSource(), queueName);
        //When
        QueueStore storeImplementation = jdbcBackedQueueConfig.getStoreImplementation();
        for(long i=0; i < numberOfEvents; i++ ) {
            storeImplementation.store(i,String.valueOf(i));
        }
        MatcherAssert.assertThat(storeImplementation.loadAllKeys().size(), CoreMatchers.equalTo(numberOfEvents));
        shouldTruncateQueueStoreTable(queueName);
    }

    //@Test
    public void shouldCreateQueueStoreTable() {
        //Given
        String queueName = "jobEventQueue";
        //When
        StringBuilder createTableSqlBuilder = new StringBuilder();
        createTableSqlBuilder.append("CREATE TABLE `" + queueName + "` (");
        createTableSqlBuilder.append("`id`             INT(11) NOT NULL,");
        createTableSqlBuilder.append("`message`        VARCHAR(1024) COLLATE utf8_bin DEFAULT NULL,");
        createTableSqlBuilder.append("`status`         VARCHAR(45) COLLATE utf8_bin DEFAULT NULL,");
        createTableSqlBuilder.append("`createDateTime` DATETIME         DEFAULT NULL,");
        createTableSqlBuilder.append("`updateDateTime` DATETIME         DEFAULT NULL,");
        createTableSqlBuilder.append("PRIMARY KEY (`id`))");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
        jdbcTemplate.execute(createTableSqlBuilder.toString());
    }

    //@Test
    public static void shouldTruncateQueueStoreTable(String queueName) {
        //Given
        //When
        new JdbcTemplate(dataSource()).execute("TRUNCATE TABLE "+ queueName);
    }





}
