package com.sf;

import com.google.common.base.Optional;

/**
 * Created by adityasofat on 18/11/2015.
 */
public class OddIntegerProcessor implements ItemProcessor<Integer,String> {
    @Override
    public Optional<String> process(Integer item) {
        if ( item % 2 == 0 ){
            return Optional.of(item.toString());
        }
        return Optional.absent();
    }
}
