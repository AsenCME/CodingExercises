package gad.dijkstra;

public class StudentResult implements Result {

    @Override
    public void addNode(int id, int pathLength) {
        System.out.println("Added node " + id + " with length " + pathLength);
    }
}