package com.sf;

import java.io.Serializable;

/**
 * Created by adityasofat on 12/11/2015.
 */
public class Message<T> implements Serializable {
    public long index;
    public T payload;

    public Message(T payload) {
        this.payload = payload;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "Message{" +
                "index=" + index +
                ", payload=" + payload +
                '}';
    }
}
