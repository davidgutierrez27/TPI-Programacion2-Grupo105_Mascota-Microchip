
package dao;

import java.sql.Connection;
import java.util.List;

/**
 * Interfaz genérica para Data Access Objects (DAO).
 *
 * Provee las operaciones CRUD estándar que deben implementarse
 * para cualquier entidad del dominio.
 *
 * Todas las operaciones reciben una Connection externa para permitir:
 * - Participación en transacciones coordinadas.
 * - Manejo de commit/rollback desde la capa Service.
 *
 * Operaciones:
 * ------------
 * - crear       → INSERT
 * - leer        → SELECT por PK
 * - leerTodos   → SELECT *
 * - actualizar  → UPDATE
 * - eliminar    → Soft-delete (eliminado = TRUE)
 */

public interface GenericDAO<T> {

    void crear(T t, Connection conn) throws Exception;
    T leer(Long id, Connection conn) throws Exception;
    List<T> leerTodos(Connection conn) throws Exception;
    void actualizar(T t, Connection conn) throws Exception;
    void eliminar(Long id, Connection conn) throws Exception;
}
