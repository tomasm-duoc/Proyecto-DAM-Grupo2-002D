# TecMundo Connect (AgroDuocApp)

## Informacion General
Este proyecto académico desarrollado para Duoc UC consiste en una aplicación móvil en Kotlin utilizando **Jetpack Compose**, arquitectura **MVVM** y persistencia local con **Room**, diseñada para conectar la producción agrícola con servicios de aplicación e insumos ("TecMundo Connect").

Se trabaja con las librerías de AndroidX, Material Design 3, Navigation Compose, KSP y SQLite/Room.

---

## Get Started
Se recomienda el uso de **Android Studio** y un emulador Android configurado con API 34 o superior (o dispositivo físico).

Se debe clonar el repositorio para su ejecución con el comando:

```shell
git clone https://github.com/Proyecto-DAM-Grupo2-002D/AgroDuoc.git
```

> [!NOTE]
> Al momento de la ejecución, la base de datos local Room se encarga de estructurar las tablas de usuarios y productos automáticamente sin necesidad de configuración manual previa.

> [!TIP]
> Para probar la aplicación de manera fluida, se recomienda iniciar ejecutando la aplicación en el emulador y utilizar el flujo de registro de usuario con la cámara integrada.

---

## Atributos de las Entidades de la Base de Datos (Room)

> **User**
> - rut (Primary Key)
> - nombre
> - apellido
> - email
> - telefono
> - rol ("Productor" o "Aplicador")
> - password
> - fotoPerfilUri

> **Producto**
> - id (Primary Key, AutoGenerate)
> - nombre
> - precio
> - nombreVendedor
> - calificacion
> - imagenProductoRes
> - imagenVendedorRes
> - imagenProductoUri
> - imagenVendedorUri

---

> [!IMPORTANT]
> ### REQUISITOS DE EJECUCIÓN
> LA APLICACION REQUIERE QUE EL DISPOSITIVO O EMULADOR TENGA SOPORTE PARA CÁMARA PARA CAPTURAR LA FOTO DE PERFIL DEL USUARIO DURANTE EL REGISTRO.

> [!NOTE]
> ## Arquitectura y Módulos Principales del Sistema
>
> - **Capa Model (`model/`)**
>   - Entidades Room y Data Classes (`User.kt`, `Producto.kt`)
>
> - **Capa Repository (`repository/`)**
>   - Interfaces DAO (`UserDao.kt`), Base de Datos Singleton (`AppDatabase.kt`) y Repositorio (`UserRepository.kt`)
>
> - **Capa ViewModel (`viewmodel/`)**
>   - Lógica de negocio y validación de formularios asíncrona (`LoginViewModel.kt`, `RegisterViewModel.kt`)
>
> - **Capa UI (`ui/`)**
>   - Pantallas Compose (`LandingScreen.kt`, `LoginScreen.kt`, `RegisterScreen.kt`, `HomeScreen.kt`), temas y navegación con `NavHost` (`AppNavigation.kt`)

---

## Autores
- Tomás Martínez
- Adriel Yañez
### Grupo 2 - DSY1105 - Duoc UC
