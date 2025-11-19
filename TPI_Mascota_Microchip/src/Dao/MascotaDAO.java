
package dao;

import model.Mascota;

import java.sql.Connection;
import java.util.List;

/**
 * DAO especializado para Mascota.
 *
 * Extiende GenericDAO e incorpora una búsqueda adicional:
 * - buscarPorNombre: búsqueda parcial con LIKE.
 */

public interface MascotaDAO extends GenericDAO<Mascota> {

    List<Mascota> buscarPorNombre(String nombreLike, Connection conn) throws Exception;
}

