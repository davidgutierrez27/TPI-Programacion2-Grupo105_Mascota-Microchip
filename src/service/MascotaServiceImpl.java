
package service;

import config.TransactionManager;
import dao.MascotaDAO;
import dao.MascotaDAOImpl;
import dao.MicrochipDAO;
import dao.MicrochipDAOImpl;
import model.Mascota;
import model.Microchip;

import java.sql.Connection;
import java.util.List;

/**********************************************************************
 * Implementación del servicio de Mascota.
 *
 * Responsabilidades:
 * ------------------
 * - Validar campos obligatorios según reglas del dominio.
 * - Orquestar operaciones que involucran a Mascota y Microchip.
 * - Manejar transacciones completas con commit/rollback.
 * - Delegar la persistencia a los DAOs correspondientes.
 */

public class MascotaServiceImpl implements MascotaService {

    /** DAOs utilizados para persistencia */
    private final MascotaDAO mascotaDAO = new MascotaDAOImpl();
    private final MicrochipDAO chipDAO = new MicrochipDAOImpl();
  
    @Override
    public Mascota insertar(Mascota m) throws Exception {
        validate(m);
        try (TransactionManager tx = new TransactionManager()) {
            mascotaDAO.crear(m, tx.getConnection());
            tx.commit();
            return m;
        }
    }

    @Override
    public Mascota insertarConChip(Mascota mascota) throws Exception {
        validate(mascota);

        try (TransactionManager tx = new TransactionManager()) {
            Connection conn = tx.getConnection();

            // Si la mascota trae un chip nuevo, primero se persiste el microchip
            if (mascota.getMicrochip() != null &&
                mascota.getMicrochip().getId() == null) {

                chipDAO.crear(mascota.getMicrochip(), conn);
            }

            // Luego se inserta la mascota con la FK correspondiente
            mascotaDAO.crear(mascota, conn);

            tx.commit();
            return mascota;

        } catch (Exception e) {
            throw e;  // rollback lo hace TransactionManager al cerrar
        }
    }

    @Override
    public Mascota actualizar(Mascota mascota) throws Exception {
        if (mascota.getId() == null)
            throw new IllegalArgumentException("ID requerido para actualizar");

        validate(mascota);

        try (TransactionManager tx = new TransactionManager()) {
            mascotaDAO.actualizar(mascota, tx.getConnection());
            tx.commit();
            return mascota;
        }
    }

    @Override
    public void eliminar(Long id) throws Exception {
        try (TransactionManager tx = new TransactionManager()) {
            mascotaDAO.eliminar(id, tx.getConnection());
            tx.commit();
        }
    }

    @Override
    public Mascota getById(Long id) throws Exception {
        try (TransactionManager tx = new TransactionManager()) {
            return mascotaDAO.leer(id, tx.getConnection());
        }
    }

    @Override
    public List<Mascota> getAll() throws Exception {
        try (TransactionManager tx = new TransactionManager()) {
            return mascotaDAO.leerTodos(tx.getConnection());
        }
    }

    @Override
    public List<Mascota> buscarPorNombre(String like) throws Exception {
        try (TransactionManager tx = new TransactionManager()) {
            return mascotaDAO.buscarPorNombre(like, tx.getConnection());
        }
    }

    /**
     * Validaciones mínimas de negocio para Mascota.
     */
    private void validate(Mascota m) {
        if (m.getNombre() == null || m.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio");

        if (m.getEspecie() == null || m.getEspecie().isBlank())
            throw new IllegalArgumentException("La especie es obligatoria");

        if (m.getDuenio() == null || m.getDuenio().isBlank())
            throw new IllegalArgumentException("El nombre del dueño es obligatorio");

        // Regla: si viene un microchip, debe tener código válido
        if (m.getMicrochip() != null &&
            (m.getMicrochip().getCodigo() == null || m.getMicrochip().getCodigo().isBlank()))
            throw new IllegalArgumentException("El microchip debe tener un código válido");
    }
}

