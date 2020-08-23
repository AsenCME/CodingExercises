package gad.bfs;

import java.util.Queue;
import java.util.HashMap;
import java.util.LinkedList;

import gad.bfs.Graph.Node;

public class BFS {
    private Result result;
    private HashMap<Integer, Integer> depths = new HashMap<>();
    private HashMap<Integer, Node> parents = new HashMap<>();

    public BFS() {
        this(ignored -> {
        });
    }

    public BFS(Result result) {
        this.result = result;
    }

    public void sssp(Graph g, Node start) {
        Queue<Integer> q = new LinkedList<>();
        var visited = new boolean[g.getAllNodes().size()];

        int startID = start.getID();
        depths.put(startID, 0);
        q.add(startID);

        var nextLayer = new LinkedList<Integer>();
        while (!q.isEmpty()) {
            for (var n : q) {
                result.visit(n);
                visited[n] = true;
            }
            while (!q.isEmpty()) {
                var nodeID = q.poll();
                int depth = this.depths.get(nodeID);
                var children = g.getAllNeighbours(nodeID);
                for (Node child : children) {
                    int childID = child.getID();
                    if (visited[childID] || nextLayer.contains(childID))
                        continue;
                    nextLayer.add(childID);
                    this.depths.put(childID, depth + 1);
                    this.parents.put(childID, g.getNode(nodeID));
                }
            }
            nextLayer.sort(Integer::compareTo);
            q.addAll(nextLayer);
            nextLayer.clear();
        }
    }

    public int getDepth(Node n) {
        var val = this.depths.get(n.getID());
        if (val == null)
            throw new UnsupportedOperationException();
        return val;
    }

    public Node getParent(Node n) {
        var val = this.parents.get(n.getID());
        if (val == null)
            throw new UnsupportedOperationException();
        return val;
    }

    public static void main(String[] args) {
        var g = new Graph();
        for (int i = 0; i < 20; i++)
            g.addNode();

        g.addEdge(0, 5);
        g.addEdge(0, 16);
        g.addEdge(0, 10);

        g.addEdge(5, 4);
        g.addEdge(5, 3);

        g.addEdge(10, 2);
        g.addEdge(10, 1);

        g.addEdge(16, 17);
        g.addEdge(16, 18);

        var bfs = new BFS(new StudentResult());
        bfs.sssp(g, g.getNode(0));
        System.out.println(bfs.getParent(g.getNode(8)).getID());
    }
}
