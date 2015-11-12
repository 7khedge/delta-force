package com.sf;

import org.junit.Test;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by adityasofat on 09/11/2015.
 */
public class ObservableLearningOne {

    @Test
    public void shouldEmit123() {
        Observable
                .just(1, 2, 3)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("Completed Observable.");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.err.println("Whoops: " + throwable.getMessage());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(Thread.currentThread().getName() + " Got: " + integer);
                    }
                });
    }
}
