
package dao;

import model.Microchip;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/********************************************************************
 * Implementación JDBC del DAO para Microchip.
 *
 * Características:
 * ----------------
 * - Implementa todas las operaciones de persistencia con PreparedStatement.
 * - Soft-delete mediante campo eliminado = 1.
 * - Búsqueda por código único.
 * - Inserción con obtención de claves generadas.
 * - Mapeo completo desde ResultSet a entidad Microchip.
 *
 * Relaciones:
 * -----------
 * Microchip no referencia a ninguna entidad (lado B independiente).
 */

public class MicrochipDAOImpl implements MicrochipDAO {

    /** SQL: Inserción de microchip */
    private static final String INSERT_SQL =
            "INSERT INTO microchip (codigo, fecha_implantacion, veterinaria, observaciones) " +
            "VALUES (?, ?, ?, ?)";

    /** SQL: Actualización */
    private static final String UPDATE_SQL =
            "UPDATE microchip SET codigo=?, fecha_implantacion=?, veterinaria=?, observaciones=? " +
            "WHERE id_microchip=?";

    /** SQL: Soft-delete */
    private static final String DELETE_SQL =
            "UPDATE microchip SET eliminado = 1 WHERE id_microchip=?";

    /** SQL: SELECT por ID */
    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM microchip WHERE id_microchip=? AND eliminado=0";

    /** SQL: SELECT por código */
    private static final String SELECT_BY_CODIGO_SQL =
            "SELECT * FROM microchip WHERE codigo=? AND eliminado=0";

    /** SQL: SELECT todos */
    private static final String SELECT_ALL_SQL =
            "SELECT * FROM microchip WHERE eliminado=0";

    @Override
    public void crear(Microchip m, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, m.getCodigo());
            stmt.setDate(2, Date.valueOf(m.getFechaImplantacion()));
            stmt.setString(3, m.getVeterinaria());
            stmt.setString(4, m.getObservaciones());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    m.setId(keys.getLong(1));
                } else {
                    throw new SQLException("No se pudo obtener el ID generado del microchip.");
                }
            }
        }
    }

    @Override
    public Microchip leer(Long id, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToMicrochip(rs);
                }
            }
        }
        return null;
    }

    @Override
    public Microchip leerPorCodigo(String codigo, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_BY_CODIGO_SQL)) {
            stmt.setString(1, codigo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToMicrochip(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Microchip> leerTodos(Connection conn) throws Exception {
        List<Microchip> lista = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapToMicrochip(rs));
            }
        }
        return lista;
    }

    @Override
    public void actualizar(Microchip m, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

            stmt.setString(1, m.getCodigo());
            stmt.setDate(2, Date.valueOf(m.getFechaImplantacion()));
            stmt.setString(3, m.getVeterinaria());
            stmt.setString(4, m.getObservaciones());
            stmt.setLong(5, m.getId());

            int rows = stmt.executeUpdate();
            if (rows == 0)
                throw new SQLException("No se encontró microchip con ID: " + m.getId());
        }
    }

    @Override
    public void eliminar(Long id, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    
    /**
     * Mapeo auxiliar de ResultSet → Microchip.
     */
    private Microchip mapToMicrochip(ResultSet rs) throws SQLException {
        Microchip m = new Microchip();

        m.setId(rs.getLong("id_microchip"));
        m.setEliminado(rs.getBoolean("eliminado"));
        m.setCodigo(rs.getString("codigo"));
        m.setFechaImplantacion(rs.getDate("fecha_implantacion").toLocalDate());
        m.setVeterinaria(rs.getString("veterinaria"));
        m.setObservaciones(rs.getString("observaciones"));

        return m;
    }
}

