package models;

import org.neo4j.graphdb.GraphDatabaseService;
import translation.Writer;

import java.io.BufferedReader;

public class CFBatchTransaction extends ParallelReaderConfiguration<String>  {

    private GraphDatabaseService graphDatabaseService;

    public CFBatchTransaction(int start, int length, BufferedReader bufferedReader, Integer fullSize, Integer reportBlockSize) {
        super(start, length, bufferedReader, fullSize, reportBlockSize);
    }

    public CFBatchTransaction(int start, int length, BufferedReader bufferedReader, Integer fullSize, Integer reportBlockSize, GraphDatabaseService graphDatabaseService) {
        super(start, length, bufferedReader, fullSize, reportBlockSize);
        this.graphDatabaseService = graphDatabaseService;
    }

    public GraphDatabaseService getGraphDatabaseService() {
        return graphDatabaseService;
    }

    public void setGraphDatabaseService(GraphDatabaseService graphDatabaseService) {
        this.graphDatabaseService = graphDatabaseService;
    }

    @Override
    public void transactBlock(String block) {
        Writer.updateCollaborativeFilteringForRow(block, graphDatabaseService, reportBlockSize);
    }

    @Override
    public ParallelReaderConfiguration<String> clone() throws CloneNotSupportedException {
        return new CFBatchTransaction(start, length, bufferedReader, fullSize, reportBlockSize, graphDatabaseService);
    }
}
