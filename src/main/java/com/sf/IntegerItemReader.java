package com.sf;

import fj.data.List;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by adityasofat on 11/11/2015.
 */
public class IntegerItemReader implements ItemReader<Integer> {
    private final List<Integer> range;
    private AtomicInteger index = new AtomicInteger(0);
    public IntegerItemReader(List<Integer> range) {
        this.range = range;
    }

    @Override
    public Optional<Integer> readItem() {
        try {
            return Optional.of(range.index(index.getAndIncrement()));
        }catch(Error e){
            return Optional.empty();
        }
    }
}
