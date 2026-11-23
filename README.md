# TecMundo Connect (AgroDuocApp)

## Informacion General
Este proyecto académico desarrollado para Duoc UC consiste en una aplicación móvil Fullstack en Kotlin utilizando **Jetpack Compose**, arquitectura **MVVM**, persistencia local para sesión con **Room / SharedPreferences**, y conexión a un backend externo en la nube mediante **Xano (REST API)**. 

La aplicación está diseñada para conectar la producción agrícola con servicios de aplicación e insumos ("TecMundo Connect"), permitiendo a los usuarios registrarse como productores o aplicadores, iniciar sesión con autenticación segura por Token Bearer, visualizar su perfil, gestionar solicitudes y ofertas, y eliminar cuentas de forma permanente.

---

## Get Started
Para evaluar y utilizar la aplicación de forma directa, **se proveerá un APK firmado en modo release** listo para instalar en emuladores o dispositivos Android.

Si deseas clonar el repositorio y revisar el código fuente en **Android Studio**:

```shell
git clone https://github.com/Proyecto-DAM-Grupo2-002D/AgroDuoc.git
```

> [!NOTE]
> La aplicación requiere conexión a internet para consumir los endpoints de la API en Xano y permisos de cámara para la captura de la foto de perfil en el registro.

> [!TIP]
> Para probar la app, instala el APK proporcionado, presiona "Crear una Cuenta", completa el formulario seleccionando tu rol (Productor o Aplicador) y captura tu foto con la cámara integrada. Luego podrás iniciar sesión con tus credenciales.

---

## Flujo de Usuario y Uso de la Aplicación

1. **Pantalla de Inicio (Landing)**:
   - Bienvenida a TecMundo Connect con gradiente agrícola y acceso directo a "Iniciar Sesión" y "Crear una Cuenta".
2. **Registro de Cuenta (Sign Up)**:
   - Formulario con validaciones de RUT chileno, email, teléfono y contraseña (mínimo 4 caracteres).
   - Selección de rol mediante botones interactivos (`FilterChip`).
   - Integración con el recurso nativo **Cámara** para capturar la foto de perfil y guardarla codificada en Base64 en el servidor.
3. **Inicio de Sesión (Login)**:
   - Autenticación conectada a Xano (`/auth/login`), generando y almacenando el token de sesión (`authToken`) para peticiones seguras (`AuthInterceptor`).
4. **Pantalla Principal (Home & Solicitudes)**:
   - Listado de solicitudes y ofertas agrícolas con diseño moderno en tarjetas Material 3.
5. **Perfil de Usuario**:
   - Visualización de datos de la cuenta obtenidos directamente desde la API en la nube.
   - Opción de eliminar la cuenta de forma permanente conectada al backend (`DELETE /usuario/{usuario_id}`).

---

## Atributos de las Entidades del Backend (Xano)

> **usuario**
> - id (PK)
> - created_at
> - nombre
> - apellido
> - rut
> - email
> - password
> - telefono
> - rol ("productor" o "aplicador")
> - foto_perfil (Base64)
> - calificacion

> **solicitud**
> - id (PK)
> - productor_id (FK)
> - superficie_ha
> - tipo_producto
> - tiene_agua
> - tipo_aplicacion
> - estado

> **oferta**
> - id (PK)
> - solicitud_id (FK)
> - aplicador_id (FK)
> - precio_por_ha
> - precio_total_iva
> - estado

---

## Autores
- Adriel Yañez
- Javier Fuentealba
- Tomás Martínez
### Grupo 2 - DSY1105 - Duoc UC
