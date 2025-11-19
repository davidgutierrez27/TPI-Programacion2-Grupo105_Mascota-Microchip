
package service;

import config.TransactionManager;
import dao.MicrochipDAO;
import dao.MicrochipDAOImpl;
import model.Microchip;

import java.sql.Connection;
import java.util.List;

/********************************************************************
 * Implementación del servicio de Microchip.
 *
 * Responsabilidades:
 * ------------------
 * - Validar datos según reglas de negocio antes de persistir.
 * - Coordinar llamadas al DAO dentro de una transacción.
 * - Gestionar commit/rollback a través de TransactionManager.
 * - Proveer una API simple para la capa de presentación.
 */

public class MicrochipServiceImpl implements MicrochipService {

    /** DAO concreto para acceso a datos */
    private final MicrochipDAO chipDAO = new MicrochipDAOImpl();
   
    @Override
    public Microchip insertar(Microchip m) throws Exception {
        validate(m);
        try (TransactionManager tx = new TransactionManager()) {
            chipDAO.crear(m, tx.getConnection());
            tx.commit();
            return m;
        }
    }

    @Override
    public Microchip actualizar(Microchip m) throws Exception {
        if (m.getId() == null)
            throw new IllegalArgumentException("ID requerido para actualizar");

        validate(m);

        try (TransactionManager tx = new TransactionManager()) {
            chipDAO.actualizar(m, tx.getConnection());
            tx.commit();
            return m;
        }
    }

    @Override
    public void eliminar(Long id) throws Exception {
        try (TransactionManager tx = new TransactionManager()) {
            chipDAO.eliminar(id, tx.getConnection());
            tx.commit();
        }
    }

    @Override
    public Microchip getById(Long id) throws Exception {
        try (TransactionManager tx = new TransactionManager()) {
            return chipDAO.leer(id, tx.getConnection());
        }
    }
    
    @Override
    public List<Microchip> getAll() throws Exception {
        try (TransactionManager tx = new TransactionManager()) {
            return chipDAO.leerTodos(tx.getConnection());
        }
    }

    @Override
    public Microchip getByCodigo(String codigo) throws Exception {
        try (TransactionManager tx = new TransactionManager()) {
            return chipDAO.leerPorCodigo(codigo, tx.getConnection());
        }
    }

    
    
    /********************************************************************
     * Validaciones de negocio específicas del dominio:
     * - código no puede ser nulo ni vacío
     * - fecha de implantación obligatoria
     * - veterinaria obligatoria
     */
    private void validate(Microchip m) {
        if (m.getCodigo() == null || m.getCodigo().isBlank())
            throw new IllegalArgumentException("El código de microchip es obligatorio");

        if (m.getFechaImplantacion() == null)
            throw new IllegalArgumentException("La fecha de implantación es obligatoria");

        if (m.getVeterinaria() == null || m.getVeterinaria().isBlank())
            throw new IllegalArgumentException("El nombre de la veterinaria es obligatorio");
    }
}

