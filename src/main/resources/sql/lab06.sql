-- =========================================
-- CREACIÓN DE TABLAS
-- =========================================
DROP SCHEMA IF EXISTS `lab06` ;
CREATE SCHEMA IF NOT EXISTS `lab06` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `lab06` ;

CREATE TABLE roles (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE usuarios (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          nombre VARCHAR(100) NOT NULL,
                          correo VARCHAR(100) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          rol_id BIGINT NOT NULL,
                          FOREIGN KEY (rol_id) REFERENCES roles(id)
);

CREATE TABLE heroes_navales (
                                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                nombre VARCHAR(100) NOT NULL,
                                descripcion VARCHAR(255),
                                pais VARCHAR(50)
);

CREATE TABLE intenciones (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             usuario_id BIGINT NOT NULL,
                             descripcion VARCHAR(255) NOT NULL,
                             fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE canciones_criollas (
                                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    titulo VARCHAR(150) NOT NULL,
                                    letra TEXT
);

CREATE TABLE asignaciones_cancion (
                                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                      usuario_id BIGINT NOT NULL,
                                      cancion_id BIGINT NOT NULL,
                                      intentos INT DEFAULT 0,
                                      adivinada BOOLEAN DEFAULT FALSE,
                                      FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
                                      FOREIGN KEY (cancion_id) REFERENCES canciones_criollas(id)
);

CREATE TABLE numeros_casa (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              usuario_id BIGINT NOT NULL,
                              numero_objetivo INT NOT NULL,
                              intentos INT DEFAULT 0,
                              adivinado BOOLEAN DEFAULT FALSE,
                              FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE mesas (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       numero INT NOT NULL UNIQUE,
                       capacidad INT NOT NULL DEFAULT 4,
                       disponible BOOLEAN DEFAULT TRUE
);

CREATE TABLE reservas (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          usuario_id BIGINT NOT NULL,
                          mesa_id BIGINT NOT NULL,
                          fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
                          FOREIGN KEY (mesa_id) REFERENCES mesas(id)
);

-- =========================================
-- INSERCIÓN DE DATOS BASE
-- =========================================

-- Roles
INSERT INTO roles (nombre) VALUES
                               ('ADMIN'),
                               ('USUARIO'),
                               ('VISITANTE');

INSERT INTO usuarios (nombre, correo, password, rol_id) VALUES
-- Hombres
('Carlos Vargas', 'carlos.vargas@example.com', '123456', 2),
('Xavier Ruiz', 'xavier.ruiz@example.com', '123456', 2),
('Victor Guerra', 'victor.guerra@example.com', '123456', 2),
('Diego Torres', 'diego.torres@example.com', '123456', 2),
('Jorge Rubio', 'jorge.rubio@example.com', '123456', 2),
('Paolo Mendoza', 'paolo.mendoza@example.com', '123456', 2),
('Alonso Llanos', 'alonso.llanos@example.com', '123456', 2),
('Ronald Sanchez', 'ronald.sanchez@example.com', '123456', 2),
('Luis Cotrina', 'luis.cotrina@example.com', '123456', 2),
('Jhocell Perez', 'jhocell.perez@example.com', '123456', 2),
('Paolo Valiente', 'paolo.valiente@example.com', '123456', 2),
('Mariana Rojas', 'mariana.rojas@example.com', '123456', 2),
('Camila Fernández', 'camila.fernandez@example.com', '123456', 2),
('Valeria Campos', 'valeria.campos@example.com', '123456', 2),
('Gabriela Paredes', 'gabriela.paredes@example.com', '123456', 2),
('Andrea Lozano', 'andrea.lozano@example.com', '123456', 2),
('Sofía Delgado', 'sofia.delgado@example.com', '123456', 2),
('Lucía Herrera', 'lucia.herrera@example.com', '123456', 2),
('Rosa Aguilar', 'rosa.aguilar@example.com', '123456', 2),
('Claudia Navarro', 'claudia.navarro@example.com', '123456', 2),
('Alejandra Cornejo', 'alejandra.cornejo@example.com', '123456', 2),
('Administrador', 'admin@example.com', 'admin123', 1);

INSERT INTO heroes_navales (nombre, descripcion, pais) VALUES
                                                           ('Miguel Grau', 'El Caballero de los Mares, héroe máximo del Perú', 'Perú'),
                                                           ('Arturo Prat', 'Héroe naval chileno en la Guerra del Pacífico', 'Chile'),
                                                           ('Almirante Brown', 'Fundador de la Armada Argentina', 'Argentina'),
                                                           ('Simón Bolívar', 'Libertador y líder naval en el Caribe', 'Venezuela');

INSERT INTO intenciones (usuario_id, descripcion) VALUES
                                                      (1, 'Quiero participar en la campaña de mensajes.'),
                                                      (2, 'Deseo enviar flores amarillas a mis amigos.'),
                                                      (3, 'Me gustaría sumarme con un carrito de amistad.');

INSERT INTO canciones_criollas (titulo, letra) VALUES
                                                   ('La Flor de la Canela', 'Yo perdí el corazón'),
                                                   ('Contigo Perú', 'Cuando despiertan mis ojos'),
                                                   ('La flor de la canela', 'Olga'),
                                                   ('Valicha', 'Esta es mi tierra'),
                                                   ('Ritmo, color y sabor', 'Jipi jay'),
                                                   ('Y se llama Perú', 'Toro Mata');

INSERT INTO asignaciones_cancion (usuario_id, cancion_id) VALUES
                                                              (1, 1),
                                                              (2, 2),
                                                              (3, 3);

INSERT INTO numeros_casa (usuario_id, numero_objetivo) VALUES
                                                           (1, 7),
                                                           (2, 15),
                                                           (3, 23);

INSERT INTO mesas (numero, capacidad, disponible) VALUES
                                                      (1, 4, TRUE),
                                                      (2, 4, TRUE),
                                                      (3, 4, TRUE),
                                                      (4, 4, TRUE),
                                                      (5, 4, TRUE);

INSERT INTO reservas (usuario_id, mesa_id) VALUES
    (1, 1);
