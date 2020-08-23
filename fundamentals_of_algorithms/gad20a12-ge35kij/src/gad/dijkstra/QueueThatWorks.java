package gad.dijkstra;

import gad.dijkstra.Graph.Node;

import java.util.LinkedList;

public class QueueThatWorks extends LinkedList<Node> {
    private static final long serialVersionUID = 1L;

    @Override
    public Node poll() {
        var node = this.get(0);
        for (int i = 1; i < this.size(); i++) {
            var other = this.get(i);
            if (other.costToVisit < node.costToVisit)
                node = other;
            else if (other.costToVisit == node.costToVisit && other.getID() < node.getID())
                node = other;
        }
        this.remove(node);
        return node;
    }
}