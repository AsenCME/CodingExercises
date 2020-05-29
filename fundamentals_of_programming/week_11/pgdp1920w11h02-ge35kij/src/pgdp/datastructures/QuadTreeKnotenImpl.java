package pgdp.datastructures;

import java.util.NoSuchElementException;

public class QuadTreeKnotenImpl implements QuadTreeKnoten {
    private final int[][] data;
    private boolean isLeaf;
    private final QuadTreeKnoten[] nodes = new QuadTreeKnoten[4];

    public static QuadTreeKnoten buildFromIntArray(int[][] image) {
        return new QuadTreeKnotenImpl(image);
    }

    public QuadTreeKnotenImpl(int[][] _data) {
        data = _data;
        isLeaf = calculateIsLeaf(_data);

        if (!isLeaf) {
            nodes[0] = getTopLeft();
            nodes[1] = getTopRight();
            nodes[2] = getBottomLeft();
            nodes[3] = getBottomRight();
        }
    }

    @Override
    public QuadTreeKnoten getTopLeft() {
        if (isLeaf)
            throw new NoSuchElementException();

        int nodeSize = data.length / 2;
        int[][] nodeData = new int[nodeSize][nodeSize];

        for (int x = 0; x < nodeSize; x++)
            for (int y = 0; y < nodeSize; y++)
                nodeData[x][y] = data[x][y];
        return new QuadTreeKnotenImpl(nodeData);
    }

    @Override
    public QuadTreeKnoten getTopRight() {
        if (isLeaf)
            throw new NoSuchElementException();

        int nodeSize = data.length / 2;
        int[][] nodeData = new int[nodeSize][nodeSize];

        for (int x = nodeSize; x < data.length; x++)
            for (int y = 0; y < nodeSize; y++)
                nodeData[x - nodeSize][y] = data[x][y];

        return new QuadTreeKnotenImpl(nodeData);
    }

    @Override
    public QuadTreeKnoten getBottomLeft() {
        if (isLeaf)
            throw new NoSuchElementException();

        int nodeSize = data.length / 2;
        int[][] nodeData = new int[nodeSize][nodeSize];

        for (int x = 0; x < nodeSize; x++)
            for (int y = nodeSize; y < data.length; y++)
                nodeData[x][y - nodeSize] = data[x][y];
        return new QuadTreeKnotenImpl(nodeData);
    }

    @Override
    public QuadTreeKnoten getBottomRight() {
        if (isLeaf)
            throw new NoSuchElementException();

        int nodeSize = data.length / 2;
        int[][] nodeData = new int[nodeSize][nodeSize];

        for (int x = nodeSize; x < data.length; x++)
            for (int y = nodeSize; y < data.length; y++)
                nodeData[x - nodeSize][y - nodeSize] = data[x][y];

        return new QuadTreeKnotenImpl(nodeData);
    }

    @Override
    public int getRelativeColor(int x, int y) {
        if (!isInRange(x, y))
            throw new IllegalArgumentException("Coordinates out of range");

        return data[x][y];
    }

    @Override
    public void setRelativeColor(int x, int y, int color) {
        if (!isInRange(x, y))
            throw new IllegalArgumentException("Coordinates out of range");

        data[x][y] = color;

        isLeaf = calculateIsLeaf(data);
        if (isLeaf) {
            for (int i = 0; i < 4; i++)
                nodes[i] = null;
        } else {
            nodes[0] = getTopLeft();
            nodes[1] = getTopRight();
            nodes[2] = getBottomLeft();
            nodes[3] = getBottomRight();
        }
    }

    @Override
    public int getDimension() {
        return data.length;
    }

    @Override
    public int getSize() {
        int degree = 1;
        if (!isLeaf)
            for (QuadTreeKnoten node : nodes)
                degree += node.getSize();
        return degree;
    }

    @Override
    public boolean isLeaf() {
        return isLeaf;
    }

    @Override
    public int[][] toArray() {
        return data;
    }

    private boolean isInRange(int x, int y) {
        return (0 <= x && x < data.length) && (0 <= y && y < data.length);
    }

    private boolean calculateIsLeaf(int[][] _data) {
        int prevColor = _data[0][0];

        for (int x = 0; x < _data.length; x++)
            for (int y = 0; y < _data.length; y++)
                if (prevColor != _data[x][y])
                    return false;
        return true;
    }
}
