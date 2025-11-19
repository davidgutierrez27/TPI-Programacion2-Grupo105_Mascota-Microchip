README - TPI Mascota → Microchip
=================================

Proyecto: Sistema de Veterinaria - Gestión de Mascotas y Microchips  
Materia: Programación II – Tecnicatura Universitaria en Programación  
Año: 2025

Integrantes
-----------
● Brian Gutiérrez Colque
● David Gutiérrez
● Verónica Guirín
● María del Rosario Margarita Iturralde Elortegui



Descripción general
-------------------
Este proyecto implementa una aplicación Java con arquitectura en capas y acceso a datos mediante JDBC.  
El dominio seleccionado para el Trabajo Práctico Integrador es:

    Mascota (A) → Microchip (B)  
    Relación 1 a 1 unidireccional (A contiene referencia a B)

El sistema permite realizar operaciones CRUD completas sobre ambas entidades, aplicar baja lógica, organizar transacciones con commit/rollback y manipular la información mediante un menú de consola.



Tecnologías utilizadas
----------------------
- Java 21+
- MySQL 8 (XAMPP o instalación local)
- JDBC (sin ORM)
- Patrón DAO + Service
- PreparedStatement en todas las consultas
- try-with-resources para manejo seguro de conexiones



Estructura del proyecto
-----------------------
El proyecto sigue la estructura obligatoria definida en el TPI:

    /config
        DatabaseConnection.java
        TransactionManager.java

    /model
        Mascota.java
        Microchip.java

    /dao
        GenericDAO.java
        MascotaDAO.java
        MascotaDAOImpl.java
        MicrochipDAO.java
        MicrochipDAOImpl.java

    /service
        GenericService.java
        MascotaService.java
        MascotaServiceImpl.java
        MicrochipService.java
        MicrochipServiceImpl.java

    /main
        Main.java
        AppMenu.java
        MenuDisplay.java
        MenuHandler.java
        TestConexion.java

    /resources
        db.properties



Base de datos
--------------
El proyecto utiliza una base MySQL denominada:

    tpi_mascota_microchip

Se incluyen dos archivos SQL (crear y poblar):

1. **Creacion de BaseDatos.sql**
   - CREATE DATABASE
   - CREATE TABLE Mascota, Microchip
   - Clave foránea única para relación 1→1
   - Constraints (UNIQUE, CHECK, NOT NULL)

2. **Carga de datos,sql**
   - Datos de prueba para ambas tablas

SE INCLUYEN OTROS ARCHIVOS
consultas1.sql (consultas del tp programacion)
consultas2.sql (consultas avanzadas tp base de datos) 
seguridad integridad.sql 


Conexión a MySQL
----------------
La conexión se configura desde:  

    /resources/db.properties

Formato recomendado:

    db.url=jdbc:mysql://localhost:3306/tpi_mascota_microchip?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    db.user=(TU USUARIO)
    db.password=(TU CONTRASEÑA)

ACLARACION: 
No pedimos credenciales porque sumaria complejidad innecesaria.
El archivo db.properties es justamente para no pedir datos al usuario



Cómo ejecutar el proyecto
-------------------------
1. Iniciar MySQL (levantar el puerto 3306) 
2. Crear la base ejecutando DDL.sql. (si aun no esta creada)
3. Insertar datos iniciales con INSERTS.sql. (o hacerlo desde Java luego de la conexion)

4. Configurar db.properties con usuario/contraseña correctos.
5. Compilar y ejecutar el proyecto desde Main.java. (antes probar conexión con TestConexion)



Funcionalidades del sistema
---------------------------
- CRUD completo de Mascotas
- CRUD completo de Microchips
- Búsqueda por:
    • ID  
    • Nombre (Mascota)  
    • Código (Microchip)
- Asociación 1→1 en alta de Mascota
- Baja lógica (eliminado = TRUE)
- Transacciones:
    • Inserción compuesta (Mascota + Microchip)
    • commit automático si todo está OK
    • rollback ante cualquier excepción



Transacciones
-------------
El Service usa TransactionManager para manejar:

    setAutoCommit(false)
    operaciones DAO
    commit()
    rollback()

Esto garantiza integridad en la relación 1→1.



Requisitos previos
------------------
- Java 21+
- MySQL/XAMPP activo
- Conector JDBC (mysql-connector-j.jar)
- IDE recomendado: NetBeans 17/18




Video
-----
https://drive.google.com/file/d/1bTupfreiqeGDf-pKxu-VyR65P_owtvcX/view?usp=drive_link



