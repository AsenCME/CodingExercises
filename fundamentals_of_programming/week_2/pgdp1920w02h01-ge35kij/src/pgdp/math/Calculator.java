package pgdp.math;

public class Calculator extends MiniJava {
    public static void main(String[] args) {
        int index;
        while (true) {
            // Print the entry message
            String[] entryMessage = new String[] { "Wählen Sie eine Operation:", "1) +", "2) -", "3) *", "4) /",
                    "5) %", };
            for (String line : entryMessage)
                write(line);

            // Get operation index
            index = readInt("6) Programm beenden");

            // Perform operation
            int num1;
            int num2;

            switch (index) {
            case 1:
                // addition
                num1 = readInt("Ersten Operand eingeben:");
                num2 = readInt("Zweiten Operand eingeben:");
                System.out.println(num1 + num2);
                break;
            case 2:
                // subtraction
                num1 = readInt("Ersten Operand eingeben:");
                num2 = readInt("Zweiten Operand eingeben:");
                System.out.println(num1 - num2);
                break;
            case 3:
                // multiplication
                num1 = readInt("Ersten Operand eingeben:");
                num2 = readInt("Zweiten Operand eingeben:");
                System.out.println(num1 * num2);
                break;
            case 4:
                // division
                num1 = readInt("Ersten Operand eingeben:");
                num2 = readInt("Zweiten Operand eingeben:");
                if (num2 == 0)
                    System.out.println("Fehler: Division durch 0!");
                else
                    System.out.println(num1 / num2);
                break;
            case 5:
                // modulo
                num1 = readInt("Ersten Operand eingeben:");
                num2 = readInt("Zweiten Operand eingeben:");
                if (num2 == 0)
                    System.out.println("Fehler: Division durch 0!");
                else
                    System.out.println(num1 % num2);
                break;

            case 6:
                return;

            default:
                break;
            }
        }
    }
}
