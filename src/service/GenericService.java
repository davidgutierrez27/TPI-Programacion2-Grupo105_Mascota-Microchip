
package service;

import java.util.List;

/**
 * Interfaz genérica para operaciones CRUD a nivel de negocio.
 *
 * Esta interfaz abstrae las acciones comunes que realizan los servicios
 * sobre cualquier entidad del dominio.
 *
 * Propósito:
 * ----------
 * - Unificar la firma de métodos entre distintos servicios.
 * - Permitir implementaciones reutilizables y desacopladas del DAO.
 * - Servir como capa entre el menú (presentación) y el DAO (persistencia).
 *
 * Operaciones:
 * ------------
 * - insertar: Alta de una nueva entidad.
 * - actualizar: Modifica una entidad existente.
 * - eliminar: Baja lógica (eliminado = true).
 * - getById: Obtiene una entidad por ID.
 * - getAll: Lista completa, sin eliminados.
 */

public interface GenericService<T> {

    T insertar(T t) throws Exception;
    T actualizar(T t) throws Exception;
    void eliminar(Long id) throws Exception;
    T getById(Long id) throws Exception;
    List<T> getAll() throws Exception;
}

