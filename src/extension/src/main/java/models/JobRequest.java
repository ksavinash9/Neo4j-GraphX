package models;

import java.io.Serializable;

public class JobRequest implements Serializable {
    private JobRequestType jobRequestType;
    private String cypherQuery;

    public JobRequestType getJobRequestType() {
        return jobRequestType;
    }

    public void setJobRequestType(JobRequestType jobRequestType) {
        this.jobRequestType = jobRequestType;
    }

    public String getCypherQuery() {
        return cypherQuery;
    }

    public void setCypherQuery(String cypherQuery) {
        this.cypherQuery = cypherQuery;
    }
}
