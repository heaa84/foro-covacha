CREATE TABLE usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(200) NOT NULL,
    correo_electronico VARCHAR(255) NOT NULL UNIQUE,
    contrasena VARCHAR(300) NOT NULL,
    PRIMARY KEY (id)
);
