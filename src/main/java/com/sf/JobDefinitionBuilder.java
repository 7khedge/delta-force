package com.sf;

/**
 * Created by adityasofat on 11/11/2015.
 */
public class JobDefinitionBuilder {

    private String jobName;

    private JobDefinitionBuilder() {

    }

    public static JobDefinitionBuilder jobDefinition() {
        return new JobDefinitionBuilder();
    }

    public JobDefinitionBuilder name(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public JobDefinition build() {
        return new JobDefinition(jobName);
    }
}
