package pgdp.datastructures;

public interface QuadTreeKnoten {
    QuadTreeKnoten getTopLeft();

    QuadTreeKnoten getTopRight();

    QuadTreeKnoten getBottomLeft();

    QuadTreeKnoten getBottomRight();

    int getRelativeColor(int x, int y);

    void setRelativeColor(int x, int y, int color);

    int getDimension();

    int getSize();

    boolean isLeaf();

    int[][] toArray();

    default double getCompressionRatio() {
        double nodes = this.getSize();
        double pixels = Math.pow(this.getDimension(), 2);
        return (nodes / pixels);
    };
}
