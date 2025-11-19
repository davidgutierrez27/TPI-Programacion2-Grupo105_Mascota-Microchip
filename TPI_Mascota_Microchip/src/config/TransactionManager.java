
package config;

import java.sql.Connection;
import java.sql.SQLException;

/**********************************************************************
 * Manejador de transacciones.
 *
 * Responsabilidades:
 * ------------------
 * - Obtener una conexión desde DatabaseConnection.
 * - Desactivar autocommit para iniciar una transacción manual.
 * - Exponer la misma conexión para que DAOs y Services trabajen juntos.
 * - Realizar commit() o rollback() según corresponda.
 * - Restaurar autocommit y cerrar la conexión al finalizar.
 *
 * Usos típicos:
 * -------------
 * try (TransactionManager tx = new TransactionManager()) {
 *      Connection c = tx.getConnection();
 *
 *      // varias operaciones DAO usando la misma conexión
 *      dao.crear(..., c);
 *      dao.actualizar(..., c);
 *
 *      tx.commit();  // si todo va bien
 * } catch (Exception e) {
 *      // rollback automático al cerrar (silencioso)
 * }
 *
 * Beneficios:
 * -----------
 * - Evita fugas de conexiones.
 * - Código transaccional ordenado y fácil de leer.
 * - Reduce repetición de boilerplate.
 */


public class TransactionManager implements AutoCloseable {

    /** Conexión única que participa de la transacción */
    private final Connection connection;

    
    /**********************************************************************
     * Constructor:
     * - Obtiene una conexión desde DatabaseConnection
     * - Desactiva autocommit (comienza una transacción manual)
     * - Si falla, lanza SQLException y NO queda ninguna conexión abierta
     */
    public TransactionManager() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
        this.connection.setAutoCommit(false);  // inicio real de la transacción
    }

    
    /**********************************************************************
     * Devuelve la conexión transaccional.
     * Esta conexión debe pasarse explícitamente a los DAOs.
     */
    public Connection getConnection() {
        return connection;
    }

    
    /**********************************************************************
     * Confirma todos los cambios realizados dentro de la transacción.
     * Si falla el commit, la excepción debe ser manejada por el servicio.
     */
    public void commit() throws SQLException {
        connection.commit();
    }

    
    /**********************************************************************
     * Ejecuta rollback sin lanzar excepción.
     * Se usa cuando ocurre algún error en el try principal.
     */
    public void rollbackSilently() {
        try {
            connection.rollback();
        } catch (SQLException ignored) {
            // fallo del rollback → no se puede hacer mucho más
        }
    }

    
    
    /**
     * Cierre automático (gracias a AutoCloseable).
     *
     * Orden:
     * 1) Si autocommit está desactivado → restaurarlo a true
     * 2) Cerrar la conexión
     * 3) Si algo falla, no propagamos la excepción para no
     *    ensuciar el flujo original del Service
     *
     * Nota:
     * - No hace rollback automático. Ese es trabajo del Service.
     * - El Service debe llamar a tx.rollbackSilently() en el catch.
     */
    @Override
    public void close() {
        try {
            if (!connection.getAutoCommit()) {
                connection.setAutoCommit(true);  // restaurar estado original
            }
        } catch (SQLException ignored) {}

        try {
            connection.close();
        } catch (SQLException ignored) {}
    }
}

