package gad.dijkstra;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Graph {
    public class Node implements Comparable<Node> {
        private int id;
        public boolean visited;
        public int costToVisit;
        public LinkedList<Edge> edges;

        public Node(int id) {
            this.id = id;
            this.visited = false;
            this.costToVisit = Integer.MAX_VALUE;
            this.edges = new LinkedList<Edge>();
        }

        public void addEdge(Node to, int weight) {
            var edge = new Edge(to, weight);
            if (edges.stream().anyMatch(n -> n.sameAs(edge)))
                return;
            this.edges.add(edge);
            this.edges.sort(Edge::compareSort);
        }

        public int getID() {
            return this.id;
        }

        public List<Node> getAllNeighbours() {
            return this.edges.stream().map(x -> x.to).collect(Collectors.toList());
        }

        public void visit() {
            this.visited = true;
        }

        @Override
        public int compareTo(Node other) {
            int value = Integer.compare(this.costToVisit, other.costToVisit);
            if (value == 0)
                value = Integer.compare(this.id, other.id);
            return value;
        }

        public int compareSort(Node o) {
            return Integer.compare(id, o.id);
        }

        @Override
        public String toString() {
            return this.id + "";
        }
    }

    public class Edge implements Comparable<Edge> {
        private Node to;
        private int weight;

        public Edge(Node to, int weight) {
            this.to = to;
            this.weight = weight;
        }

        public int compareSort(Edge other) {
            return this.to.compareSort(other.to);
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }

        public boolean sameAs(Edge other) {
            return this.to == other.to;
        }

        public Node getTo() {
            return to;
        }

        public int getWeight() {
            return weight;
        }
    }

    private int nextIndex;
    private LinkedList<Node> nodes;

    public Graph() {
        nextIndex = 0;
        nodes = new LinkedList<Node>();
    }

    public int addNode() {
        int idx = nextIndex;
        this.nextIndex++;
        var node = new Node(idx);
        nodes.add(node);
        return idx;
    }

    public Node getNode(int id) {
        if (id < 0 || id > nodes.size() - 1)
            return null;
        return nodes.get(id);
    }

    public List<Node> getAllNodes() {
        return Collections.unmodifiableList(this.nodes);
    }

    public List<Node> getAllNeighbours(int id) {
        var node = this.nodes.get(id);
        var list = node.edges.stream().map(e -> e.to).collect(Collectors.toList());
        return Collections.unmodifiableList(list);
    }

    public void addEdge(int idA, int idB, int weight) {
        var from = this.getNode(idA);
        var to = this.getNode(idB);
        if (from == null || to == null)
            return;
        from.addEdge(to, weight);
    }
}