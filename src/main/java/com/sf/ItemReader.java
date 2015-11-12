package com.sf;

import java.util.Optional;

/**
 * Created by adityasofat on 11/11/2015.
 */
public interface ItemReader<T> {

    Optional<T> readItem();
}
