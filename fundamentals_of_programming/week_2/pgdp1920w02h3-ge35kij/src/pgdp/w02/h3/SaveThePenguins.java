package pgdp.w02.h3;

public class SaveThePenguins extends MiniJava {

  public static void main(String[] args) {
    String[] order = new String[] { "Jungtiere", "Junge Erwachsene", "Erwachsene", "Alte Erwachsene", "Weise" };
    Integer[] ageGroups = new Integer[] { 0, 0, 0, 0, 0 };
    int availableFood = 0;

    // Get simulation steps
    int steps = readInt("Bitte Geben Sie die Anzahl an Zeitschritten ein (>= 1):");
    if (steps < 1) {
      System.out.println("Zeitschritte >= 1 erforderlich");
      return;
    }

    // Get population
    for (int i = 0; i < order.length; i++) {
      ageGroups[i] = readInt(String.format("Startpopulation %s", order[i]));
    }

    // Run simlation
    for (int i = 0; i < steps; i++) {
      // Get food
      availableFood = ageGroups[1] * 3 + ageGroups[2] * 2;

      // Eat food
      for (int k = 0; k < order.length; k++) {
        int groupPopulation = ageGroups[k];
        if (availableFood - groupPopulation >= 0)
          availableFood -= groupPopulation;
        else {
          ageGroups[k] = availableFood;
          availableFood = 0;
        }
      }

      // Breed and get new babies
      int newBabies = ageGroups[1] / 4 + ageGroups[2] / 2 + ageGroups[3] / 3;

      // Age up
      ageGroups[4] = ageGroups[3];
      ageGroups[3] = ageGroups[2];
      ageGroups[2] = ageGroups[1];
      ageGroups[1] = ageGroups[0];
      ageGroups[0] = newBabies;
    }

    // Show results
    for (int i = 0; i < order.length; i++) {
      System.out.println(String.format("Anzahl %s:", order[i]));
      System.out.println(ageGroups[i]);
    }
  }
}