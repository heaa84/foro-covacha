# Creando Spring Security key con Token JWT (JSON Web Tokens).
## Estructura:
- covacha.foro/controller/
  - AutenticationController.java
    - Contiene el endpoint "/login". 
    - Capturamos en un Authentication el usuario y contraseña del usuario.
    - Método que autentifica el token.
    - Método que genera el token.
    - retorna el token en formato JSON.
- covacha.foro/infra/security
  - TokenService
    - Método que genera el JWT si los datos del usuario son válidos.
    - Método que verifica que el JWT sea válido.
    - Método que genera la fecha y hora, de expiración del token
  - SecurityConfigurations.java
    - Configuración de la cadena de filtros de seguridad.
    - Configuración del AuthenticationManager.
    - Configuración del codificador de contraseñas.
  - SecurityFilter.java
    - Método que se ejecuta con cada solicitud HTTP. este método intenta autenticar al usuario mediante el token incluido en la cabecera de la solicitud.
    - Método para extraer el token de autenticación de la cabecera "Authorization" de la solicitud.
  - AutenticacionService.java
    - Contiene un método que; busca por nombre un usuario y lo retorna como un UserDetails. 
  - DatosJWTToken.java
    - Contiene un DTO y se ocupa para retornar el JWT










