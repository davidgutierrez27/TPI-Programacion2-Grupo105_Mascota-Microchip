
package dao;

import model.Mascota;
import model.Microchip;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*********************************************************************
 * Implementación JDBC del DAO para Mascota.
 *
 * Características:
 * ----------------
 * - Maneja relación 1→1 mediante LEFT JOIN a microchip.
 * - Inserta/actualiza FK id_microchip_fk (puede ser NULL).
 * - Soft-delete (eliminado = 1).
 * - Búsqueda por nombre con LIKE.
 *
 * Patrón:
 * -------
 * - Todas las consultas se hacen con PreparedStatement.
 * - Manejo de ResultSet seguro y limpio.
 * - Mapeo completo de Mascota + Microchip.
 */

public class MascotaDAOImpl implements MascotaDAO {

    /** SQL: Inserción */
    private static final String INSERT_SQL =
            "INSERT INTO mascota (nombre, especie, raza, fecha_nacimiento, duenio, id_microchip_fk) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    /** SQL: Actualización */
    private static final String UPDATE_SQL =
            "UPDATE mascota SET nombre=?, especie=?, raza=?, fecha_nacimiento=?, duenio=?, id_microchip_fk=? " +
            "WHERE id_mascota=?";

    /** SQL: Soft-delete */
    private static final String DELETE_SQL =
            "UPDATE mascota SET eliminado=1 WHERE id_mascota=?";

    /** SQL: SELECT por ID con JOIN */
    private static final String SELECT_BY_ID_SQL =
            "SELECT m.*, c.id_microchip, c.codigo, c.fecha_implantacion, c.veterinaria, c.observaciones, c.eliminado AS chip_eliminado " +
            "FROM mascota m " +
            "LEFT JOIN microchip c ON m.id_microchip_fk = c.id_microchip " +
            "WHERE m.id_mascota=? AND m.eliminado=0";

    /** SQL: SELECT todos */
    private static final String SELECT_ALL_SQL =
            "SELECT m.*, c.id_microchip, c.codigo, c.fecha_implantacion, c.veterinaria, c.observaciones, c.eliminado AS chip_eliminado " +
            "FROM mascota m " +
            "LEFT JOIN microchip c ON m.id_microchip_fk = c.id_microchip " +
            "WHERE m.eliminado=0";

    /** SQL: Búsqueda por nombre */
    private static final String SEARCH_BY_NAME_SQL =
            "SELECT m.*, c.id_microchip, c.codigo, c.fecha_implantacion, c.veterinaria, c.observaciones, c.eliminado AS chip_eliminado " +
            "FROM mascota m " +
            "LEFT JOIN microchip c ON m.id_microchip_fk = c.id_microchip " +
            "WHERE m.eliminado=0 AND m.nombre LIKE ?";

    @Override
    public void crear(Mascota m, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, m.getNombre());
            stmt.setString(2, m.getEspecie());
            stmt.setString(3, m.getRaza());

            if (m.getFechaNacimiento() != null)
                stmt.setDate(4, Date.valueOf(m.getFechaNacimiento()));
            else
                stmt.setNull(4, Types.DATE);

            stmt.setString(5, m.getDuenio());

            if (m.getMicrochip() != null)
                stmt.setLong(6, m.getMicrochip().getId());
            else
                stmt.setNull(6, Types.BIGINT);

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    m.setId(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public Mascota leer(Long id, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return mapToMascota(rs);
            }
        }
        return null;
    }

    @Override
    public List<Mascota> leerTodos(Connection conn) throws Exception {
        List<Mascota> lista = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapToMascota(rs));
            }
        }

        return lista;
    }

    @Override
    public List<Mascota> buscarPorNombre(String nombreLike, Connection conn) throws Exception {
        List<Mascota> lista = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(SEARCH_BY_NAME_SQL)) {
            stmt.setString(1, "%" + nombreLike + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapToMascota(rs));
                }
            }
        }

        return lista;
    }

    @Override
    public void actualizar(Mascota m, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

            stmt.setString(1, m.getNombre());
            stmt.setString(2, m.getEspecie());
            stmt.setString(3, m.getRaza());

            if (m.getFechaNacimiento() != null)
                stmt.setDate(4, Date.valueOf(m.getFechaNacimiento()));
            else
                stmt.setNull(4, Types.DATE);

            stmt.setString(5, m.getDuenio());

            if (m.getMicrochip() != null)
                stmt.setLong(6, m.getMicrochip().getId());
            else
                stmt.setNull(6, Types.BIGINT);

            stmt.setLong(7, m.getId());

            int rows = stmt.executeUpdate();
            if (rows == 0)
                throw new SQLException("No se encontró mascota con ID: " + m.getId());
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
     * Mapeo auxiliar ResultSet → Mascota (con su microchip si existe).
     */
    private Mascota mapToMascota(ResultSet rs) throws SQLException {

        Mascota m = new Mascota();

        m.setId(rs.getLong("id_mascota"));
        m.setEliminado(rs.getBoolean("eliminado"));
        m.setNombre(rs.getString("nombre"));
        m.setEspecie(rs.getString("especie"));
        m.setRaza(rs.getString("raza"));

        Date nac = rs.getDate("fecha_nacimiento");
        m.setFechaNacimiento(nac != null ? nac.toLocalDate() : null);

        m.setDuenio(rs.getString("duenio"));

        Long idChip = rs.getLong("id_microchip_fk");
        if (idChip != null && idChip > 0 && !rs.wasNull()) {
            Microchip chip = new Microchip();
            chip.setId(rs.getLong("id_microchip"));
            chip.setCodigo(rs.getString("codigo"));
            chip.setFechaImplantacion(rs.getDate("fecha_implantacion").toLocalDate());
            chip.setVeterinaria(rs.getString("veterinaria"));
            chip.setObservaciones(rs.getString("observaciones"));
            chip.setEliminado(rs.getBoolean("chip_eliminado"));
            m.setMicrochip(chip);
        }

        return m;
    }
}

