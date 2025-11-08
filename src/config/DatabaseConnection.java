package config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


/**********************************************************************
 * Gestión centralizada de conexiones JDBC a MySQL.
 *
 * Principios clave:
 * -----------------
 * - Uso de archivo externo db.properties para configuración flexible.
 * - Carga de configuración en un bloque static (ocurre solo una vez).
 * - Fail-fast: si algo está mal configurado, la aplicación se detiene
 *   inmediatamente para evitar errores silenciosos.
 * - Clase utilitaria (no instanciable).
 *
 * Beneficios:
 * -----------
 * - El código de la aplicación queda desacoplado de credenciales sensibles.
 * - Permite cambiar la BD sin recompilar (solo ajustando el properties).
 * - Facilita testing y distintos entornos (desarrollo / producción).
 */


public final class DatabaseConnection {

    /** URL, usuario y contraeña JDBC obtenida desde db.properties */
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    /** Nombre del archivo de configuración en /resources */
    private static final String CONFIG_FILE = "db.properties";

    
    
    
    /********************************************************************
     * Bloque estático de inicialización.
     *
     * Lo que ocurre aquí sucede una sola vez cuando la clase es cargada.
     * Orden de ejecución:
     * 1) Cargar archivo de properties
     * 2) Asignar credenciales
     * 3) Cargar driver JDBC de MySQL
     * 4) Validar configuración (fail-fast)
     *
     * Si algo falla → ExceptionInInitializerError
     * La intención es que sin BD válida la aplicación NO continúe.
     */
    static {
        try {
            // 1. Cargar configuración desde /resources/db.properties
            Properties props = loadProperties();

            // 2. Asignar propiedades a campos estáticos
            URL = props.getProperty("db.url");
            USER = props.getProperty("db.user");
            PASSWORD = props.getProperty("db.password", ""); // clave vacía por defecto

            // 3. Registrar manualmente el driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 4. Validar la configuración
            validateConfiguration();

            // Posibles Excepciones
        } catch (IOException e) {
            throw new ExceptionInInitializerError(
                    "Error al cargar " + CONFIG_FILE + ": " + e.getMessage()
            );
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(
                    "Driver MySQL no encontrado: " + e.getMessage()
            );
        } catch (IllegalStateException e) {
            throw new ExceptionInInitializerError(
                    "Configuración inválida: " + e.getMessage()
            );
        }
    }

    
    
    /**********************************************************************
     * Constructor privado:
     * Previene completamente que la clase sea instanciada.
     * Esta clase expone solo métodos estáticos.
     */
    private DatabaseConnection() {
        throw new UnsupportedOperationException("Clase utilitaria: no debe instanciarse");
    }

    
    
    /**********************************************************************
     * Obtiene una nueva conexión JDBC hacia la base MySQL indicada en el properties.
     *
     * Importante:
     * -----------
     * - Cada llamada devuelve una conexión distinta.
     * - El consumidor es responsable de cerrarla.
     * - Para transacciones, se recomienda envolver la conexión en TransactionManager.
     *
     * @return Conexión JDBC activa y lista para usar
     * @throws SQLException si no se puede establecer conexión
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    
    
    /**********************************************************************
     * Carga el archivo db.properties desde el classpath.
     *
     * El archivo debe ubicarse en:
     *    src/main/resources/db.properties
     *
     * @throws IOException si el archivo no existe o no puede ser leído
     */
    private static Properties loadProperties() throws IOException {
        Properties props = new Properties();

        try (InputStream input = DatabaseConnection.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            if (input == null) {
                throw new IOException("Archivo no encontrado en classpath: " + CONFIG_FILE);
            }

            props.load(input);
        }

        return props;
    }

    
    
    /**********************************************************************
     * Validación estricta de parámetros mínimos.
     *
     * Se ejecuta solo una vez durante la carga de clase.
     * PASSWORD puede ser vacío (MySQL root local),
     * pero nunca null.
     *
     * @throws IllegalStateException si algo obligatorio está mal configurado
     */
    private static void validateConfiguration() {
        if (URL == null || URL.isBlank())
            throw new IllegalStateException("db.url no puede estar vacío");
        if (USER == null || USER.isBlank())
            throw new IllegalStateException("db.user no puede estar vacío");
        if (PASSWORD == null)
            throw new IllegalStateException("db.password no puede ser null");
    }
}
