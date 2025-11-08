
package main;

import java.util.Scanner;

/**
 * Punto central del sistema de menús.
 *
 * Coordina:
 *  - Menú principal
 *  - Submenús de Mascota y Microchip
 *
 * Delegación:
 *  - La lógica de interacción se realiza en MenuHandler
 *  - La presentación se realiza en MenuDisplay
 */

public class AppMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final MenuHandler handler = new MenuHandler();

    public void iniciar() {

        while (true) {
            MenuDisplay.mostrarMenuPrincipal();
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1" -> handler.gestionarMascotas();
                case "2" -> handler.gestionarMicrochips();
                case "3" -> {
                    System.out.println("Saliendo...");
                    return;
                }
                default -> System.out.println("Opción inválida");
            }
        }
    }
}

