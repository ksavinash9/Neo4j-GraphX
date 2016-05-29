package models;

import org.neo4j.graphdb.*;
import translation.Writer;

import java.io.BufferedWriter;

import static org.neo4j.graphdb.DynamicRelationshipType.withName;

public class GraphWriter extends ParallelWriterConfiguration<Node> {

    GraphDatabaseService db;
    String relationshipType;

    public GraphWriter(int start, int length, BufferedWriter bufferedWriter, Integer fullSize, Integer reportBlockSize) {
        super(start, length, bufferedWriter, fullSize, reportBlockSize);
    }

    public GraphWriter(int start, int length, BufferedWriter bufferedWriter, Integer fullSize, Integer reportBlockSize, GraphDatabaseService db, String relationshipType) {
        super(start, length, bufferedWriter, fullSize, reportBlockSize);
        this.db = db;
        this.relationshipType = relationshipType;
    }

    public GraphDatabaseService getDb() {
        return db;
    }

    public void setDb(GraphDatabaseService db) {
        this.db = db;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    @Override
    public void writeBlock(Node block) {
        Transaction tx = db.beginTx();
        for (Relationship relationship : block.getRelationships(withName(relationshipType), Direction.OUTGOING)) {
            try {
                String line = relationship.getStartNode().getId() + " " + relationship.getEndNode().getId();
                bufferedWriter.write(line + "\n");
                Writer.counter++;
                if (Writer.counter % reportBlockSize == 0) {
                    // Report status
                    System.out.println("Records exported: " + Writer.counter);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        tx.close();
    }

    @Override
    public ParallelWriterConfiguration<Node> clone() throws CloneNotSupportedException {
        return new GraphWriter(start, length, bufferedWriter, fullSize, reportBlockSize, db, relationshipType);
    }
}
