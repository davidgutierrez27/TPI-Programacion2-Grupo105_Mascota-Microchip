
package dao;

import model.Microchip;

/**
 * Interfaz especializada del DAO para Microchip.
 *
 * Extiende GenericDAO e incorpora una búsqueda adicional:
 * - leerPorCodigo: retorna un Microchip por su código único.
 */

public interface MicrochipDAO extends GenericDAO<Microchip> {

    Microchip leerPorCodigo(String codigo, java.sql.Connection conn) throws Exception;
}

