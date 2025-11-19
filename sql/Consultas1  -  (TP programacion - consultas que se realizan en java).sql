-- ============================================================
--  SCRIPTS DEL TRABAJO PRÁCTICO
--  Sistema de Gestión de Mascotas y Microchips
--  Incluye todas las operaciones del menú con comentarios
-- ============================================================


-- ============================================================
--                  TABLAS (Referencia)
-- ============================================================
-- mascota(id, nombre, especie, raza, fecha_nacimiento, dueno, microchip_codigo)
-- microchip(codigo, fecha_implantacion, veterinaria, observaciones)
-- ============================================================


-- ============================================================
--                  SECCIÓN 1: MASCOTAS
-- ============================================================


-- ------------------------------------------------------------
-- 1.1 REGISTRAR MASCOTA (Con microchip)
-- ------------------------------------------------------------
INSERT INTO mascota (nombre, especie, raza, fecha_nacimiento, dueno, microchip_codigo)
VALUES ('Mascota_1101', 'Gato', 'Raza_Y', '2020-10-10', 'Dueño_01101', 'MC-11001');


-- ------------------------------------------------------------
-- 1.1 REGISTRAR MASCOTA (Sin microchip)
-- ------------------------------------------------------------
INSERT INTO mascota (nombre, especie, raza, fecha_nacimiento, dueno, microchip_codigo)
VALUES ('Mascota_1001', 'Perro', 'Raza_X', '2018-08-15', 'Dueño_01001', NULL);


-- ------------------------------------------------------------
-- 1.2 LISTAR TODAS LAS MASCOTAS
-- ------------------------------------------------------------
SELECT * FROM mascota;


-- ------------------------------------------------------------
-- 1.3 BUSCAR MASCOTA POR ID
-- ------------------------------------------------------------
SELECT * FROM mascota WHERE id = 10005;


-- ------------------------------------------------------------
-- 1.4 ACTUALIZAR MASCOTA
-- ------------------------------------------------------------
UPDATE mascota
SET nombre = 'NuevoNombre',
    especie = 'NuevaEspecie',
    raza = 'NuevaRaza',
    fecha_nacimiento = '2020-01-01',
    dueno = 'NuevoDueño'
WHERE id = 10005;


-- ------------------------------------------------------------
-- 1.5 ELIMINAR MASCOTA
-- ------------------------------------------------------------
DELETE FROM mascota
WHERE id = 10005;



-- ============================================================
--                  SECCIÓN 2: MICROCHIPS
-- ============================================================


-- ------------------------------------------------------------
-- 2.1 REGISTRAR MICROCHIP
-- ------------------------------------------------------------
INSERT INTO microchip (codigo, fecha_implantacion, veterinaria, observaciones)
VALUES ('MC-10001', '2023-12-01', 'Vet_David', 'Implante 1001');


-- ------------------------------------------------------------
-- 2.2 LISTAR MICROCHIPS
-- ------------------------------------------------------------
SELECT * FROM microchip;


-- ------------------------------------------------------------
-- 2.3 BUSCAR MICROCHIP POR CÓDIGO
-- ------------------------------------------------------------
SELECT * FROM microchip
WHERE codigo = 'MC-10001';


-- ------------------------------------------------------------
-- 2.4 ACTUALIZAR MICROCHIP
-- ------------------------------------------------------------
UPDATE microchip
SET fecha_implantacion = '2024-01-10',
    veterinaria = 'Vet_Actualizada',
    observaciones = 'Observación editada'
WHERE codigo = 'MC-10001';


-- ------------------------------------------------------------
-- 2.5 ELIMINAR MICROCHIP
-- ------------------------------------------------------------
DELETE FROM microchip
WHERE codigo = 'MC-10001';



-- ============================================================
--      SECCIÓN 3: ASOCIACIÓN ENTRE MASCOTA Y MICROCHIP
-- ============================================================


-- ------------------------------------------------------------
-- 3.1 VERIFICAR QUE LA MASCOTA EXISTE
-- ------------------------------------------------------------
SELECT * FROM mascota WHERE id = 10005;


-- ------------------------------------------------------------
-- 3.2 VERIFICAR QUE EL MICROCHIP EXISTE
-- ------------------------------------------------------------
SELECT * FROM microchip WHERE codigo = 'MC-10001';


-- ------------------------------------------------------------
-- 3.3 VERIFICAR QUE EL MICROCHIP NO ESTÁ ASIGNADO A OTRA MASCOTA
-- (Si este SELECT devuelve una fila → no se puede asignar)
-- ------------------------------------------------------------
SELECT * FROM mascota
WHERE microchip_codigo = 'MC-10001';


-- ------------------------------------------------------------
-- 3.4 ASOCIAR MICROCHIP A UNA MASCOTA
-- (Esta es la operación final que en Java corre dentro de una transacción)
-- ------------------------------------------------------------
UPDATE mascota
SET microchip_codigo = 'MC-10001'
WHERE id = 10005;



-- ============================================================
--                  SECCIÓN 4: DESASOCIACIÓN
-- ============================================================


-- ------------------------------------------------------------
-- 4.1 QUITAR MICROCHIP DE UNA MASCOTA
-- ------------------------------------------------------------
UPDATE mascota
SET microchip_codigo = NULL
WHERE id = 10005;
