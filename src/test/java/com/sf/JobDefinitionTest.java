package com.sf;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

/**
 * Created by adityasofat on 11/11/2015.
 */
public class JobDefinitionTest {

    @Test
    public void shouldCreateANewJobDefinition(){
        //Given
        String jobName = "ApplicationInstance";
        //When
        JobDefinition jobDefinition = JobDefinitionBuilder.jobDefinition().name(jobName).build();
        //Then
        MatcherAssert.assertThat(jobDefinition.getName(), CoreMatchers.equalTo(jobName));
    }

    @Test
    public void shouldCreateANewJobDefinitionWithReader(){
        //Given
        String jobName = "ApplicationInstance";

        //When
        JobDefinition jobDefinition = JobDefinitionBuilder.jobDefinition()
                .name(jobName)
                .itemReader(new IntegerItemReader())
                .build();
        //Then
        MatcherAssert.assertThat(jobDefinition.getName(), CoreMatchers.equalTo(jobName));
    }




}
