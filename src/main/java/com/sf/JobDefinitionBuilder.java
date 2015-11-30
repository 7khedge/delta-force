package com.sf;

/**
 * Created by adityasofat on 11/11/2015.
 */
public class JobDefinitionBuilder<F,T> {

    private String jobName;
    private ItemReader<F> itemReader;
    private ItemProcessor<F, T> itemProcessor;
    private ItemWriter<T> itemWriter;

    private JobDefinitionBuilder() {
    }

    public static <X,O> JobDefinitionBuilder<X,O> jobDefinition() {
        return new JobDefinitionBuilder<X,O>();
    }

    public JobDefinitionBuilder name(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public JobDefinition<F,T> build() {
        return new JobDefinition<F,T>(jobName,itemReader);
    }

    public JobDefinitionBuilder itemReader(ItemReader<F> integerItemReader) {
        this.itemReader = integerItemReader;
        return this;
    }

    public JobDefinitionBuilder itemProcessor(ItemProcessor<F,T> itemProcessor) {
        this.itemProcessor = itemProcessor;
        return this;
    }

    public JobDefinitionBuilder itemWriter(ItemWriter<T> integerStringWriter) {
        this.itemWriter = integerStringWriter;
        return this;
    }
}
