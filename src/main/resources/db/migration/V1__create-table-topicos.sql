CREATE TABLE topico (
    id BIGINT NOT NULL AUTO_INCREMENT, -- Auto ingrementable
    titulo VARCHAR(300) NOT NULL,  -- Titulo del Topico
    mensaje TEXT NOT NULL,  -- Mensaje o pregunta del Totico
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- fecha de cuando se creo el Topico
    /*
    ACTIVO: Indica que el tópico está activo y puede recibir respuestas.
    CERRADO: Indica que el tópico no acepta más respuestas.
    ARCHIVADO: Indica que el tópico ha sido cerrado y ya no está en uso.*/
    status VARCHAR(50) NOT NULL,
    autor varchar(150) NOT NULL,
    /*
    curso BIGINT NOT NULL, -- CURSO???
    */
    PRIMARY KEY (id)
);
