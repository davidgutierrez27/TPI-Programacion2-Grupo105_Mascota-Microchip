package model;

import java.time.LocalDate;

/******************************************************************
 * Entidad Mascota (lado A de la relación 1→1).
 *
 * Esta entidad referencia un Microchip mediante un atributo de tipo Microchip,
 * lo que implementa la relación unidireccional A → B.
 *
 * Reglas según DB:
 * ----------------
 * - id: PK autoincremental.
 * - nombre: obligatorio.
 * - especie: obligatoria (valores permitidos según CHECK).
 * - raza: opcional.
 * - fechaNacimiento: opcional.
 * - duenio: obligatorio.
 * - eliminado: baja lógica.
 *
 * Relación:
 * ---------
 * - microchip: referencia 1→1.
 *   Se materializa en la BD mediante la FK UNIQUE id_microchip_fk en la tabla mascota.
 */


public class Mascota {

    /** Identificador único (PK) */
    private Long id;

    /** Indicador de baja lógica */
    private boolean eliminado;

    /** Nombre de la mascota (obligatorio) */
    private String nombre;

    /** Especie (Perro, Gato, Ave, Roedor) */
    private String especie;

    /** Raza (opcional) */
    private String raza;

    /** Fecha de nacimiento (opcional) */
    private LocalDate fechaNacimiento;

    /** Nombre del dueño (obligatorio) */
    private String duenio;

    /** Relación 1→1: referencia a Microchip */
    private Microchip microchip;

    /** Constructor vacío */
    public Mascota() {}

    
    
    /** Constructor completo */
    public Mascota(Long id, boolean eliminado, String nombre,
                   String especie, String raza, LocalDate fechaNacimiento,
                   String duenio, Microchip microchip) {
        this.id = id;
        this.eliminado = eliminado;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.fechaNacimiento = fechaNacimiento;
        this.duenio = duenio;
        this.microchip = microchip;
    }

    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }

    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getDuenio() { return duenio; }
    public void setDuenio(String duenio) { this.duenio = duenio; }

    public Microchip getMicrochip() { return microchip; }
    public void setMicrochip(Microchip microchip) { this.microchip = microchip; }

    @Override
    public String toString() {
        return "Mascota{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", especie='" + especie + '\'' +
                ", duenio='" + duenio + '\'' +
                ", chip=" + (microchip != null ? microchip.getCodigo() : "SIN-CHIP") +
                ", eliminado=" + eliminado +
                '}';
    }
}

