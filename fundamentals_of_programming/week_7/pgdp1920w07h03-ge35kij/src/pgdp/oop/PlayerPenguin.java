package pgdp.oop;

public class PlayerPenguin extends Penguin {
    public PlayerPenguin(int x, int y) {
        super(x, y);
    }

    public boolean canEat(Animal animal) {
        return animal.eatenBy(this);
    }

    public boolean move(int newX, int newY) {
        // Player has not moved
        if (newX == x && newY == y) {
            return false;
        }

        Animal newPosition = antarktis[newX][newY];

        // Spot is free, move and return
        if (newPosition == null) {
            antarktis[newX][newY] = this;
            antarktis[x][y] = null;
            x = newX;
            y = newY;
            return false;
        }

        // Player has been eaten, game over, lose
        if (newPosition.canEat(this)) {
            this.alive = false;
            return true;
        }

        // There is a fish at the new position
        if (this.canEat(newPosition)) {
            newPosition.alive = false;
            antarktis[newX][newY] = this;
            antarktis[x][y] = null;
            x = newX;
            y = newY;
            return false;
        }

        // There is a seal (not null, cannot be eaten, cannot eat penguin)
        else {
            return true;
        }

    }
}
