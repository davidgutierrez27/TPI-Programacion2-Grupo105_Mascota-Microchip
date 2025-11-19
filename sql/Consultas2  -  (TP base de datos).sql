-- Consulta 1: JOIN entre Mascota y Microchip (Relación A↔B)
-- El WHERE filtra los registros que no están eliminados, y el ORDER BY ordena por especie y nombre.
SELECT
	m.id_mascota,
	m.nombre AS nombre_mascota,
	m.especie,
	m.raza,
	m.fecha_nacimiento,
	m.duenio,
	mc.codigo AS numero_microchip,
	mc.fecha_implantacion,
	mc.veterinaria,
	mc.observaciones
FROM mascota m
INNER JOIN microchip mc ON m.id_microchip_fk = mc.id_microchip
WHERE m.eliminado = false AND mc.eliminado = false
ORDER BY m.especie, m.nombre;






-- Consulta 2: JOIN con Análisis Temporal (Relación A↔B)
-- Actualizar una fecha de ejemplo
UPDATE microchip
SET fecha_implantacion = '2025-09-10'
WHERE codigo = 'MC001';

-- Seguimiento de microchips recientes
-- Consulta para ver microchips implantados en los últimos 6 meses, categorizados por antigüedad.
SELECT 
    m.nombre AS nombre_mascota,
    m.especie,
    m.raza,
    m.duenio,
    mc.codigo,
    mc.fecha_implantacion,
    mc.veterinaria,
    DATEDIFF(CURDATE(), mc.fecha_implantacion) AS dias_desde_implantacion,
    CASE 
        WHEN DATEDIFF(CURDATE(), mc.fecha_implantacion) < 30 THEN 'Muy Reciente'
        WHEN DATEDIFF(CURDATE(), mc.fecha_implantacion) < 90 THEN 'Reciente'
        ELSE 'Estable'
    END AS categoria_tiempo
FROM mascota m
JOIN microchip mc ON m.id_microchip_fk = mc.id_microchip
WHERE m.eliminado = FALSE 
  AND mc.eliminado = FALSE
  AND mc.fecha_implantacion >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)
ORDER BY mc.fecha_implantacion DESC;





-- Consulta 3: GROUP BY + HAVING para Estadísticas
-- Genera estadísticas por especie: total, cobertura de microchips y edad promedio
-- Estadísticas de cobertura por especie
SELECT
	m.especie,
	COUNT(*) AS total_mascotas,
	COUNT(mc.id_microchip) AS mascotas_con_chip,
	ROUND((COUNT(mc.id_microchip) / COUNT(*) * 100), 2) AS porcentaje_cobertura,
	AVG(TIMESTAMPDIFF(YEAR, m.fecha_nacimiento, CURDATE())) AS edad_promedio
FROM mascota m
LEFT JOIN microchip mc 
	ON m.id_microchip_fk = mc.id_microchip 
	AND mc.eliminado = FALSE
WHERE m.eliminado = FALSE
GROUP BY m.especie
HAVING total_mascotas >= 1
ORDER BY porcentaje_cobertura DESC;




-- Consulta 4: subconsulta.
-- Analiza la actividad de cada veterinaria: cantidad, especies atendidas y estado actual.
-- Desempeño de veterinarias
SELECT
    mc.veterinaria,
	COUNT(*) AS total_implantaciones,
	COUNT(DISTINCT m.especie) AS especies_atendidas,
	ROUND(AVG(TIMESTAMPDIFF(YEAR, m.fecha_nacimiento, CURDATE())), 1) AS edad_promedio,
	MIN(mc.fecha_implantacion) AS primera_implantacion,
	MAX(mc.fecha_implantacion) AS ultima_implantacion,
	DATEDIFF(CURDATE(), MAX(mc.fecha_implantacion)) AS dias_desde_ultima,
	CASE
    	WHEN DATEDIFF(CURDATE(), MAX(mc.fecha_implantacion)) > 90 THEN 'Inactiva recientemente'
    	ELSE 'Activa'
    END AS estado_actividad
FROM mascota m
JOIN microchip mc ON m.id_microchip_fk = mc.id_microchip
WHERE m.eliminado = FALSE
  AND mc.eliminado = FALSE
GROUP BY mc.veterinaria
HAVING total_implantaciones >= 1
ORDER BY total_implantaciones DESC, dias_desde_ultima DESC;






-- =============================================
-- VISTA: dashboard_mascotas_completo
-- Sistema: Gestión Veterinaria - Microchips
-- Propósito: Vista consolidada para reportes
-- =============================================

-- Eliminar vista si existe (para actualización)
DROP VIEW IF EXISTS dashboard_mascotas_completo;

-- Crear vista principal
CREATE VIEW dashboard_mascotas_completo AS
SELECT 
    -- Información básica
    m.id_mascota,
    m.nombre AS nombre_mascota,
    m.especie,
    m.raza,
    m.duenio,
    m.fecha_nacimiento,
    
    -- Información del microchip
    mc.id_microchip,
    mc.codigo AS numero_microchip,
    mc.fecha_implantacion,
    mc.veterinaria AS veterinaria_implantacion,
    mc.observaciones AS observaciones_chip,
    
    -- Métricas calculadas
    TIMESTAMPDIFF(YEAR, m.fecha_nacimiento, CURDATE()) AS edad_anios,
    TIMESTAMPDIFF(MONTH, m.fecha_nacimiento, CURDATE()) AS edad_meses,
    
    -- Categorización
    CASE 
        WHEN mc.id_microchip IS NULL THEN 'Sin Microchip'
        WHEN TIMESTAMPDIFF(YEAR, mc.fecha_implantacion, CURDATE()) < 1 THEN 'Chip Reciente (<1 año)'
        WHEN TIMESTAMPDIFF(YEAR, mc.fecha_implantacion, CURDATE()) < 3 THEN 'Chip Estable (1-3 años)'
        ELSE 'Chip Antiguo (>3 años)'
    END AS estado_microchip,
    
    -- Prioridades
    CASE 
        WHEN mc.id_microchip IS NULL AND m.especie IN ('Perro', 'Gato') THEN 'Alta Prioridad - Implantar'
        WHEN mc.id_microchip IS NULL THEN 'Media Prioridad - Implantar'
        WHEN TIMESTAMPDIFF(YEAR, mc.fecha_implantacion, CURDATE()) >= 3 THEN 'Prioridad - Verificar Chip'
        ELSE 'Seguimiento Normal'
    END AS prioridad_accion

FROM mascota m
LEFT JOIN microchip mc ON m.id_microchip_fk = mc.id_microchip AND mc.eliminado = false
WHERE m.eliminado = false;

-- Mensaje de confirmación
SELECT 'Vista dashboard_mascotas_completo creada exitosamente' AS estado;
