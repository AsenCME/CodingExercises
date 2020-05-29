package pgdp.games;

public class Penguin extends MiniJava {

    public static void printWorld(int[][] world, int pinguRow, int pinguColumn) {
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[i].length; j++) {
                var pos = world[i][j];
                String boxString = "";
                if (pinguRow == i && pinguColumn == j)
                    boxString = "(P)";
                switch (pos) {
                case 0:
                    boxString = "L" + boxString;
                    break;
                case 1:
                    boxString = "E" + boxString;
                    break;
                case 2:
                    boxString = "W" + boxString;
                    break;
                case 3:
                    boxString = "H" + boxString;
                    break;
                case 4:
                    boxString = "F" + boxString;
                    break;
                }
                if (j != world[i].length - 1)
                    boxString += "\t";
                writeConsole(boxString);
            }
            write("");
        }
    }

    public static boolean isFishReachable(int[][] world, int pinguRow, int pinguColumn) {
        int pinguPos = world[pinguRow][pinguColumn];
        if (pinguPos == 3)
            return false;
        int[] lastPos = new int[] { pinguRow, pinguColumn };
        int[] lastLastPos = new int[] { pinguRow, pinguColumn };

        while (true) {
            int distToFish = distToFish(world, pinguRow, pinguColumn);
            switch (pinguPos) {

            case 0: // is on land

                int[] possibilitiesLand = possibilitiesLand(world, pinguRow, pinguColumn);
                if (contains(possibilitiesLand, 4))
                    return true;
                if (!contains(possibilitiesLand, 0) && !contains(possibilitiesLand, 1)
                        && !contains(possibilitiesLand, 2))
                    return false;

                for (int i = 0; i < possibilitiesLand.length; i++) {
                    int p = possibilitiesLand[i];
                    if (p == 3 || p == -1)
                        continue;
                    switch (i) {
                    case 0: // left
                        if (distToFish(world, pinguRow, pinguColumn - 1) <= distToFish) {
                            lastPos[1] = pinguColumn;
                            pinguColumn -= 1;
                            continue;
                        }
                        break;
                    case 1: // right
                        if (distToFish(world, pinguRow, pinguColumn + 1) <= distToFish) {
                            lastPos[1] = pinguColumn;
                            pinguColumn += 1;
                            continue;
                        }
                        break;
                    case 2: // up
                        if (distToFish(world, pinguRow - 1, pinguColumn) <= distToFish) {
                            lastPos[0] = pinguRow;
                            pinguRow -= 1;
                            continue;
                        }
                        break;
                    case 3: // down
                        if (distToFish(world, pinguRow + 1, pinguColumn) <= distToFish) {
                            lastPos[0] = pinguRow;
                            pinguRow += 1;
                            continue;
                        }
                        break;
                    }
                }

                break;
            case 1: // is on ice

                lastPos[0] = pinguRow;
                lastPos[1] = pinguColumn;

                int[] possibilitiesIce = possibilitiesIce(world, pinguRow, pinguColumn);
                if (contains(possibilitiesIce, 4))
                    return true;
                if (!contains(possibilitiesIce, 0) && !contains(possibilitiesIce, 1) && !contains(possibilitiesIce, 2))
                    return false;

                for (int i = 0; i < possibilitiesIce.length; i++) {
                    int p = possibilitiesIce[i];
                    if (p == 3 || p == -1)
                        continue;
                    switch (i) {
                    case 0: // leftUp
                        if (distToFish(world, pinguRow - 1, pinguColumn - 1) <= distToFish) {
                            pinguRow -= 1;
                            pinguColumn -= 1;
                            continue;
                        }
                        break;
                    case 1: // rightUp
                        if (distToFish(world, pinguRow - 1, pinguColumn + 1) <= distToFish) {
                            pinguRow -= 1;
                            pinguColumn += 1;
                            continue;
                        }
                        break;
                    case 2: // leftDown
                        if (distToFish(world, pinguRow + 1, pinguColumn - 1) <= distToFish) {
                            pinguColumn += 1;
                            pinguColumn -= 1;
                            continue;
                        }
                        break;
                    case 3: // rightDown
                        if (distToFish(world, pinguRow + 1, pinguColumn + 1) <= distToFish) {
                            pinguColumn += 1;
                            pinguColumn += 1;
                            continue;
                        }
                        break;
                    }
                }

                break;
            case 2: // is on water
                lastPos[0] = pinguRow;
                lastPos[1] = pinguColumn;

                int[] possibilitiesWater = possibilitiesWater(world, pinguRow, pinguColumn);
                if (contains(possibilitiesWater, 4))
                    return true;
                if (!contains(possibilitiesWater, 0) && !contains(possibilitiesWater, 1)
                        && !contains(possibilitiesWater, 2))
                    return false;

                for (int i = 0; i < possibilitiesWater.length; i++) {
                    int p = possibilitiesWater[i];
                    if (p == 3 || p == -1)
                        continue;
                    switch (i) {
                    case 0: // leftUp
                        if (distToFish(world, pinguRow - 3, pinguColumn - 3) <= distToFish) {
                            pinguRow -= 3;
                            pinguColumn -= 3;
                            continue;
                        }
                        break;
                    case 1: // rightUp
                        if (distToFish(world, pinguRow - 3, pinguColumn + 3) <= distToFish) {
                            pinguRow -= 3;
                            pinguColumn += 3;
                            continue;
                        }
                        break;
                    case 2: // leftDown
                        if (distToFish(world, pinguRow + 3, pinguColumn - 3) <= distToFish) {
                            pinguRow += 3;
                            pinguColumn -= 3;
                            continue;
                        }
                        break;
                    case 3: // rightDown
                        if (distToFish(world, pinguRow + 3, pinguColumn + 3) <= distToFish) {
                            pinguRow += 3;
                            pinguColumn += 3;
                            continue;
                        }
                        break;
                    }

                }

                break;
            }

            int a = lastPos[0];
            int b = lastPos[1];
            // hasn't moved
            if (lastPos[0] == pinguRow && lastPos[1] == pinguColumn)
                return false;
            lastPos[0] = pinguRow;
            lastPos[1] = pinguColumn;
            // has been here
            if (lastPos[0] == lastLastPos[0] && lastPos[1] == lastLastPos[1])
                return false;
            // has been here
            if (pinguRow == lastLastPos[0] && pinguColumn == lastLastPos[1])
                return false;
            lastLastPos[0] = a;
            lastLastPos[1] = b;

            pinguPos = world[pinguRow][pinguColumn];
        }
    }

    private static int[] getFishPos(int[][] world) {
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[i].length; j++) {
                if (world[i][j] == 4)
                    return new int[] { i, j };
            }
        }
        return new int[] { -1, -1 };
    }

    private static int getPosition(int[][] world, int posRow, int posCol) {
        if (posRow < 0 || posRow >= world.length)
            return -1;
        if (posCol < 0 || posCol >= world[posRow].length)
            return -1;
        return world[posRow][posCol];
    }

    private static boolean contains(int[] a, int b) {
        for (int i : a)
            if (i == b)
                return true;
        return false;
    }

    private static int distToFish(int[][] world, int row1, int col1) {
        int[] fishPos = getFishPos(world);
        return Math.abs(row1 - fishPos[0]) + Math.abs(col1 - fishPos[1]);
    }

    // Land methods
    private static int[] possibilitiesLand(int[][] world, int posRow, int posCol) {
        int up = getPosition(world, posRow - 1, posCol);
        int down = getPosition(world, posRow + 1, posCol);
        int left = getPosition(world, posRow, posCol - 1);
        int right = getPosition(world, posRow, posCol + 1);

        return new int[] { left, right, up, down };
    }

    // Ice methods
    private static int[] possibilitiesIce(int[][] world, int posRow, int posCol) {
        int leftUp = getPosition(world, posRow - 1, posCol - 1);
        int rightUp = getPosition(world, posRow - 1, posCol + 1);
        int leftDown = getPosition(world, posRow + 1, posCol - 1);
        int rightDown = getPosition(world, posRow + 1, posCol + 1);

        return new int[] { leftUp, rightUp, leftDown, rightDown };
    }

    // Water methods
    private static int[] possibilitiesWater(int[][] world, int posRow, int posCol) {
        int leftUp = getPosition(world, posRow - 3, posCol - 3);
        int rightUp = getPosition(world, posRow - 3, posCol + 3);
        int leftDown = getPosition(world, posRow + 3, posCol - 3);
        int rightDown = getPosition(world, posRow + 3, posCol + 3);

        return new int[] { leftUp, rightUp, leftDown, rightDown };
    }

    // Provided methods
    public static int[][] generateExampleWorldOne() {
        return new int[][] { { 2, 3, 3, 3, 3, 3 }, { 3, 0, 3, 3, 4, 3 }, { 3, 3, 3, 3, 3, 1 }, { 3, 3, 3, 0, 1, 3 },
                { 3, 3, 3, 3, 3, 3 } };
    }

    public static int[][] generateExampleWorldTwo() {
        return new int[][] { { 0, 0, 0, 2 }, { 0, 0, 0, 1 }, { 0, 1, 3, 4 }, { 3, 4, 3, 3 } };
    }

    public static void main(String[] args) {
        int pinguRow = 0;
        int pinguColumn = 1;
        int[][] world = generateExampleWorldTwo();
        // printWorld(world, pinguRow, pinguColumn);
        boolean res = isFishReachable(world, pinguRow, pinguColumn);
        write("" + res);
    }

}