package com.sf;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

/**
 * Created by adityasofat on 11/11/2015.
 */
public class JobDefinitionBuilderShouldCreateJobDefinition {

    @Test
    public void withReader(){
        //Given
        String jobName = "ApplicationInstance";
        //When
        JobDefinition<Integer,String> jobDefinition = JobDefinitionBuilder.<Integer,String>jobDefinition()
                .name(jobName)
                .itemReader(new IntegerItemReader())
                .build();
        //Then
        MatcherAssert.assertThat(jobDefinition.getName(), CoreMatchers.equalTo(jobName));
    }

    @Test
    public void withReaderAndProcessor() {
        //Given
        String jobName = "ApplicationInstance";

        //When
        JobDefinition<Integer,String> jobDefinition = JobDefinitionBuilder.<Integer,String>jobDefinition()
                .name(jobName)
                .itemReader(new IntegerItemReader())
                .itemProcessor(new OddIntegerProcessor())
                .build();
        //Then
        MatcherAssert.assertThat(jobDefinition.getName(), CoreMatchers.equalTo(jobName));
    }


    @Test
    public void withReaderProcessorAndWriter() {
        //Given
        String jobName = "ApplicationInstance";

        //When
        JobDefinition<Integer,String> jobDefinition = JobDefinitionBuilder.<Integer,String>jobDefinition()
                .name(jobName)
                .itemReader(new IntegerItemReader())
                .itemProcessor(new OddIntegerProcessor())
                .itemWriter(new OddStringWriter())
                .build();
        //Then
        MatcherAssert.assertThat(jobDefinition.getName(), CoreMatchers.equalTo(jobName));
    }



}
