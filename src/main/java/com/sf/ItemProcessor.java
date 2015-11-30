package com.sf;

import com.google.common.base.Optional;

/**
 * Created by adityasofat on 18/11/2015.
 */
public interface ItemProcessor<F,T> {

    Optional<T> process(F item);

}
