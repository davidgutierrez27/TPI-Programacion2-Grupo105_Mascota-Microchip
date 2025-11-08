package model;

import java.time.LocalDate;

/*******************************************************************
 * Entidad Microchip (lado B de la relación 1→1).
 *
 * Representa un microchip implantado en una mascota.
 * Es una entidad independiente con su propia tabla, y puede existir
 * antes de ser asociada a una Mascota.
 *
 * Reglas según DB:
 * ----------------
 * - id: PK autoincremental.
 * - codigo: único y obligatorio.
 * - fechaImplantacion: obligatoria.
 * - veterinaria: obligatoria.
 * - eliminado: baja lógica.
 *
 * Relación:
 * ---------
 * No referencia a Mascota (relación unidireccional desde A→B).
 */

public class Microchip {

    /** Identificador único (PK) */
    private Long id;

    /** Indicador de baja lógica */
    private boolean eliminado;

    /** Código único del microchip (UNIQUE NOT NULL) */
    private String codigo;

    /** Fecha en que se implantó el chip */
    private LocalDate fechaImplantacion;

    /** Veterinaria responsable */
    private String veterinaria;

    /** Texto libre (opcional) */
    private String observaciones;

    /** Constructor vacío requerido para JDBC/DAO */
    public Microchip() {}

    /** Constructor completo */
    public Microchip(Long id, boolean eliminado, String codigo,
                     LocalDate fechaImplantacion, String veterinaria,
                     String observaciones) {
        this.id = id;
        this.eliminado = eliminado;
        this.codigo = codigo;
        this.fechaImplantacion = fechaImplantacion;
        this.veterinaria = veterinaria;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public LocalDate getFechaImplantacion() { return fechaImplantacion; }
    public void setFechaImplantacion(LocalDate fechaImplantacion) { this.fechaImplantacion = fechaImplantacion; }

    public String getVeterinaria() { return veterinaria; }
    public void setVeterinaria(String veterinaria) { this.veterinaria = veterinaria; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    @Override
    public String toString() {
        return "Microchip{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", fechaImplantacion=" + fechaImplantacion +
                ", veterinaria='" + veterinaria + '\'' +
                ", eliminado=" + eliminado +
                '}';
    }
}

