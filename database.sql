DROP DATABASE IF EXISTS clinica_veterinaria;
CREATE DATABASE clinica_veterinaria;
USE clinica_veterinaria;

CREATE TABLE clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    correo VARCHAR(100),
    direccion VARCHAR(150)
);

CREATE TABLE mascotas (
    id_mascota INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    especie VARCHAR(30) NOT NULL,
    raza VARCHAR(50),
    edad INT,
    peso DECIMAL(5,2),
    sexo VARCHAR(10),
    id_cliente INT NOT NULL,
    CONSTRAINT fk_mascota_cliente FOREIGN KEY (id_cliente)
        REFERENCES clientes(id_cliente) ON DELETE CASCADE
);

CREATE TABLE veterinarios (
    id_veterinario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    cedula VARCHAR(30),
    especialidad VARCHAR(50),
    telefono VARCHAR(20),
    email VARCHAR(100),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE citas (
    id_cita INT AUTO_INCREMENT PRIMARY KEY,
    id_mascota INT NOT NULL,
    id_veterinario INT NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    motivo VARCHAR(150),
    estado VARCHAR(20) NOT NULL DEFAULT 'PROGRAMADA',
    CONSTRAINT fk_cita_mascota FOREIGN KEY (id_mascota)
        REFERENCES mascotas(id_mascota) ON DELETE CASCADE,
    CONSTRAINT fk_cita_veterinario FOREIGN KEY (id_veterinario)
        REFERENCES veterinarios(id_veterinario) ON DELETE CASCADE
);

CREATE TABLE consultas (
    id_consulta INT AUTO_INCREMENT PRIMARY KEY,
    id_mascota INT NOT NULL,
    id_veterinario INT NOT NULL,
    fecha DATE NOT NULL,
    diagnostico VARCHAR(255),
    tratamiento VARCHAR(255),
    observaciones VARCHAR(255),
    CONSTRAINT fk_consulta_mascota FOREIGN KEY (id_mascota)
        REFERENCES mascotas(id_mascota) ON DELETE CASCADE,
    CONSTRAINT fk_consulta_veterinario FOREIGN KEY (id_veterinario)
        REFERENCES veterinarios(id_veterinario) ON DELETE CASCADE
);

CREATE TABLE servicios (
    id_servicio INT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(20) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    fecha DATE NOT NULL,
    id_mascota INT NOT NULL,
    motivo VARCHAR(150),
    tipo_vacuna VARCHAR(50),
    descripcion_procedimiento VARCHAR(150),
    complejidad VARCHAR(10),
    CONSTRAINT fk_servicio_mascota FOREIGN KEY (id_mascota)
        REFERENCES mascotas(id_mascota) ON DELETE CASCADE
);

CREATE TABLE facturas (
    id_factura INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    fecha DATE NOT NULL,
    total DECIMAL(10,2) NOT NULL DEFAULT 0,
    CONSTRAINT fk_factura_cliente FOREIGN KEY (id_cliente)
        REFERENCES clientes(id_cliente) ON DELETE CASCADE
);

CREATE TABLE detalle_factura (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_factura INT NOT NULL,
    id_servicio INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    subtotal DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_detalle_factura FOREIGN KEY (id_factura)
        REFERENCES facturas(id_factura) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_servicio FOREIGN KEY (id_servicio)
        REFERENCES servicios(id_servicio)
);