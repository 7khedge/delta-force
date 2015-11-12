package com.sf;


import com.hazelcast.config.Config;
import com.hazelcast.config.QueueConfig;
import com.hazelcast.config.QueueStoreConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.QueueStore;
import com.sf.event.queue.QueueStoreConfigFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import rx.Observable;
import rx.Subscriber;
import rx.observers.SafeSubscriber;
import rx.schedulers.Schedulers;

import java.util.concurrent.CountDownLatch;

import static com.sf.QueueHelper.dataSource;

/**
 * Created by adityasofat on 09/11/2015.
 */
public class ObservableTest {

    private static HazelcastInstance hazelcastInstance;
    private static String queueName = "jobEventQueue";
    private static int queueSize = 5;

    @BeforeClass
    public static void setup() {
        Config config = new Config();
        QueueStoreConfig jdbcBackedQueueConfig = QueueStoreConfigFactory.getJdbcBackedQueueConfig(dataSource(), queueName);
        QueueConfig messageQueue = config.getQueueConfig(queueName);
        messageQueue.setQueueStoreConfig(jdbcBackedQueueConfig);
        hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        QueueStore storeImplementation = jdbcBackedQueueConfig.getStoreImplementation();
        for (long i = 0; i < queueSize; i++) {
            storeImplementation.store(i, String.valueOf(i));
        }
    }

    @AfterClass
    public static void tearDown() {
        shouldTruncateQueueStoreTable(queueName);
        hazelcastInstance.shutdown();
    }


    @Test
    public void shouldCreateObservable() {
        Observable<String> myObservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {
                        sub.onNext("Hello, world!");
                        sub.onCompleted();
                    }
                }
        );
        Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                System.out.println(s);
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }
        };
        myObservable.subscribe(mySubscriber);
    }

    @Test
    public void shouldCreateQueueObservable() throws InterruptedException {
        final IQueue<String> queue = hazelcastInstance.getQueue(queueName);

        Subscriber<String> stringSubscriber = new SafeSubscriber<String>(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e.getCause());
            }

            @Override
            public void onNext(String s) {
                System.out.println(Thread.currentThread().getName() + "Processed message [" + s + "]");
            }
        });

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Observable<String> myObservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {
                        try {
                            int size = queue.size();
                            for (int i = 0; i < size; i++) {
                                String message = queue.peek();
                                System.out.println(Thread.currentThread().getName() + "Processing Message [" + message + "]");
                                sub.onNext(message);
                                try {
                                    queue.take();
                                } catch (InterruptedException e) {
                                    sub.onError(e);
                                }
                            }

                            sub.onCompleted();
                        } finally {
                            countDownLatch.countDown();
                        }
                    }
                }
        )
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.computation());

        myObservable.subscribe(stringSubscriber);
        countDownLatch.await();
    }

    public static void shouldTruncateQueueStoreTable(String queueName) {
        //Given
        //When
        new JdbcTemplate(dataSource()).execute("TRUNCATE TABLE " + queueName);
    }

}
