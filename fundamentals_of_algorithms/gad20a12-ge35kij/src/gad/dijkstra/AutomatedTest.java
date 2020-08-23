package gad.dijkstra;

import java.util.Random;

public class AutomatedTest {
    public static void main(String[] args) {
        int size = 20;
        int repeat = 10;

        for (int i = 0; i < repeat; i++) {

            Dijkstra d = new Dijkstra();
            Graph g = new Graph();
            for (int j = 0; j < size; j++)
                g.addNode();
            Random random = new Random(i);

            for (int j = 0; j < size * 3; j++)
                g.addEdge(random.nextInt(size), random.nextInt(size), random.nextInt(size * 10));

            try {
                d.findRoute(g, g.getNode(random.nextInt(size)), g.getNode(random.nextInt(size)), new StudentResult());
            } catch (RuntimeException ignored) {
            }
            System.out.println(d.getShortestPathLength());
            System.out.println(d.getShortestPath());
        }
    }
}
