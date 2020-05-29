package gad.maze;

public class Walker {
    private Result result;
    private boolean[][] maze;
    private int goalX, goalY;
    private boolean[][] visited;

    private int currentX = 1, currentY = 0;
    private char direction = 'd';
    private int turns = 0;
    private boolean hasMoved = false;
    private char lastTurn = 'l';

    public Walker(boolean[][] maze, int goalX, int goalY, Result result) {
        this.maze = maze;
        this.goalX = goalX;
        this.goalY = goalY;
        this.result = result;
        this.visited = new boolean[maze.length][maze.length];
    }

    private void moveForward() {
        hasMoved = true;
        switch (direction) {
            case 'u':
                currentY--;
                break;
            case 'd':
                currentY++;
                break;
            case 'l':
                currentX--;
                break;
            case 'r':
                currentX++;
                break;
        }
        log();
    }

    private void log() {
        try {
            result.addLocation(currentX, currentY);
            visited[currentX][currentY] = true;
        } catch (Exception e) {
        }
    }

    private void turnLeft() {
        if (lastTurn == 'l')
            turns++;
        else {
            lastTurn = 'l';
            turns = 1;
        }
        if (direction == 'd')
            direction = 'r';
        else if (direction == 'r')
            direction = 'u';
        else if (direction == 'u')
            direction = 'l';
        else
            direction = 'd';
    }

    private void turnRight() {
        if (lastTurn == 'r')
            turns++;
        else {
            lastTurn = 'r';
            turns = 1;
        }
        if (direction == 'd')
            direction = 'l';
        else if (direction == 'l')
            direction = 'u';
        else if (direction == 'u')
            direction = 'r';
        else
            direction = 'd';
    }

    public boolean walk() {
        log();
        while (true) {
            // back to start and no way to the right
            if (currentX == 1 && currentY == 0 && hasMoved)
                return false;

            if (currentX == goalX && currentY == goalY)
                return true;

            boolean nextCell = true;
            boolean rightCell = false;
            boolean isAtCorner = false;
            if (direction == 'd') {
                try {
                    nextCell = maze[currentX][currentY + 1];
                    rightCell = maze[currentX - 1][currentY];
                    isAtCorner = maze[currentX - 1][currentY - 1] && !rightCell;
                } catch (Exception e) {
                }
                if (currentY + 1 < maze.length) {

                }
            }
            if (direction == 'u') {
                try {
                    nextCell = maze[currentX][currentY - 1];
                    rightCell = maze[currentX + 1][currentY];
                    isAtCorner = maze[currentX + 1][currentY + 1] && !rightCell;
                } catch (Exception e) {
                }
                if (currentY - 1 >= 0) {

                }
            }
            if (direction == 'l') {
                try {
                    nextCell = maze[currentX - 1][currentY];
                    rightCell = maze[currentX][currentY - 1];
                    isAtCorner = maze[currentX + 1][currentY - 1] && !rightCell;
                } catch (Exception e) {
                }
                if (currentX - 1 >= 0) {

                }
            }
            if (direction == 'r') {
                try {
                    nextCell = maze[currentX + 1][currentY];
                    rightCell = maze[currentX][currentY + 1];
                    isAtCorner = maze[currentX - 1][currentY + 1] && !rightCell;
                } catch (Exception e) {
                }
            }

            if (isAtCorner) {
                turnRight();
                moveForward();
                continue;
            }
            if (!nextCell && rightCell)
                moveForward();
            else
                turnLeft();
        }
    }

    public static void main(String[] args) {
        // boolean[][] maze = Maze.generateStandardMaze(1, 1);
        // Walker walker = new Walker(maze, 2, 1, new StudentResult());
        // boolean canSolve = walker.walk();
        // Maze.draw(1, 0, walker.maze, walker.visited);

        int end = 100;
        for (int i = 95; i <= end; i++) {
            boolean[][] maze = Maze.generateMaze(100, 100, i);
            Walker walker = new Walker(maze, 99, 98, new StudentResult());
            boolean canSolve = walker.walk();

            if (!canSolve) {
                Maze.draw(1, 0, walker.maze, walker.visited);
                System.out.println(i);
            }
        }

    }
}
