package gad.dijkstra;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import gad.dijkstra.Graph.Edge;
import gad.dijkstra.Graph.Node;

public class Dijkstra {
    private Node origin;
    private Node target;
    private LinkedList<Node> queue;
    private HashMap<Node, Node> parents;

    public Dijkstra() {
        // queue = new PriorityQueue<Node>();
        queue = new QueueThatWorks();
        parents = new HashMap<>();
    }

    public void findRoute(Graph g, Graph.Node a, Graph.Node b, Result result) {

        // Initialize
        this.origin = a;
        this.target = b;
        a.costToVisit = 0;
        a.visit();
        result.addNode(a.getID(), 0);

        // Run on nodes from origin
        for (Edge edge : a.edges) {
            var to = edge.getTo();
            to.costToVisit = edge.getWeight();
            result.addNode(to.getID(), to.costToVisit);
            queue.add(edge.getTo());
            parents.put(to, a);
        }

        // Loop over all
        while (!queue.isEmpty()) {
            var next = queue.poll();
            next.visit();
            if (next == b)
                break;
            for (Edge edge : next.edges) {
                var node = edge.getTo();
                if (node.visited)
                    continue;

                var cost = next.costToVisit + edge.getWeight();
                result.addNode(node.getID(), cost);
                if (node.costToVisit > cost) {
                    parents.put(node, next);
                    node.costToVisit = cost;
                }

                if (!queue.contains(node))
                    queue.add(node);
            }
        }

        if (!hasPath())
            throw new RuntimeException("No path found");
    }

    public List<Node> getShortestPath() {
        if (!hasPath())
            return List.of();

        var path = new LinkedList<Node>();
        path.add(0, this.target);
        var end = this.parents.get(this.target);
        while (end != this.origin) {
            path.add(0, end);
            end = this.parents.get(end);
        }
        path.add(0, this.origin);
        return path;
    }

    public Integer getShortestPathLength() {
        if (!hasPath())
            return 0;
        return this.target.costToVisit;
    }

    private boolean hasPath() {
        return this.target.costToVisit != Integer.MAX_VALUE;
    }

    public static void main(String[] args) {
        var g = new Graph();
        for (int i = 0; i < 20; i++)
            g.addNode();

        g.addEdge(0, 1, 4);
        g.addEdge(0, 2, 1);
        g.addEdge(1, 3, 1);
        g.addEdge(2, 1, 2);
        g.addEdge(2, 3, 5);
        g.addEdge(3, 4, 3);

        // g.addEdge(0, 2, 5);
        // g.addEdge(0, 1, 5);
        // g.addEdge(2, 3, 1);
        // g.addEdge(1, 3, 1);

        // g.addEdge(0, 1, 19);
        // g.addEdge(0, 2, 20);
        // g.addEdge(0, 3, 1);
        // g.addEdge(0, 4, 18);
        // g.addEdge(0, 5, 17);
        // g.addEdge(5, 6, 6);
        // g.addEdge(6, 3, 1);
        // g.addEdge(3, 2, 2);
        // g.addEdge(2, 1, 3);
        // g.addEdge(1, 4, 4);
        // g.addEdge(4, 5, 5);
        // g.addEdge(2, 5, 11);
        // g.addEdge(4, 3, 7);

        var d = new Dijkstra();
        d.findRoute(g, g.getNode(0), g.getNode(4), new StudentResult());
        System.out.println(d.getShortestPath());
        System.out.println(d.getShortestPathLength());
    }
}