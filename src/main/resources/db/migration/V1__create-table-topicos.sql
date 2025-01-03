CREATE TABLE topicos (
    id BIGINT NOT NULL AUTO_INCREMENT, -- Auto ingrementable
    titulo VARCHAR(300) NOT NULL,  -- Titulo del Topico
    mensaje TEXT NOT NULL,  -- Mensaje o pregunta del Totico
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- fecha de cuando se creo el Topico

    status VARCHAR(50) NOT NULL,
    autor varchar(150) NOT NULL,
    curso_id int,
    PRIMARY KEY (id)
);
