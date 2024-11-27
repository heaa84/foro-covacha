CREATE TABLE respuesta (
    id BIGINT NOT NULL AUTO_INCREMENT,
    mensaje TEXT NOT NULL, -- Suponiendo que es un texto largo para el mensaje
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Fecha y hora de creación
    solucion BOOLEAN DEFAULT FALSE, -- Indica si es una solución aceptada
    PRIMARY KEY (id)
);
