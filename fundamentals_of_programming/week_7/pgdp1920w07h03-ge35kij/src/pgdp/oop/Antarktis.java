package pgdp.oop;

import java.awt.event.WindowEvent;

public class Antarktis extends Maze {
    private static int width, height;
    private static Penguin lostPenguin;
    private static Whale[] whales = new Whale[5];
    private static LeopardSeal[] leopardSeals = new LeopardSeal[20];
    private static Fish[] fishes = new Fish[500];
    private static PlayerPenguin playerPenguin;

    public static void main(String[] args) {
        width = 41;
        height = 41;
        antarktis = generateMaze(width, height);

        Whale.setAntarktis(antarktis);
        LeopardSeal.setAntarktis(antarktis);
        Fish.setAntarktis(antarktis);
        Penguin.setAntarktis(antarktis);
        PlayerPenguin.setAntarktis(antarktis);

        setupMaze();
        gameLoop();

        // Close the opnend frame
        closeFrame();
    }

    private static void gameLoop() {
        while (true) {
            draw();

            int playerX = playerPenguin.x, playerY = playerPenguin.y;
            int newX = playerX, newY = playerY;

            switch (currentEvent) {
            case UP:
                newY = wrapValue(playerY - 1);
                break;

            case DOWN:
                newY = wrapValue(playerY + 1);
                break;

            case LEFT:
                newX = wrapValue(playerX - 1);
                break;

            case RIGHT:
                newX = wrapValue(playerX + 1);
                break;

            default:
                break;
            }

            int lostX = lostPenguin.x, lostY = lostPenguin.y;
            if (newX == lostX && newY == lostY) {
                write("Epic! You've won!");
                return;
            }

            if (playerPenguin.move(newX, newY)) {
                if (playerPenguin.alive) {
                    write("Your fate has been SEALed.");
                } else
                    write("A whale ate you.");

                write("You lose!");
                return;
            }

            moveAll();
            currentEvent = NOTHING;

            if (!lostPenguin.alive) {
                write("Lost penguin was eaten...");
                return;
            }
        }
    }

    private static int wrapValue(int value) {
        if (value < 0)
            return 40;
        if (value > 40)
            return 0;
        else
            return value;
    }

    private static void moveAll() {
        // move whales
        for (Animal whale : whales) {
            whale.move();
        }

        // move seals
        for (Animal seal : leopardSeals) {
            if (!seal.alive)
                continue;
            seal.move();
        }

        // move penguins
        lostPenguin.move();

        // move fish
        for (Animal fish : fishes) {
            if (!fish.alive)
                continue;
            fish.move();
        }
    }

    /**
     * Example Setup for easier Testing during development
     */
    private static void setupMaze() {
        int[] pos;
        playerPenguin = new PlayerPenguin(3, 3);
        antarktis[3][3] = playerPenguin;

        for (int i = 0; i < whales.length; i++) {
            pos = getRandomEmptyField();
            whales[i] = new Whale(pos[0], pos[1]);
            antarktis[pos[0]][pos[1]] = whales[i];
        }

        for (int i = 0; i < leopardSeals.length; i++) {
            pos = getRandomEmptyField();
            leopardSeals[i] = new LeopardSeal(pos[0], pos[1]);
            antarktis[pos[0]][pos[1]] = leopardSeals[i];
        }

        for (int i = 0; i < fishes.length; i++) {
            pos = getRandomEmptyField();
            fishes[i] = new Fish(pos[0], pos[1]);
            antarktis[pos[0]][pos[1]] = fishes[i];
        }

        antarktis[20][20] = new Penguin(20, 20);
        lostPenguin = (Penguin) antarktis[20][20];
    }
}
