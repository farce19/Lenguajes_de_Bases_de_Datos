package com.mycompany.lenguajes_de_bases_de_datos;
import java.util.Scanner;

public class UIHelper {
    private static final Scanner scanner = new Scanner(System.in);

    // Mostrar un encabezado estilizado
    public static void showHeader(String title) {
        String separator = "=".repeat(title.length() + 4);
        System.out.println("\n" + separator);
        System.out.println("  " + title);
        System.out.println(separator);
    }

    // Mostrar mensaje informativo
    public static void showInfo(String message) {
        System.out.println("[INFO] " + message);
    }

    // Mostrar mensaje de error
    public static void showError(String message) {
        System.err.println("[ERROR] " + message);
    }

    // Mostrar opciones
    public static void showOptions(String[] options) {
        for (int i = 0; i < options.length; i++) {
            System.out.printf("%d. %s%n", i + 1, options[i]);
        }
        System.out.print("Seleccione una opción: ");
    }

    // Capturar entradas numéricas con validación
    public static int getValidatedIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Entrada inválida. Por favor, ingrese un número: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    // Capturar entradas de texto con validación
    public static String getValidatedStringInput(String prompt) {
        System.out.print(prompt);
        String input;
        do {
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.print("La entrada no puede estar vacía. Intente de nuevo: ");
            }
        } while (input.isEmpty());
        return input;
    }

    // Limpiar la consola
    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("No se pudo limpiar la consola.");
        }
    }
}
/**/
