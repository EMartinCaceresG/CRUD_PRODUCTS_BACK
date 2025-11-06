-- Script SQL para crear la base de datos y tabla de productos
-- Ejecutar este script en MySQL si se desea crear la estructura manualmente

CREATE DATABASE IF NOT EXISTS productdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE productdb;

-- La tabla se crea automáticamente con JPA si spring.jpa.hibernate.ddl-auto=update
-- Este script es solo de referencia

CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500),
    precio DECIMAL(12, 2) NOT NULL,
    stock INT NOT NULL,
    categoria VARCHAR(50),
    fecha_creacion DATETIME,
    fecha_actualizacion DATETIME,
    CONSTRAINT chk_precio_positivo CHECK (precio > 0),
    CONSTRAINT chk_stock_positivo CHECK (stock >= 0)
);

-- Índices para mejorar el rendimiento de las búsquedas
CREATE INDEX idx_nombre ON products(nombre);
CREATE INDEX idx_categoria ON products(categoria);
CREATE INDEX idx_precio ON products(precio);

