# Sistema de Gestión de Clínica Veterinaria

Proyecto final de Programación IV — UISIL. Aplicación de escritorio en Java
para administrar una clínica veterinaria pequeña: clientes, mascotas,
veterinarios, citas, consultas, servicios y facturación.

## Integrantes

- Breyner Mora Jiménez — Clientes, mascotas y relación de composición
- Pamela María Montiel Serrano — Veterinarios y citas
- Jason Steve Ortiz Tenorio — Consultas, servicios y jerarquía polimórfica
- Joselyn Rodríguez Bonilla — Facturación, base de datos/JDBC e integración del multihilo

## Tecnologías

- Java (Swing para la interfaz gráfica)
- JDBC (conector MySQL)
- MySQL (base de datos relacional)
- iText (generación de reportes PDF)
- Git / GitHub

## Estructura del proyecto

```
src/
├── Modelo/         Clases del dominio, herencia, enums
├── Datos/          DAOs y conexión JDBC
├── Negocio/        Reglas de negocio, validaciones, excepciones
├── Presentacion/   Paneles Swing y ventana principal
└── Reportes/       Generación de PDF de facturas
```

## Pasos para ejecutar

1. Clonar el repositorio.
2. Crear la base de datos ejecutando `database.sql` en MySQL Workbench
   (o `mysql -u root -p < database.sql`).
3. Abrir el proyecto en tu IDE (NetBeans/IntelliJ) apuntando a la carpeta
   `src`.
4. Agregar el conector JDBC de MySQL (`mysql-connector-j`) como librería
   del proyecto.
5. Ajustar el usuario y la contraseña de MySQL en `src/Datos/ConexionBD.java`.
6. Compilar y ejecutar la clase `Presentacion.Principal`.

## Configuración de la base de datos

- Nombre sugerido: `clinica_veterinaria`
- Script de creación: `database.sql` (en la raíz del repositorio)
- Diagrama entidad-relación: `diagrama_er_clinica.png`

## Repositorio

https://github.com/breynermj2024-debug/Sistema_de_Gestion_de_Clnica-Veterinaria
