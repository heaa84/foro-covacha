CREATE TABLE topico (
    id BIGINT NOT NULL AUTO_INCREMENT, -- Auto ingrementable
    titulo VARCHAR(300) NOT NULL,  -- Titulo del Topico
    mensaje TEXT NOT NULL,  -- Mensaje o pregunta del Totico
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- fecha de cuando se creo el Topico
    status VARCHAR(50) NOT NULL, -- Estado del Topico: SIN_RESOLVER, RESUELTO,
    /*
    autor BIGINT NOT NULL, -- EL AUTOR DEL TOPICO
    curso BIGINT NOT NULL, -- CURSO???
    respuesta TEXT, -- RESPUESTA: LA RESPUESTA CUANDO QUERAMOS CONTESRA UN TOPICO ???
    */
    PRIMARY KEY (id)
);
