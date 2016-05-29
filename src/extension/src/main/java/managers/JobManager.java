package managers;

import com.google.gson.Gson;
import messaging.Worker;
import models.JobRequest;
import models.ProcessorMessage;
import models.ProcessorMode;
import org.apache.hadoop.fs.Path;
import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.util.Assert;
import translation.Writer;

import java.io.IOException;
import java.net.URISyntaxException;

public class JobManager {
    private JobRequest jobRequest;
    private GraphDatabaseService graphDatabaseService;

    public JobManager(JobRequest jobRequest, GraphDatabaseService graphDatabaseService) {
        this.jobRequest = jobRequest;
        this.graphDatabaseService = graphDatabaseService;
    }

    public JobRequest getJobRequest() {
        return jobRequest;
    }

    public void setJobRequest(JobRequest jobRequest) {
        this.jobRequest = jobRequest;
    }

    public GraphDatabaseService getGraphDatabaseService() {
        return graphDatabaseService;
    }

    public void setGraphDatabaseService(GraphDatabaseService graphDatabaseService) {
        this.graphDatabaseService = graphDatabaseService;
    }

    public void startJob() {
        Assert.notNull(jobRequest, "jobRequest must not be null");
        Assert.notNull(graphDatabaseService, "graphDatabaseService must not be null");
        Assert.notNull(jobRequest.getCypherQuery(), "jobRequest.cypherQuery must not be null");
        Assert.notNull(jobRequest.getJobRequestType(), "jobRequest.jobRequestType must not be null");

        // Query and write to HDFS
        try {
            Path path = Writer.exportCypherQueryToHDFSParallel(graphDatabaseService,
                    jobRequest.getCypherQuery(), jobRequest.getJobRequestType());

            // Serialize processor message
            ProcessorMessage message = new ProcessorMessage(path.toString(),
                    jobRequest.getJobRequestType().toString().toLowerCase(), ProcessorMode.Unpartitioned);

            Gson gson = new Gson();
            String strMessage = gson.toJson(message);

            // Send message to the Spark graph processor
            Worker.sendMessage(strMessage);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
