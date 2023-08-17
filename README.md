Este proyecto permite visualizar la forma en que se debe crear un proyecto en JAVA, con base en una arquitectura limpia.

* Backend:
* Versión Java: 11
* Lombok: Libreria que permite trabajar con un código más limpio.
* ProjectReactor: Libreria Java para trabajar con flujos de datos reactivos no bloqueantes.
* Fluent Validator: Libreria para la realización de validaciones a nivel de request.
* H2 Database: Base de datos en memoria para pruebas.
* Arquitectura limpia: https://nescalro.medium.com/entendiendo-a-la-arquitectura-limpia-7877ad3a0a47

* Front:
* Swagger: http://localhost:8080/swagger-ui/#/ (Libreria para la documentación de la API)

* Servicios:
  * product-controller: Permite la administración de los productos (guardar, editar, eliminar, consultar).
  * product-sale-controller: Permite gestionar las ventas de productos
  * Models: Objetos que contienen los parametros de entrada para los productos, venta y la respuesta.

* NOTAS:
  * Las consultas se encuentran inmersas dentro de la capa de infraestructura, dentro del driven adapter correspondiente:
    * ProductDataRepository
    * ProductSaleDataRepository
  * La creación de las tablas esta dada por un archivo .sql (resources/schema.sql) y ejecutado en el Application.java
  * Para la ejecución, se debe ingresar a la url (http://localhost:8080/swagger-ui/#/), donde se visualizaran los servicios y dentro de estos cada uno de los métodos para su ejecución.
  * IDE de desarrollo: IntelliJ
  * Administrador de dependencias: Maven