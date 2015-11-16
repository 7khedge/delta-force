package com.sf;

import com.google.common.base.Optional;
import fj.data.List;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;



/**
 * Created by adityasofat on 11/11/2015.
 */
public class ItemReaderTest {

    @Test
    public void shouldReadItem(){
        //Given
        List<Integer> range = List.range(1, 4);
        List<Integer> actual = List.nil();
        IntegerItemReader integerItemReader  = new IntegerItemReader();
        integerItemReader.setIntegerList(range);
        //When
        while(true) {
            Optional<Integer> integerValue = integerItemReader.readItem();
            if (!integerValue.isPresent()){
                break;
            }
            actual = actual.cons(integerValue.get());
            System.out.println(integerValue.get());
        }
        //Then
        MatcherAssert.assertThat(range.toString(), CoreMatchers.equalTo(actual.reverse().toString()));
    }

}