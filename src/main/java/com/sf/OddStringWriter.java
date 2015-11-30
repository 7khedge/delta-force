package com.sf;

/**
 * Created by adityasofat on 20/11/2015.
 */
public class OddStringWriter implements  ItemWriter<String> {

    @Override
    public void write(String item) throws Exception {
        System.out.println(item);
    }
}
