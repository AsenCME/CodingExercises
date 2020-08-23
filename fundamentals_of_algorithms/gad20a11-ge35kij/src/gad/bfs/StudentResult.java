package gad.bfs;

public class StudentResult implements Result {

    @Override
    public void visit(int node) {
        System.out.println("Visited node " + node);
    }
}
