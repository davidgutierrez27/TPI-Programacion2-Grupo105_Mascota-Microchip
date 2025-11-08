
package main;

import config.DatabaseConnection;
import java.sql.Connection;

/**
 * Prueba simple de conexión a la base de datos.
 *
 * Permite validar:
 * ---------------
 * - Lectura correcta de db.properties.
 * - Driver JDBC cargado.
 * - Credenciales válidas.
 *
 * Su uso típico es ejecutar esta clase antes de iniciar el desarrollo
 * para asegurarse de que la aplicación puede comunicarse con MySQL.
 */

public class TestConexion {

    public static void main(String[] args) {
        System.out.println("Probando conexión a la base de datos...");
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("✅ Conexión exitosa a MySQL!");
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}

