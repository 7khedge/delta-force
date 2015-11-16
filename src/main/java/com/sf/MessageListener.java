package com.sf;

/**
 * Created by adityasofat on 12/11/2015.
 */
public interface MessageListener<T> {
    void onMessage(Message<T> message);
}
