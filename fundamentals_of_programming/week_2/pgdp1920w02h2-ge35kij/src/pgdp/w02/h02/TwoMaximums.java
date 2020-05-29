package pgdp.w02.h02;

public class TwoMaximums extends MiniJava {
  public static void main(String[] args) {
    System.out.println("Bitte Anzahl eingeben:");
    int lines = readInt();
    if (lines < 2) {
      System.out.println("Fehler: Anzahl >= 2 erwartet!");
      return;
    }

    int biggest = Integer.MIN_VALUE;
    int secondBiggest = Integer.MIN_VALUE;
    for (int i = 0; i < lines; i++) {
      System.out.println("Bitte Zahlen eingeben:");
      int line = readInt();

      if (line > secondBiggest) {
        secondBiggest = line;
      }
      if (line > biggest) {
        secondBiggest = biggest;
        biggest = line;
      }

    }

    if (secondBiggest == Integer.MIN_VALUE)
      secondBiggest = biggest;
    write("Erster:");
    write(biggest);
    write("Zweiter:");
    write(secondBiggest);
  }
}
