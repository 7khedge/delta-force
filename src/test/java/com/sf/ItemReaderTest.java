package com.sf;

import fj.data.List;
import org.junit.Test;

import java.util.Optional;

/**
 * Created by adityasofat on 11/11/2015.
 */
public class ItemReaderTest {

    @Test
    public void shouldReadItem(){
        //Given
        List<Integer> range = List.range(1, 4);
        ItemReader<Integer> integerItemReader = new IntegerItemReader(range);
        while(true) {
            Optional<Integer> integerValue = integerItemReader.readItem();
            if (!integerValue.isPresent()){
                break;
            }
            System.out.println(integerValue.get());
        }
    }

}