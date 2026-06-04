CREATE DATABASE IF NOT EXISTS panteon_rpg;
USE panteon_rpg;

CREATE TABLE partidas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_partida VARCHAR(100) NOT NULL,
    piso_actual INT DEFAULT 1,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE clases_personaje (
    clase VARCHAR(50) PRIMARY KEY,
    hp_max INT NOT NULL,
    ataque INT NOT NULL,
    defensa INT NOT NULL,
    velocidad INT NOT NULL,
    suerte INT NOT NULL,
    estamina_max INT NOT NULL
);

INSERT INTO clases_personaje (clase, hp_max, ataque, defensa, velocidad, suerte, estamina_max) VALUES
('Guerrero', 800, 120, 30, 60, 25, 150),

('Mago',      500, 180, 15, 70, 35, 200),

('Pícaro',   600, 130, 20, 150, 80, 130),

('Tanque',   1200,  90, 50, 40, 15, 180),

('Arquero',  650, 150, 22, 140, 45, 140);

CREATE TABLE personajes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    partida_id INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    clase VARCHAR(50) NOT NULL,
    nivel INT DEFAULT 1,
    experiencia INT DEFAULT 0,
    hp_max INT NOT NULL,
    hp_actual INT NOT NULL,
    ataque INT NOT NULL,
    defensa INT NOT NULL,
    velocidad INT NOT NULL,
    suerte INT NOT NULL,
    estamina_max INT NOT NULL,
    estamina_actual INT NOT NULL,
    FOREIGN KEY (partida_id) REFERENCES partidas(id) ON DELETE CASCADE,
    FOREIGN KEY (clase) REFERENCES clases_personaje(clase)
);

CREATE TABLE monstruos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    hp_max INT NOT NULL,
    ataque INT NOT NULL,
    defensa INT NOT NULL,
    velocidad INT NOT NULL,
    suerte INT NOT NULL
);

CREATE TABLE cementerio_heroes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_heroe VARCHAR(100) NOT NULL,
    clase VARCHAR(50) NOT NULL,
    nivel_alcanzado INT NOT NULL,
    piso_final INT NOT NULL,
    estado VARCHAR(50) NOT NULL,
    asesino VARCHAR(100),
    epitafio TEXT
);

CREATE TABLE bestiario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    monstruo_id INT NOT NULL,
    fecha_descubrimiento DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (monstruo_id) REFERENCES monstruos(id) ON DELETE CASCADE,
    UNIQUE (monstruo_id)
);

INSERT INTO monstruos (nombre, tipo, hp_max, ataque, defensa, velocidad, suerte) VALUES

('Basilisco Menor', 'Rápido', 30, 10, 2, 80, 15),

('Harpía', 'Volador', 40, 15, 3, 90, 20),

('Sombra Espectral', 'Mágico', 50, 12, 5, 75, 40),

('Golem de Piedra', 'Tanque', 150, 25, 15, 0, 0),

('Minotauro', 'Berserker', 100, 30, 8, 40, 10),

('Cíclope', 'Pesado', 180, 45, 10, 10, 5),

('Quimera', 'Equilibrado', 120, 25, 12, 50, 15),

('Hidra de Lerna', 'Colosal', 250, 20, 15, 20, 10),

('Fénix de Fuego', 'Rápido', 130, 35, 10, 85, 35),

('Dragón Ancestral', 'Jefe', 300, 50, 20, 60, 25); 



CREATE TABLE IF NOT EXISTS bestiario_partida (
    partida_id INT,
    monstruo_id INT,
    PRIMARY KEY (partida_id, monstruo_id),
    FOREIGN KEY (partida_id) REFERENCES partidas(id) ON DELETE CASCADE,
    FOREIGN KEY (monstruo_id) REFERENCES monstruos(id) ON DELETE CASCADE
);