
package service;

import model.Mascota;

import java.util.List;

/********************************************************************
 * Servicio específico para la entidad Mascota.
 *
 * Funciones adicionales respecto al CRUD genérico:
 * ------------------------------------------------
 * - búsqueda por nombre (campo relevante según el TFI).
 * - inserción transaccional Mascota + Microchip.
 *
 * Reglas:
 * -------
 * insertarConChip() crea primero el microchip (si no existe),
 * y luego la mascota que lo referencia, en una única transacción.
 */

public interface MascotaService extends GenericService<Mascota> {

    // Búsqueda por coincidencia parcial de nombre
    List<Mascota> buscarPorNombre(String like) throws Exception;
    
    
    // Inserta una mascota junto con su microchip (si trae uno).
    Mascota insertarConChip(Mascota mascota) throws Exception;
}

