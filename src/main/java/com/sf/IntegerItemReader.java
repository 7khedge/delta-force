package com.sf;

import com.google.common.base.Optional;
import fj.data.List;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by adityasofat on 11/11/2015.
 */
public class IntegerItemReader implements ItemReader<Integer> {
    private List<Integer> range;
    private AtomicInteger index = new AtomicInteger(0);
    public IntegerItemReader(){
    }



    @Override
    public Optional<Integer> readItem() {
        try {
            return Optional.of(range.index(index.getAndIncrement()));
        }catch(Error e){
            return Optional.absent();
        }
    }

    @Override
    public void setIntegerList(List<Integer> range) {
        this.range = range;
    }
}
