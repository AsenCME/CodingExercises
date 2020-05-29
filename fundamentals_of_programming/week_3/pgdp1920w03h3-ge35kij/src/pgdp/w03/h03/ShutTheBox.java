package pgdp.w03.h03;

public class ShutTheBox extends MiniJava {
  public static void main(String[] args) throws IllegalAccessException {
    boolean[] boxes = { true, true, true, true, true, true, true, true };
    String[] boxPrompts = { "Box 1:", "Box 2:" };

    String[] playerPrompts = { "Spieler %d hat die folgenden Zahlen gewürfelt:",
        "Welche Boxen möchte Spieler %d schliessen? (0 für keine valide Kombination)" };

    int[] playerPoints = { 0, 0 };

    int numberOfRounds = 8;

    // Main game loop
    int winner = -1;
    for (int i = 0; i < numberOfRounds; i++) {

      // Player 1 and 2 turns
      for (int playerIndex = 0; playerIndex < 2; playerIndex++) {
        int roll1 = dice();
        int roll2 = dice();

        while (true) {
          // Tell player what they can do
          write(String.format(playerPrompts[0], playerIndex + 1));
          write(roll1);
          write(roll2);
          outputBoxes(boxes);
          write(String.format(playerPrompts[1], playerIndex + 1));

          // Let player one choose
          int box1 = readInt(boxPrompts[0]);
          int box2 = readInt(boxPrompts[1]);
          int box1Index = box1 - 1;
          int box2Index = box2 - 1;

          // Break if player forfeits turn (aka enters 0)
          if (box1 == 0 || box2 == 0) {
            for (int k = 0; k < boxes.length; k++)
              playerPoints[playerIndex] = boxes[k] ? playerPoints[playerIndex] + k + 1 : playerPoints[playerIndex] + 0;
            break;
          }

          // If index is out of range - try again
          if (box1Index >= boxes.length || box2Index >= boxes.length)
            continue;

          // If numbers don't add up - try again
          if (box1 + box2 != roll1 + roll2)
            continue;

          // Break if player can shut both boxes
          if (boxes[box1Index] == true && boxes[box2Index] == true) {
            boxes[box1Index] = false;
            boxes[box2Index] = false;

            // Check if all boxes are shut
            boolean allShut = true;
            for (boolean box : boxes)
              if (box) {
                allShut = false;
                break;
              }

            // There is a winner
            if (allShut)
              winner = playerIndex;

            break;
          }
        } // Otherwise loop again

        if (winner != -1)
          break;
      }

      if (winner != -1)
        break;
    }

    // There is preliminary winner
    if (winner != -1)
      write(String.format("Spieler %d schliesst alle Boxen! Spieler %d gewinnt!", winner + 1, winner + 1));
    // There is no preliminary winner
    else {
      // Show both scores
      String line = "Spieler %d Punktzahl";
      for (int i = 0; i < 2; i++) {
        write(String.format(line, i + 1));
        write(playerPoints[i]);
      }

      // Announce winner
      if (playerPoints[0] > playerPoints[1])
        write("Spieler 2 gewinnt!");
      else if (playerPoints[0] < playerPoints[1])
        write("Spieler 1 gewinnt!");
      else
        write("Unentschieden!");
    }
  }

  /**
   * Do not modify this !
   * 
   * @param boxes
   */
  private static void outputBoxes(boolean[] boxes) {
    StringBuilder sb = new StringBuilder("Geöffnete Boxen: 1:");
    sb.append(boxes[0]);
    for (int i = 1; i < boxes.length; i++) {
      sb.append(" ").append(i + 1).append(":").append(boxes[i]);
    }
    write(sb.toString());
  }
}
