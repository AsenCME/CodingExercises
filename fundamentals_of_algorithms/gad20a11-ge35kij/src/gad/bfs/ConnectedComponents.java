package gad.bfs;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;

public class ConnectedComponents {
    private BFS search;

    public ConnectedComponents() {
        search = new BFS();
    }

    public int countConnectedComponents(Graph g) {
        int count = 0;
        var nodes = g.getAllNodes();
        int nodesCount = nodes.size();
        var unvisited = new LinkedList<Graph.Node>();
        unvisited.addAll(nodes);
        var visitedCount = 0;

        while (visitedCount != nodesCount) {
            search.sssp(g, unvisited.get(0));
            var justVisited = new LinkedList<Graph.Node>();
            for (Graph.Node node : unvisited)
                try {
                    search.getDepth(node);
                    justVisited.add(node);
                } catch (Exception e) {
                }
            unvisited.removeAll(justVisited);
            visitedCount += justVisited.size();
            count++;
        }

        return count;
    }

    // public static void main(String[] args) {
    // var g = new Graph();
    // for (int i = 0; i < 2000; i++)
    // g.addNode();

    // g.addEdge(0, 4);
    // g.addEdge(1, 4);
    // g.addEdge(2, 3);

    // var cc = new ConnectedComponents();
    // var start = Instant.now();
    // System.out.println(cc.countConnectedComponents(g));
    // var end = Instant.now();
    // System.out.println(Duration.between(start, end).getNano() / 1000);
    // }
}