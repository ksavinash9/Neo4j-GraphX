# Neo4j-GraphX
Neo4j-GraphX  extends Neo4j graph database to process big data graph algorithms with HDFS and Apache Spark on a scalable data set

# Graph Analytics for Neo4j

Architecture
================

This extensions uses a Message Broker architecture which is an intermediary program module that translates a message from the formal messaging protocol of the sender(neo4j) to the formal messaging protocol of the receiver(Apache Spark).
This message brokers is used to distribute graph processing jobs to [Apache Spark's GraphX](https://spark.apache.org/graphx/) module. 

A subgraph is exported from Neo4j and written to [Apache Hadoop HDFS](https://hadoop.apache.org/docs/r2.4.1/hadoop-project-dist/hadoop-hdfs/HdfsUserGuide.html) whenever an agent job is dispatched.

After a subgraph is exported to HDFS by Neo4j, a separate service for Spark is notified to begin processing that data. 
Then this extension starts graph processing agorithms by invoking the Spark's GraphX module through Scala. 
Then Spark's GraphX module sends serialized and distributed  algorithms for Apache Spark to process. 


## Supported Algorithms

- [x]  *SVD++ (unstable)*

- [x]  *PageRank*

- [x]  *Closeness Centrality*

- [x]  *Betweenness Centrality*

- [x]  *Triangle Counting*

- [x]  *Connected Components*

- [x]  *Strongly Connected Components*

- [x] *Label propagation (unstable)*

### Neo4j Mazerunner Service

The Neo4j Mazerunner service in this image is a [unmanaged extension](http://neo4j.com/docs/stable/server-unmanaged-extensions.html) that adds a REST API endpoint to Neo4j for submitting graph analysis jobs to Apache Spark GraphX. The results of the analysis are applied back to the nodes in Neo4j as property values, making the results queryable using Cypher.

## Installation/Deployment

Installation requires 3 docker image deployments, each containing a separate linked component.

* *Hadoop HDFS* (sequenceiq/hadoop-docker:2.4.1)
* *Neo4j Graph Database* (neo4j/docker-neo4j:2.3.1)
* *Apache Spark Service* (kbastani/neo4j-graph-analytics:1.1.0)

Pull the following docker images:

    docker pull sequenceiq/hadoop-docker:2.4.1
    docker pull neo4j/docker-neo4j:2.3.1
    docker pull kbastani/neo4j-graph-analytics:1.1.0

### Use Existing Neo4j Database

To use an existing Neo4j database, make sure that the database store directory, typically `data/graph.db`, is available on your host OS.

### Use New Neo4j Database

To create a new Neo4j database, use any path to a valid directory.

### Accessing the Neo4j Browser

The Neo4j browser is exposed on the `graphdb` container on port 7474. If you're using boot2docker on MacOSX, follow the directions [here](https://github.com/kbastani/docker-neo4j#boot2docker) to access the Neo4j browser.

## Usage Directions

Graph analysis jobs are started by accessing the following endpoint:

    http://localhost:7474/service/neo4j-graphx/analysis/{analysis}/{relationship_type}

Replace `{analysis}` in the endpoint with one of the following analysis algorithms:

+ labelpropogation
+ svdplusplus
+ pagerank
+ closeness_centrality
+ betweenness_centrality
+ triangle_count
+ connected_components
+ strongly_connected_components

Replace `{relationship_type}` in the endpoint with the relationship type in your Neo4j database that you would like to perform analysis on. The nodes that are connected by that relationship will form the graph that will be analyzed. For example, the equivalent Cypher query would be the following:

    MATCH (a)-[:FOLLOWS]->(b)
    RETURN id(a) as src, id(b) as dst

The result of the analysis will set the property with `{analysis}` as the key on `(a)` and `(b)`. For example, if you ran the `pagerank` analysis on the `FOLLOWS` relationship type, the following Cypher query will display the results:

    MATCH (a)-[:FOLLOWS]-()
    RETURN DISTINCT id(a) as id, a.pagerank as pagerank
    ORDER BY pagerank DESC

## Available Metrics

To begin graph analysis jobs on a particular metric, HTTP GET request on the following Neo4j server endpoints:

### PageRank

    http://172.17.0.21:7474/service/neo4j-graphx/analysis/pagerank/FOLLOWS

* Gets all nodes connected by the `FOLLOWS` relationship and updates each node with the property key `pagerank`.

* The value of the `pagerank` property is a float data type, ex. `pagerank: 3.14159265359`.

* PageRank is used to find the relative importance of a node within a set of connected nodes.

### Closeness Centrality

    http://172.17.0.21:7474/service/neo4j-graphx/analysis/closeness_centrality/FOLLOWS

* Gets all nodes connected by the `FOLLOWS` relationship and updates each node with the property key `closeness_centrality`.

* The value of the `closeness_centrality` property is a float data type, ex. `pagerank: 0.1337`.

* A key node centrality measure in networks is closeness centrality (Freeman, 1978; Opsahl et al., 2010; Wasserman and Faust, 1994). It is defined as the inverse of farness, which in turn, is the sum of distances to all other nodes.

### Betweenness Centrality

    http://172.17.0.21:7474/service/neo4j-graphx/analysis/betweenness_centrality/FOLLOWS

* Gets all nodes connected by the `FOLLOWS` relationship and updates each node with the property key `betweenness_centrality`.

* The value of the `betweenness_centrality` property is a float data type, ex. `betweenness_centrality: 20.345`.

* Betweenness centrality is an indicator of a node's centrality in a network. It is equal to the number of shortest paths from all vertices to all others that pass through that node. A node with high betweenness centrality has a large influence on the transfer of items through the network, under the assumption that item transfer follows the shortest paths.

### Triangle Counting

    http://172.17.0.21:7474/service/neo4j-graphx/analysis/triangle_count/FOLLOWS

* Gets all nodes connected by the `FOLLOWS` relationship and updates each node with the property key `triangle_count`.

* The value of the `triangle_count` property is an integer data type, ex. `triangle_count: 2`.

* The value of `triangle_count` represents the count of the triangles that a node is connected to.

* A node is part of a triangle when it has two adjacent nodes with a relationship between them. The `triangle_count` property provides a measure of clustering for each node.

### Connected Components

    http://172.17.0.21:7474/service/neo4j-graphx/analysis/connected_components/FOLLOWS

* Gets all nodes connected by the `FOLLOWS` relationship and updates each node with the property key `connected_components`.

* The value of `connected_components` property is an integer data type, ex. `connected_components: 181`.

* The value of `connected_components` represents the *Neo4j internal node ID* that has the lowest integer value for a set of connected nodes.

* Connected components are used to find isolated clusters, that is, a group of nodes that can reach every other node in the group through a *bidirectional* traversal.

### Strongly Connected Components

    http://172.17.0.21:7474/service/neo4j-graphx/analysis/strongly_connected_components/FOLLOWS

* Gets all nodes connected by the `FOLLOWS` relationship and updates each node with the property key `strongly_connected_components`.

* The value of `strongly_connected_components` property is an integer data type, ex. `strongly_connected_components: 26`.

* The value of `strongly_connected_components` represents the *Neo4j internal node ID* that has the lowest integer value for a set of strongly connected nodes.

* Strongly connected components are used to find clusters, that is, a group of nodes that can reach every other node in the group through a *directed* traversal.

