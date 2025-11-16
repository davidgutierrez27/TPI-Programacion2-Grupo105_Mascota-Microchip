
package main;

import model.Mascota;
import model.Microchip;
import service.MascotaService;
import service.MascotaServiceImpl;
import service.MicrochipService;
import service.MicrochipServiceImpl;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


/***********************************************
 * Maneja toda la lógica del menú interactivo:
 *
 * Responsabilidades:
 * ------------------
 * - Leer entradas del usuario.
 * - Convertir y validar cada dato.
 * - Invocar a los servicios correspondientes.
 * - Mostrar mensajes claros de éxito o error.
 *
 * Este handler no contiene lógica de persistencia
 * ni lógica de negocio: delega todo a los Services.
 */

public class MenuHandler {

    private final Scanner scanner = new Scanner(System.in);

    private final MascotaService mascotaService = new MascotaServiceImpl();
    private final MicrochipService microchipService = new MicrochipServiceImpl();

    // ---------------------------------------------------------------------

    public void gestionarMascotas() {
        while (true) {
            MenuDisplay.mostrarMenuMascota();
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1" -> registrarMascota();
                case "2" -> listarMascotas();
                case "3" -> buscarMascotaPorId();
                case "4" -> buscarMascotaPorNombre();
                case "5" -> actualizarMascota();
                case "6" -> eliminarMascota();
                case "7" -> asociarMicrochip();
                case "8" -> { return; }
                default -> System.out.println("Opción inválida");
            }
        }
    }

    public void gestionarMicrochips() {
        while (true) {
            MenuDisplay.mostrarMenuMicrochip();
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1" -> registrarMicrochip();
                case "2" -> listarMicrochips();
                case "3" -> buscarMicrochipPorId();
                case "4" -> buscarMicrochipPorCodigo();
                case "5" -> actualizarMicrochip();
                case "6" -> eliminarMicrochip();
                case "7" -> { return; }
                default -> System.out.println("Opción inválida");
            }
        }
    }

    
    
    // ---------------------------------------------------------------------
    // MASCOTA
    // ---------------------------------------------------------------------

    private void registrarMascota() {
        try {
            System.out.println("\n--- Registrar Mascota ---");

            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Especie: ");
            String especie = scanner.nextLine();

            System.out.print("Raza: ");
            String raza = scanner.nextLine();

            System.out.print("Fecha nacimiento (YYYY-MM-DD o vacío): ");
            String fecha = scanner.nextLine();
            LocalDate fechaNac = fecha.isBlank() ? null : LocalDate.parse(fecha);

            System.out.print("Dueño: ");
            String duenio = scanner.nextLine();

             // ---- MICROCHIP ----
            Microchip chip = null;

            System.out.print("¿Tiene microchip? (s/n): ");
            String tieneChip = scanner.nextLine().trim().toLowerCase();

            if (tieneChip.equals("s")) {

                System.out.print("Código del chip: ");
                String codigo = scanner.nextLine().trim();

                // 1) Buscar chip
                chip = microchipService.getByCodigo(codigo);

                // 2) Si NO existe → registrarlo de forma normal
                if (chip == null) {
                    System.out.println("⚠ El microchip no existe. Debe registrarlo ahora.");

                    registrarMicrochip();   // → pide datos, inserta correctamente

                    // 3) Volvemos a buscarlo
                    chip = microchipService.getByCodigo(codigo);

                    if (chip == null) {
                        System.out.println("❌ Error: el microchip no fue creado.");
                        return;
                    }
                }
            }

            Mascota m = new Mascota(null, false, nombre, especie, raza, fechaNac, duenio, chip);
            mascotaService.insertar(m);

            System.out.println("✅ Mascota registrada con ID: " + m.getId());

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    
    private void listarMascotas() {
        try {
            List<Mascota> lista = mascotaService.getAll();
            lista.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    
    private void buscarMascotaPorId() {
        try {
            System.out.print("ID: ");
            Long id = Long.parseLong(scanner.nextLine());

            Mascota m = mascotaService.getById(id);
            System.out.println(m != null ? m : "No encontrado");

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    
    private void buscarMascotaPorNombre() {
        try {
            System.out.print("Nombre (LIKE): ");
            String nombre = scanner.nextLine();

            List<Mascota> lista = mascotaService.buscarPorNombre(nombre);
            lista.forEach(System.out::println);

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    
    private void actualizarMascota() {
        try {
            System.out.print("ID de mascota: ");
            Long id = Long.parseLong(scanner.nextLine());

            Mascota m = mascotaService.getById(id);
            if (m == null) {
                System.out.println("Mascota no encontrada");
                return;
            }

            System.out.println("Deje vacío para mantener valor actual:");

            System.out.print("Nombre (" + m.getNombre() + "): ");
            String nombre = scanner.nextLine();
            if (!nombre.isBlank()) m.setNombre(nombre);

            System.out.print("Especie (" + m.getEspecie() + "): ");
            String especie = scanner.nextLine();
            if (!especie.isBlank()) m.setEspecie(especie);

            System.out.print("Raza (" + m.getRaza() + "): ");
            String raza = scanner.nextLine();
            if (!raza.isBlank()) m.setRaza(raza);

            System.out.print("Fecha nacimiento (" + m.getFechaNacimiento() + "): ");
            String fecha = scanner.nextLine();
            if (!fecha.isBlank()) m.setFechaNacimiento(LocalDate.parse(fecha));

            System.out.print("Dueño (" + m.getDuenio() + "): ");
            String duenio = scanner.nextLine();
            if (!duenio.isBlank()) m.setDuenio(duenio);
                    
            mascotaService.actualizar(m);

            System.out.println("✅ Mascota actualizada");

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    
    
    private void asociarMicrochip() {
    try {
        System.out.print("ID de mascota: ");
        Long id = Long.parseLong(scanner.nextLine());

        Mascota m = mascotaService.getById(id);
        if (m == null) {
            System.out.println("❌ Mascota no encontrada");
            return; //retorna sin hacer commit ni rolback no se ejecuta service ni Dao
        }

        System.out.println("\nMascota encontrada:");
        System.out.println("Nombre: " + m.getNombre());
        System.out.println("Especie: " + m.getEspecie());
        System.out.println("Raza: " + m.getRaza());
        System.out.println("Fecha nacimiento: " + m.getFechaNacimiento());
        System.out.println("Dueño: " + m.getDuenio());

        System.out.println("Microchip asignado: " +
                (m.getMicrochip() != null ? m.getMicrochip().getCodigo() : "(NINGUNO)"));

        
        // Menu
        String opcion = "";
        Set<String> opcionesValidas = new HashSet<>();

        while (true) {

            System.out.println("\nSeleccione una opción:");

            // Construimos dinámicamente las opciones válidas
            opcionesValidas.clear();
            opcionesValidas.add("0");  // Volver siempre existe

            if (m.getMicrochip() == null) {
                System.out.println("1) Asignar microchip");
                opcionesValidas.add("1");
            } else {
                System.out.println("1) Reasignar microchip"); 
                System.out.println("2) Eliminar asignación");
                opcionesValidas.add("1");
                opcionesValidas.add("2");
            }

            System.out.println("0) Volver atrás");

            System.out.print("Opción: ");
            opcion = scanner.nextLine().trim();

            if (opcionesValidas.contains(opcion)) {
                break; // salida válida del menú
            }

            System.out.println("⚠ Opción inválida. Intente nuevamente.");
        }


        Microchip nuevoChip = null;

        // OPCIÓN 3 → SALIR 
        if (opcion.equals("0")) {
            System.out.println("↩ No se realizaron cambios.");
            return; //retorna sin hacer commit ni rolback no se ejecuta service ni Dao
        }

        // OPCIÓN 2 → DESASIGNAR MICROCHIP 
        if (opcion.equals("2")) {
            m.setMicrochip(nuevoChip);
            mascotaService.actualizar(m);   // ← COMMIT 
            System.out.println("✅ Asignación eliminada.");
            return; //retorna pero ya habremos hecho commit en dao
        }

            // OPCIÓN 1 → ASIGNAR / REASIGNAR
            System.out.print("Código del microchip: ");
            String codigoChip = scanner.nextLine().trim();
            
            //VALIDACIÓN si existe
            nuevoChip = microchipService.getByCodigo(codigoChip);
            if (nuevoChip == null) {
                System.out.println("❌ El microchip no existe. Debe registrarlo primero.");
                return; //retorna sin hacer commit ni rolback no se ejecuta service ni Dao
            }
            
            // VALIDACIÓN futura: Si Microchip esta usado por otra mascota 
            //buscarMascotaPorMicrochip(nuevoChip.getId()); 
          

            m.setMicrochip(nuevoChip);
            mascotaService.actualizar(m); // ← COMMIT

            System.out.println("✅ Microchip asignado:");
            System.out.println("Mascota: " + m.getNombre() + " → Chip: " + nuevoChip.getCodigo());

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            // Aquí SI hay rollback si el DAO lo hace
        }
    }

    
    private void eliminarMascota() {
        try {
            System.out.print("ID: ");
            Long id = Long.parseLong(scanner.nextLine());

            mascotaService.eliminar(id);
            System.out.println("✅ Eliminada");

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    
    
    
    
    
    
    
    
    // ---------------------------------------------------------------------
    // MICROCHIP
    // ---------------------------------------------------------------------
  
    private void registrarMicrochip() {
        try {
            System.out.println("\n--- Registrar Microchip ---");

            System.out.print("Código: ");
            String codigo = scanner.nextLine();

            System.out.print("Fecha implantación (YYYY-MM-DD): ");
            LocalDate fecha = LocalDate.parse(scanner.nextLine());

            System.out.print("Veterinaria: ");
            String vet = scanner.nextLine();

            System.out.print("Observaciones (opcional): ");
            String obs = scanner.nextLine();

            Microchip m = new Microchip(null, false, codigo, fecha, vet, obs);
            microchipService.insertar(m);

            System.out.println("✅ Microchip registrado con ID: " + m.getId());

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    
    private void listarMicrochips() {
        try {
            microchipService.getAll().forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    
    private void buscarMicrochipPorId() {
        try {
            System.out.print("ID: ");
            Long id = Long.parseLong(scanner.nextLine());

            Microchip m = microchipService.getById(id);
            System.out.println(m != null ? m : "No encontrado");

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    
    private void buscarMicrochipPorCodigo() {
        try {
            System.out.print("Código: ");
            String codigo = scanner.nextLine();

            Microchip m = microchipService.getByCodigo(codigo);
            System.out.println(m != null ? m : "No encontrado");

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    
    private void actualizarMicrochip() {
        try {
            System.out.print("ID: ");
            Long id = Long.parseLong(scanner.nextLine());

            Microchip m = microchipService.getById(id);
            if (m == null) {
                System.out.println("Microchip no encontrado");
                return;
            }

            System.out.println("Deje vacío para mantener valor actual:");

            System.out.print("Código (" + m.getCodigo() + "): ");
            String codigo = scanner.nextLine();
            if (!codigo.isBlank()) m.setCodigo(codigo);

            System.out.print("Fecha implantación (" + m.getFechaImplantacion() + "): ");
            String fecha = scanner.nextLine();
            if (!fecha.isBlank()) m.setFechaImplantacion(LocalDate.parse(fecha));

            System.out.print("Veterinaria (" + m.getVeterinaria() + "): ");
            String vet = scanner.nextLine();
            if (!vet.isBlank()) m.setVeterinaria(vet);

            System.out.print("Observaciones (" + m.getObservaciones() + "): ");
            String obs = scanner.nextLine();
            if (!obs.isBlank()) m.setObservaciones(obs);

            microchipService.actualizar(m);

            System.out.println("✅ Microchip actualizado");

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    
    private void eliminarMicrochip() {
        try {
            System.out.print("ID: ");
            Long id = Long.parseLong(scanner.nextLine());

            microchipService.eliminar(id);
            System.out.println("✅ Eliminado");

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
    
}

