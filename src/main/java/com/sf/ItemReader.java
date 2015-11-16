package com.sf;

import com.google.common.base.Optional;
import fj.data.List;



/**
 * Created by adityasofat on 11/11/2015.
 */
public interface ItemReader<T> {

    Optional<T> readItem();

    void setIntegerList(List<T> range);
}
