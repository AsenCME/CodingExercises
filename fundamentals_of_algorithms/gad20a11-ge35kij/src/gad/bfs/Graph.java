package gad.bfs;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Graph {
    public class Node implements Comparable<Node> {
        private int id;

        public Node(int id) {
            this.id = id;
        }

        public int getID() {
            return this.id;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(id, o.id);
        }
    }

    private int nextIndex = 0;
    private List<Node> nodes = new LinkedList<>();
    private HashMap<Integer, LinkedList<Node>> map = new HashMap<>();

    public Graph() {
    }

    public int addNode() {
        var node = new Node(nextIndex);
        nodes.add(node);
        map.put(nextIndex, new LinkedList<Node>());
        return nextIndex++;
    }

    public Node getNode(int id) {
        return nodes.get(id);
    }

    public List<Node> getAllNodes() {
        return Collections.unmodifiableList(this.nodes);
    }

    public List<Node> getAllNeighbours(int id) {
        return Collections.unmodifiableList(map.get(id));
    }

    public void addEdge(int idA, int idB) {
        try {
            var nodeA = nodes.get(idA);
            var nodeB = nodes.get(idB);

            if (idA == idB && !map.get(idA).contains(nodeA)) {
                map.get(idA).add(nodeA);
            } else {
                if (!map.get(idA).contains(nodeB))
                    map.get(idA).add(nodeB);
                if (!map.get(idB).contains(nodeA))
                    map.get(idB).add(nodeA);
            }
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        var g = new Graph();
        g.addNode();
        g.addNode();
        g.addNode();

        g.addEdge(1, 1);
        g.addEdge(1, 0);
        g.addEdge(0, 1);
    }
}
