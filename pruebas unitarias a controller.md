# Dependencias Maven para los Test Integrados en Spring Boot
```java
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-test</artifactId>
<scope>test</scope>
</dependency>
```
# Test de Integración en Spring Boot con @SpringBootTest
Cuando creemos nuestros test de integración, la mejor aproximación será separarlos de los test unitarios, y en clases aparte.

Spring Boot nos proporciona la anotación @SpringBootTest para poder inicializar nuestra aplicación en función de unas propiedades que le proporcionemos.
Esta anotación nos permitirá crear un ApplicationContext  de nuestra aplicación. Vamos a ver un ejemplo:
```java
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerIT {

  @Autowired
  private MockMvc mockMvc;
```
En la creación de la clase anterior hemos añadido diversas anotaciones que nos facilitará la creación de nuestro test de integración:

SpringBootTest: Nos va a crear un contenedor de nuestra aplicación creando un ApplicationContext.
ExtendWith para Junit5: Para habilitar el soporte para poder hacer uso de JUnit 5.
AutoconfigureMockMvc: Nos permitirá hacer uso de MockMvc para nuestros test integrados, de manera que podamos realizar peticiones HTTP.
Como podemos ver la anotación @SpringBootTest, nos arranca nuestra aplicación con un ApplicationContext para test. Esta anotación podrá ir sola o con otros parámetros, por ejemplo:

webEnvironment = WebEnvironment.RANDOM_PORT: Con esta opción nuestros test arrancaran en un puerto aleatorio que además no se encuentre ya en uso.
webEnvironment = WebEnvironment.DEFINED_PORT: Al incluir esta opción nuestros test arrancaran en un puerto predefinido.

------
# Modificaciones y AutoConfiguraciones para nuestro ApplicationContext
El uso de @SpringBootTest, nos va a permitir incluir diferentes autoconfiguraciones para nuestros test (en el anterior punto ya hemos visto alguna), vamos a ver qué cosas podemos hacer en nuestros test:

Habilitar autoconfiguraciones
El uso de SpringBootTest para nuestros test, nos va a permitir crear diferentes autoconfiguraciones, vamos a mostrar algunas de las que podemos añadir:

@AutoconfigureMockMvc: Nos va a permitir añadir un MockMvc  a nuestro ApplicationContext, de esta manera podremos realizar peticiones HTTP contra nuestro controlador.
@AutoConfigureTestDatabase: Por lo general tendremos una Base de Datos embebida o en memoria, esta anotación, sin embargo, nos permitira realizar nuestros test contra una base de datos real.
@JsonTest: Haciendo uso de esta anotación, podremos verificar si nuestros serializadores y deserializadores de JSON funcionan de forma correcta.
@RestClientTest: Nos va a permitir verificar nuestros resttemplate, y junto con MockRestServiceServer , nos permitirá mockear las respuestas que llegan del restemplate.
@AutoConfigureWebTestClient: Nos permite verificar los endpoint del servidor, agrega WebTestClient al contexto.

