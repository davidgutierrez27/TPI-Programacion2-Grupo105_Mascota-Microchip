-- =============================================
-- ETAPA 4 - Seguridad e Integridad
-- Base: tpi_mascota_microchip
-- Ejecutar con un usuario administrador (root)
-- =============================================


-- 4.a) Violación de UNIQUE (duplicar código de microchip)
-- (Asumir que ya existe un microchip con codigo 'MC001' de las cargas previas)
INSERT INTO microchip (codigo, fecha_implantacion, veterinaria, observaciones)
VALUES ('MC001', '2025-01-01', 'VetTest', 'Prueba duplicado');


-- 4.b) Violación de FK (usar id_microchip_fk inexistente)
INSERT INTO mascota (nombre, especie, raza, fecha_nacimiento, duenio, id_microchip_fk)
VALUES ('PruebaFK', 'Perro', 'Test', '2021-01-01', 'Tester', 999999);


-- 4.c) Violación de CHECK (especie fuera de dominio)
INSERT INTO mascota (nombre, especie, raza, fecha_nacimiento, duenio)
VALUES ('PruebaCheck', 'Dinosaurio', 'T-Rex', '2010-01-01', 'Tester');


-- 4.d) Prueba de privilegios mínimos: como admin_compania_veterinaria intentar DELETE (debe fallar)
-- Conectarse como admin_compania_veterinaria y ejecutar:
DELETE FROM microchip WHERE id_microchip = 1;
-- Esperado: ERROR 1142: DELETE command denied to user 'usuario_veterinario'@'localhost' for table 'microchip'


-- 5) Limpieza (si querés dejar la BD como antes de pruebas)
-- Eliminar filas de prueba si hubieran quedado (ejecutar con admin)
DELETE FROM mascota WHERE nombre IN ('PruebaFK','PruebaCheck');
DELETE FROM microchip WHERE codigo = 'MC001' AND /* si fue la original no eliminarla */ 0;





-- 1) Crear usuario con privilegios mínimos
CREATE USER 'admin_compania_veterinaria'@'localhost' IDENTIFIED BY 'adminVet9876';

-- Dar solo SELECT en toda la base
GRANT SELECT ON tpi_mascota_microchip.* TO 'admin_compania_veterinaria'@'localhost';

-- Permitir UPDATE solo en la tabla mascota (p.ej. para editar datos no sensibles)
GRANT UPDATE ON tpi_mascota_microchip.mascota TO 'admin_compania_veterinaria'@'localhost';

FLUSH PRIVILEGES;




-- 2) Crear dos vistas que oculten información sensible

-- Vista 1: pública - sin dueño ni observaciones
DROP VIEW IF EXISTS vista_mascotas_publica;
CREATE VIEW vista_mascotas_publica AS
SELECT 
    m.id_mascota,
    m.nombre AS nombre_mascota,
    m.especie,
    m.raza,
    mc.codigo AS numero_microchip,
    mc.fecha_implantacion
FROM mascota m
JOIN microchip mc ON m.id_microchip_fk = mc.id_microchip
WHERE m.eliminado = 0 AND mc.eliminado = 0;
SELECT * FROM tpi_mascota_microchip.vista_mascotas_publica;


-- Vista 2: veterinario - muestra dueño pero oculta observaciones internas del chip
DROP VIEW IF EXISTS vista_mascotas_veterinario;
CREATE VIEW vista_mascotas_veterinario AS
SELECT 
    m.id_mascota,
    m.nombre,
    m.especie,
    m.raza,
    m.duenio,
    mc.codigo AS numero_microchip,
    mc.veterinaria,
    mc.fecha_implantacion
FROM mascota m
JOIN microchip mc ON m.id_microchip_fk = mc.id_microchip
WHERE m.eliminado = 0;
SELECT * FROM tpi_mascota_microchip.vista_mascotas_veterinario;



-- NOTA: El usuario ya tenía SELECT global. Estos GRANT son útiles si en otros entornos
-- quisieras limitar solo a estas vistas.
-- GRANT SELECT ON tpi_mascota_microchip.vista_mascotas_publica TO 'admin_compania_veterinaria'@'localhost';
-- GRANT SELECT ON tpi_mascota_microchip.vista_mascotas_veterinario TO 'admin_compania_veterinaria'@'localhost';
-- FLUSH PRIVILEGES;

-- Comprobación rápida de permisos (opcional)
-- Intenta conectarte como admin_compania_veterinaria y hacer un SELECT de las vistas:
--   SELECT * FROM vista_mascotas_publica LIMIT 1;
--   SELECT * FROM vista_mascotas_veterinario LIMIT 1;

-- Si ocurre un error, verifica:
-- - Que las vistas existan (SHOW FULL TABLES IN tpi_mascota_microchip WHERE Tables_in_tpi_mascota_microchip LIKE 'vista_mascotas_%');
-- - Que el usuario actual tenga GRANT OPTION para conceder permisos sobre esas vistas (ejecutar como root).




-- 3) Procedimiento almacenado seguro (opción B): buscar mascotas por nombre
-- No se usa SQL dinámico; parámetro simple
-- Está diseñado sin usar SQL dinámico, lo que lo hace más seguro frente a ataques de inyección.
-- De esta forma, los usuarios pueden realizar consultas controladas sin acceder directamente a las tablas base,
-- lo que refuerza la seguridad y el control de acceso a los datos.
DELIMITER $$
DROP PROCEDURE IF EXISTS sp_buscar_mascota_por_nombre $$
CREATE PROCEDURE sp_buscar_mascota_por_nombre(IN p_nombre VARCHAR(100))
BEGIN
    SELECT 
        m.id_mascota,
        m.nombre,
        m.especie,
        m.raza,
        m.duenio,
        mc.codigo AS numero_microchip,
        mc.fecha_implantacion
    FROM mascota m
    LEFT JOIN microchip mc ON m.id_microchip_fk = mc.id_microchip
    WHERE m.eliminado = 0
      AND m.nombre = p_nombre;
END $$
DELIMITER ;









