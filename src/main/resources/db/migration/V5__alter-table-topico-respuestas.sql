-- Archivo de migración Flyway: V2__update_respuestas_table.sql

-- Eliminar la columna 'solucion' de la tabla 'respuestas'
ALTER TABLE respuestas 
DROP COLUMN solucion;

-- Agregar la columna 'usuario_que_respondio' de tipo VARCHAR(150) a la tabla 'respuestas'
ALTER TABLE respuestas 
ADD COLUMN usuario_que_respondio VARCHAR(150);