package com.sf;

/**
 * Created by adityasofat on 11/11/2015.
 */
public class JobDefinition<F,T> {
    private final String jobName;
    private final ItemReader<F> itemReader;
    private ItemProcessor<F,T> itemProcessor;

    public JobDefinition(String jobName, ItemReader<F> itemReader) {
        this.jobName = jobName;
        this.itemReader = itemReader;
    }

    public String getName() {
        return this.jobName;
    }

    public ItemReader<F> getItemReader() {
        return itemReader;
    }
}
