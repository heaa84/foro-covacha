CREATE TABLE respuestas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    mensaje TEXT NOT NULL, -- Suponiendo que es un texto largo para el mensaje
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Fecha y hora de creación
    usuario_que_respondio VARCHAR(150),
    topico_id BIGINT,
    PRIMARY KEY (id)
);
