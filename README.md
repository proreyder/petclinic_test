# Spring Boot : Application PetClinic

## Features

### 1.  Unit Test  --> Tag v1.0.0

mvn test -Dspring.profiles.active=h2

## Caso 1: veterinarios

El servicio `com.tecsupt.petclinic.service.VetService` permite crear, actualizar,
buscar y eliminar veterinarios. También permite desactivar, reactivar y consultar
activos e inactivos según los escenarios de `TASKS.md`.

Los nuevos veterinarios se crean activos. Actualizar sus datos conserva su estado.
Eliminar borra el registro; desactivar mantiene el registro con `active = false`.
Buscar, actualizar, eliminar o cambiar el estado de un ID inexistente lanza
`VetNotFoundException`. El repositorio no se publica directamente por Data REST.

### Ejecutar en Windows con Java 21

```powershell
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-21.0.12'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
.\mvnw.cmd test "-Dlogging.file.name=target/test-application.log"
```

Para ejecutar únicamente las pruebas del caso 1:

```powershell
.\mvnw.cmd test "-Dtest=VetServiceTest,VetServiceMockitoTest" "-Dlogging.file.name=target/test-application.log"
```

`VetServiceTest` prueba la persistencia con H2, fuerza la escritura y limpia la
caché JPA antes de comprobar los resultados. Cada prueba revierte sus cambios.
`VetServiceMockitoTest` prueba el servicio sin iniciar Spring ni una base de datos,
con el mapper real y el repositorio simulado. Maven carga el agente de Mockito al
iniciar las pruebas, evitando los problemas de conexión del agente dinámico en
Windows con Java 21.

Las tablas y datos iniciales ya están en `schema.sql` y `data.sql`; H2 es el perfil
predeterminado. No se necesita instalar MySQL para estas pruebas. Las clases nuevas
siguen los paquetes de `AGENTS.md`; la aplicación escanea también esos paquetes
para conservar la compatibilidad con el módulo existente de mascotas.

Para el informe, adjuntar la salida de las pruebas, el enlace del repositorio y
las evidencias de colaboración realizadas por el equipo. Los reportes de Maven
se generan en `target/surefire-reports`.
