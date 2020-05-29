package pgdp.oop;

import java.awt.Toolkit;
import java.io.File;

public class Fish extends Animal {
  static String filename = "fish.png";

  public Fish(int x, int y) {
    super(x, y);

    f = new File(filename);
    image = Toolkit.getDefaultToolkit().getImage(f.getAbsolutePath());
  }

  public boolean canEat(Animal animal) {
    return animal.eatenBy(this);
  }

  @Override
  public void move() {
    Animal newPosition = null;
    int newY = -1;
    int newX = -1;

    // try up
    newY = wrapValue(y - 1);
    newPosition = antarktis[x][newY];
    if (newPosition == null) {
      antarktis[x][y] = null;
      y = newY;
      completeMove();
      return;
    }

    // try right
    newX = wrapValue(x + 1);
    newPosition = antarktis[newX][y];
    if (newPosition == null) {
      antarktis[x][y] = null;
      x = newX;
      completeMove();
      return;
    }

    // try down
    newY = wrapValue(y + 1);
    newPosition = antarktis[x][newY];
    if (newPosition == null) {
      antarktis[x][y] = null;
      y = newY;
      completeMove();
      return;
    }

    // try left
    newX = wrapValue(x - 1);
    newPosition = antarktis[newX][y];
    if (newPosition == null) {
      antarktis[x][y] = null;
      x = newX;
      completeMove();
      return;
    }
  }

  // Inherited methods
  @Override
  protected boolean eatenBy(Penguin penguin) {
    return true;
  }

  @Override
  protected boolean eatenBy(PlayerPenguin playerPenguin) {
    return true;
  }

  @Override
  protected boolean eatenBy(Whale whale) {
    return false;
  }

  @Override
  protected boolean eatenBy(LeopardSeal leopardSeal) {
    return true;
  }

  @Override
  protected boolean eatenBy(Fish fish) {
    return false;
  }
}
