
package main;

/*********************************************************************
 * Clase utilitaria dedicada exclusivamente a mostrar menús y mensajes.
 *
 * Evita mezclar lógica (MenuHandler) con presentación (MenuDisplay),
 * siguiendo separación de responsabilidades.
 */

public final class MenuDisplay {

    private MenuDisplay() {} // Clase utilitaria

    public static void mostrarMenuPrincipal() {
        System.out.println("\n===== SISTEMA DE VETERINARIA – Mascotas & Microchips =====");
        System.out.println("1. Gestionar Mascotas");
        System.out.println("2. Gestionar Microchips");
        System.out.println("3. Salir");
        System.out.print("Opción: ");
    }

    public static void mostrarMenuMascota() {
        System.out.println("\n---- Gestión de Mascotas ----");
        System.out.println("1. Registrar nueva mascota");
        System.out.println("2. Listar mascotas");
        System.out.println("3. Buscar por ID");
        System.out.println("4. Buscar por nombre");
        System.out.println("5. Actualizar mascota (NO asocia)");
        System.out.println("6. Eliminar mascota (soft)");
        System.out.println("7. Asociar / Desasociar Microchip");
        System.out.println("8. Volver");
        System.out.print("Opción: ");
    }

    public static void mostrarMenuMicrochip() {
        System.out.println("\n---- Gestión de Microchips ----");
        System.out.println("1. Registrar microchip");
        System.out.println("2. Listar microchips");
        System.out.println("3. Buscar por ID");
        System.out.println("4. Buscar por código");
        System.out.println("5. Actualizar microchip");
        System.out.println("6. Eliminar microchip (soft)");
        System.out.println("7. Volver");
        System.out.print("Opción: ");
    }
}

