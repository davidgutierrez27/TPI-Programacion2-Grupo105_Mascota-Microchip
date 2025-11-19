-- DATABASE:
-- DDL ETAPA 1: Mascota <-> Microchip (Relación 1:1)

-- 1. DATABASE:
CREATE DATABASE IF NOT EXISTS tpi_mascota_microchip
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_general_ci;

USE tpi_mascota_microchip;

-- 2. Borrado de tablas (IMPORTANTE: Tabla hija primero)
-- Borrar tablas en orden inverso a la dependencia para evitar errores (por si se ejecuta varias veces)
DROP TABLE IF EXISTS mascota;
DROP TABLE IF EXISTS microchip;

-- 3. TABLA B: MICROCHIP (Lado referenciado)
CREATE TABLE microchip (
    id_microchip BIGINT PRIMARY KEY AUTO_INCREMENT, 
    eliminado TINYINT(1) DEFAULT 0,
    codigo VARCHAR(30) NOT NULL,
    fecha_implantacion DATE NOT NULL,
    veterinaria VARCHAR(120) NOT NULL,
    observaciones VARCHAR(255),
    
    CONSTRAINT UQ_Codigo UNIQUE (codigo),
    CONSTRAINT CHK_Fecha_Minima_Chip CHECK (fecha_implantacion >= '2000-01-01')
);

-- 4. TABLA A: MASCOTA (Lado que referencia)
CREATE TABLE mascota (
    id_mascota INT AUTO_INCREMENT PRIMARY KEY,
    eliminado TINYINT(1) DEFAULT 0,
    nombre VARCHAR(100) NOT NULL,
    especie VARCHAR(50) NOT NULL,
    raza VARCHAR(50),
    fecha_nacimiento DATE,
    duenio VARCHAR(120) NOT NULL,
    
    CONSTRAINT chk_especie_valida CHECK (especie IN ('Perro', 'Gato', 'Ave', 'Roedor')),

    -- CONSTRAINT CHK_Fecha_Nacimiento CHECK (fecha_nacimiento < CURDATE()),

    id_microchip_fk BIGINT, 
    
    CONSTRAINT UQ_Mascota__Microchip UNIQUE (id_microchip_fk),
    
    CONSTRAINT FK_Mascota_Microchip FOREIGN KEY (id_microchip_fk) 
        REFERENCES microchip(id_microchip)
        ON UPDATE RESTRICT 
        ON DELETE SET NULL
);

