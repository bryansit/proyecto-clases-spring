# EVALUAGO - Sistema de Gestión de Clases y Evaluación en Tiempo Real

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen)
![Java](https://img.shields.io/badge/Java-17-orange)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Railway](https://img.shields.io/badge/Deployed%20on-Railway-black)
![Spring Security](https://img.shields.io/badge/Security-Authenticated-red)

**EVALUAGO** es una solución Full Stack diseñada para instituciones educativas que buscan digitalizar la interacción entre docentes y estudiantes. La plataforma permite gestionar aulas virtuales, crear rúbricas personalizadas y mantener comunicación instantánea mediante WebSockets.



## 🚀 Características Principales

- **Gestión de Clases:** Los docentes crean aulas con códigos únicos de 6 dígitos; los estudiantes se unen instantáneamente ingresando dicho código.
- **Comunicación en Tiempo Real:** Chat integrado por clase utilizando el protocolo **STOMP sobre WebSockets**, permitiendo soporte en vivo.
- **Sistema de Rúbricas:** Creación dinámica de criterios de evaluación para autoevaluación, coevaluación y evaluaciones grupales/individuales.
- **Seguridad Híbrida:** Autenticación robusta con **Spring Security**, soportando Login tradicional y **OAuth2 con Google**.
- **Gestión de Archivos:** Soporte para carga de instrucciones en formato Excel y entrega de trabajos.

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 17 & Spring Boot 3**
- **Spring Security:** Gestión de roles (ADMIN, DOCENTE, ESTUDIANTE) y OAuth2.
- **Spring Data JPA:** Persistencia con MySQL.
- **Spring WebSocket:** Mensajería bidireccional en tiempo real.

### Frontend
- **Thymeleaf:** Motor de plantillas dinámicas para el servidor.
- **Bootstrap 5:** Diseño responsivo y moderno con componentes UI avanzados.
- **JavaScript:** Manejo de lógica de Sockets y validaciones de cliente.

### Infraestructura
- **MySQL:** Base de Datos relacional.
- **Railway:** Hosting y despliegue continuo (CI/CD).

## 📂 Estructura del Proyecto (Back-End)

- `Config/`: Configuraciones de Seguridad y WebSockets.
- `Controlador/`: Lógica de navegación y manejo de eventos de Chat.
- `Entidad/`: Modelado de datos (Usuario, Clase, Rubrica, Trabajo).
- `Repositorio/`: Interfaces para operaciones CRUD con la base de datos.

## 🔧 Configuración e Instalación

1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/tu-usuario/evaluago.git](https://github.com/tu-usuario/evaluago.git)
   cd evaluago
