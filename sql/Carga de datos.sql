USE tpi_mascota_microchip;

-- 2. Creación y llenado de la Tabla Auxiliar de Números (Tally Table)
CREATE TABLE Numeros (n INT);
INSERT INTO Numeros VALUES (0), (1), (2), (3), (4), (5), (6), (7), (8), (9);
-- Bloque 2: Inserción en microchip (Tabla B: 10.000 Registros)
-- Generamos 10.000 chips. Los id_microchip se generarán automáticamente del 1 al 10.000. Código:

-- 3. Inserción de 10.000 registros en la tabla MICROCHIP (Tabla Referenciada)
INSERT INTO microchip (codigo, fecha_implantacion, veterinaria, observaciones)
SELECT
    -- Genera un código único del MC-00001 al MC-10000
    CONCAT('MC-', LPAD((t1.n + t2.n * 10 + t3.n * 100 + t4.n * 1000) + 1, 5, '0')),
    
    -- Fecha de implantación aleatoria en los últimos 4 años
    DATE_SUB(CURDATE(), INTERVAL FLOOR(RAND() * 1460) DAY), -- 1460 días ≈ 4 años
    
    -- Selecciona una de tus veterinarias aleatoriamente
    CONCAT('Vet_', ELT(1 + FLOOR(RAND() * 5), 'Brian', 'David', 'Veronica', 'Rosario', 'Luciana')),
    
    -- Observación genérica
    CONCAT('Implante ', (t1.n + t2.n * 10 + t3.n * 100 + t4.n * 1000) + 1)

FROM 
    Numeros t1, Numeros t2, Numeros t3, Numeros t4
-- Limita la inserción a 10.000, aunque con 4 JOINs ya son 10.000 exactos
LIMIT 10000;




-- Bloque 3: Inserción en mascota (Tabla A: 10.000 Registros)
-- Aquí garantizamos la integridad 1→1 al asignar directamente los id_microchip (del 1 al 10.000) como clave foránea id_microchip_fk. Código:

-- 4. Inserción de 10.000 registros en la tabla MASCOTA (Tabla Referente)
INSERT INTO mascota (nombre, especie, raza, fecha_nacimiento, duenio, id_microchip_fk)
SELECT
    -- Nombre aleatorio basado en el ID (AHORA es el primer valor, va a 'nombre')
    CONCAT('Mascota_', T.id_chip),

    -- Especie aleatoria (respeta el CHECK) (AHORA es el segundo valor, va a 'especie')
    ELT(1 + FLOOR(RAND() * 4), 'Perro', 'Gato', 'Ave', 'Roedor'),
    
    -- Raza genérica
    CONCAT('Raza_', ELT(1 + FLOOR(RAND() * 5), 'A', 'B', 'C', 'D', 'E')),

    -- Fecha de nacimiento aleatoria en los últimos 10 años
    DATE_SUB(CURDATE(), INTERVAL FLOOR(RAND() * 3650) DAY),
    
    -- Dueño genérico
    CONCAT('Dueño_', LPAD(T.id_chip, 5, '0')),
    
    -- ¡LA CLAVE! Asigna el ID del chip generado (1 a 10000) como FK
    T.id_chip
    
FROM (
    -- Subconsulta que genera el número secuencial de 1 a 10000
    SELECT 
        (t1.n + t2.n * 10 + t3.n * 100 + t4.n * 1000) + 1 AS id_chip
    FROM 
        Numeros t1, Numeros t2, Numeros t3, Numeros t4
) AS T;

-- La clave está en usar los números secuenciales (1 a 10000)
-- que coinciden con los IDs que se crearon en la tabla microchip.
-- Esto garantiza la FK y la restricción UNIQUE del 1:1.
;

-- 5. Limpieza de la tabla auxiliar
DROP TABLE Numeros;

-- 6. Verificación Final de Registros
SELECT 
    (SELECT COUNT(*) FROM microchip) AS Total_Microchips,
    (SELECT COUNT(*) FROM mascota) AS Total_Mascotas;

