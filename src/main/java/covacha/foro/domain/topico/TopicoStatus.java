package covacha.foro.domain.topico;

public enum TopicoStatus {
    ACTIVO, //Indica que el tópico está activo y puede recibir respuestas.
    CERRADO, //Indica que el tópico no acepta más respuestas.
    ARCHIVADO //Indica que el tópico ha sido cerrado y ya no está en uso.
}
