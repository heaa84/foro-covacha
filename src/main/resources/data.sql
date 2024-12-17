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

CREATE TABLE usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(200) NOT NULL,
    correo_electronico VARCHAR(255) NOT NULL UNIQUE,
    contrasena VARCHAR(300) NOT NULL,
    perfil VARCHAR(15) NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE cursos (
    id BIGINT NOT NULL AUTO_INCREMENT, -- Auto ingrementable
    nombre VARCHAR(150) NOT NULL,  -- Titulo del Topico
    categoria VARCHAR(100) NOT NULL,  -- Titulo del Topico

    PRIMARY KEY (id)
);

CREATE TABLE respuestas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    mensaje TEXT NOT NULL, -- Suponiendo que es un texto largo para el mensaje
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Fecha y hora de creación
    usuario_que_respondio VARCHAR(150),
    topico_id BIGINT,
    PRIMARY KEY (id)
);

INSERT INTO usuarios (nombre, correo_electronico, contrasena , perfil)
VALUES ('admin', 'admin@covacha.com', '$2a$10$GK8Yv7P.gZPs95iW/oewJe3FQ7f3miTY7ob.X3A2TF5BBerWOOuEO', 'ADMIN');

INSERT INTO usuarios (nombre, correo_electronico, contrasena, perfil)
VALUES ('user', 'user@covacha.com', '$2a$10$AhrAsr3SkMDBwcabbSWNK.GJAQg85KkZHrVoOa5tx6oz.UDvcEB1W','USER');

INSERT INTO cursos (nombre, categoria)
VALUES
('Operaciones basicas', 'JAVA'),
('Operaciones basicas', 'PYTHON'),
('Operaciones basicas', 'JAVASCRIP'),
('VARIABLES', 'JAVA'),
('VARIABLES', 'PYTHON'),
('VARIABLES', 'JAVASCRIP');

INSERT INTO topicos (titulo,mensaje,status,autor,curso_id)
VALUES

('Sumar dos numeros', 'Como sumo dos numeros en java?','ACTIVO','admin','1'),
('Resta dos numeros', 'Como restar dos numeros en java?','ACTIVO','user','1'),
('Sumar dos numeros', 'Como sumo dos numeros en python?','ACTIVO','user','2'),
('Dividir dos numeros', 'Como Divido dos numeros en python?','ACTIVO','admin','2'),
('Sumar dos numeros', 'Como sumo dos numeros en javaScrip?','ACTIVO','user','3'),
('Declarar variables', 'Forma correcta de declarar variables javaScrip?','ACTIVO','user','3'),
('Tipos de variables', 'como declarar variable en java?','ACTIVO','user','4'),
('Constantes', 'como declarar constantes en java?','ACTIVO','user','4'),
('Declarar variable', 'como declarar variable en python?','ACTIVO','user','5'),
('Imprimir en pantalla', 'imprimir en pantalla?','ACTIVO','user','5'),
('Tipos de variables', 'como declarar variable en javascrip?','ACTIVO','user','6'),
('Hola Mundo', 'primer programa!!!','ACTIVO','user','6');

INSERT INTO respuestas (mensaje,usuario_que_respondio,topico_id)
VALUES

('Puedes sumar dos numeros e imprimirlos en pantalla System.out.println(1+2);', 'admin','1'),
('Puedes imprimir variables con: print(variable);', 'admin','10');

UPDATE topicos
SET status = 'RESUELTO'
WHERE id IN(1,10);