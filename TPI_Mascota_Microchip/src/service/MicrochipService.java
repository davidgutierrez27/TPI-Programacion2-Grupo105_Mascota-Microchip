
package service;

import model.Microchip;

/**********************************************************************
 * Servicio específico para la entidad Microchip.
 *
 * Extiende la interfaz genérica de servicios para incluir una operación
 * adicional relevante para el dominio:
 *
 * - getByCodigo: búsqueda por el campo "codigo" (UNIQUE).
 *
 * Esto permite validar unicidad, preexistencia de chips y búsquedas rápidas.
 */

public interface MicrochipService extends GenericService<Microchip> {

    /**
     * Busca un microchip por su código único.
     *
     * @param codigo valor exacto del código del microchip
     * @return microchip encontrado o null si no existe
     */
    
    Microchip getByCodigo(String codigo) throws Exception;
}
