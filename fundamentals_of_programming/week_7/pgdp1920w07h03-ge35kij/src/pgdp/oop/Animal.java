package pgdp.oop;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.io.File;

public abstract class Animal {
  protected int x, y;
  static String filename;
  protected File f;
  protected Image image;
  public boolean alive = true;

  protected static Animal[][] antarktis;

  public Animal(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void die() {
    antarktis[x][y] = null;
  }

  public void move() {
    boolean[] moves = new boolean[4];
    Animal newPosition = null;
    int newY = -1;
    int newX = -1;

    // XXX: Check left
    newX = wrapValue(x - 1);
    newPosition = antarktis[newX][y];
    if (newPosition == null) { // spot is free, save it
      moves[0] = true;
    } else if (canEat(newPosition)) { // animal at new position can be eaten -> eat, move, return
      antarktis[x][y] = null;
      newPosition.alive = false;
      x = newX;
      completeMove();
      return;
    }

    // XXX: Try upwards
    newY = wrapValue(y - 1);
    newPosition = antarktis[x][newY];
    if (newPosition == null) { // spot is free, save it
      moves[1] = true;
    } else if (canEat(newPosition)) { // animal at new position can be eaten -> eat, move, return
      antarktis[x][y] = null;
      newPosition.alive = false;
      y = newY;
      completeMove();
      return;
    }

    // XXX: Check right
    newX = wrapValue(x + 1);
    newPosition = antarktis[newX][y];
    if (newPosition == null) { // spot is free, save it
      moves[2] = true;
    } else if (canEat(newPosition)) { // animal at new position can be eaten -> eat, move, return
      antarktis[x][y] = null;
      newPosition.alive = false;
      x = newX;
      completeMove();
      return;
    }

    // XXX: Check below
    newY = wrapValue(y + 1);
    newPosition = antarktis[x][newY];
    if (newPosition == null) { // spot is free, save it
      moves[3] = true;
    } else if (canEat(newPosition)) { // animal at new position can be eaten -> eat, move, return
      antarktis[x][y] = null;
      newPosition.alive = false;
      y = newY;
      completeMove();
      return;
    }

    for (int i = 0; i < moves.length; i++) {
      boolean canMove = moves[i];

      // can't move there, try another move
      if (!canMove)
        continue;

      switch (i) {
      case 0: // left
        antarktis[x][y] = null;
        x = wrapValue(x - 1);
        completeMove();
        return;
      case 1: // up
        antarktis[x][y] = null;
        y = wrapValue(y - 1);
        completeMove();
        return;
      case 2: // right
        antarktis[x][y] = null;
        x = wrapValue(x + 1);
        completeMove();
        return;
      case 3: // down
        antarktis[x][y] = null;
        y = wrapValue(y + 1);
        completeMove();
        return;
      }
    }
  }

  protected static int wrapValue(int value) {
    if (value < 0)
      return 40;
    if (value > 40)
      return 0;
    else
      return value;
  }

  protected void completeMove() {
    antarktis[x][y] = this;
  }

  public abstract boolean canEat(Animal animal);

  protected abstract boolean eatenBy(Penguin penguin);

  protected abstract boolean eatenBy(PlayerPenguin playerPenguin);

  protected abstract boolean eatenBy(Whale whale);

  protected abstract boolean eatenBy(LeopardSeal leopardSeal);

  protected abstract boolean eatenBy(Fish fish);

  public static void setAntarktis(Animal[][] antarktis) {
    Animal.antarktis = antarktis;
  }

  // Graphics Stuff - You don't have to do anything here

  private void paintSymbol(Graphics g, Color c, int height, int width) {
    GradientPaint gradient = new GradientPaint(15, 0, c, width, 0, Color.LIGHT_GRAY);
    ((Graphics2D) g).setPaint(gradient);
    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.fillOval((int) (width * 0.3), (int) (height * 0.3), (int) (width * 0.5), (int) (height * 0.5));
  }

  public void draw(Graphics g, int height, int width) {
    if (image == null) {
      paintSymbol(g, Color.YELLOW, height, width);
      return;
    }
    ((Graphics2D) g).drawImage(image, 0, 0, width, height, 0, 0, image.getWidth(null), image.getHeight(null), null);
  }
}
