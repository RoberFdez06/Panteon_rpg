[README.md](https://github.com/user-attachments/files/33426135/README.md)
<p align="center">
  <img src="Panteon_rpg/src/main/resources/imagenes/Iconos/LogoPanteonRPG.jpg" alt="Panteón RPG Logo" width="260">
</p>

<h1 align="center">Panteón RPG</h1>

<p align="center">
  Roguelike por turnos con temática mitológica, desarrollado en <b>Java Swing</b> con arquitectura <b>MVC</b> y persistencia híbrida (<b>Oracle SQL</b> + ficheros de texto).
</p>

<p align="center">
  <img alt="Java" src="https://img.shields.io/badge/Java-11%2B-orange">
  <img alt="Oracle" src="https://img.shields.io/badge/Oracle-SQL-red">
  <img alt="Arquitectura" src="https://img.shields.io/badge/arquitectura-MVC-green">
  <img alt="Estado" src="https://img.shields.io/badge/estado-en%20desarrollo-yellow">
</p>

---

## Índice

1. [Descripción](#descripción)
2. [Capturas](#capturas)
3. [Características principales](#características-principales)
4. [Arquitectura (MVC)](#arquitectura-mvc)
5. [Modelo de datos](#modelo-de-datos)
6. [Archivos de salida: el epitafio](#archivos-de-salida-el-epitafio)
7. [Instalación y ejecución](#instalación-y-ejecución)
8. [Cómo se juega](#cómo-se-juega)
9. [Hoja de ruta](#hoja-de-ruta)
10. [Autor](#autor)

---

## Descripción

**Panteón RPG** es un videojuego de rol táctico con interfaz gráfica desarrollado en Java con la librería Swing. Implementa mecánicas de estilo *roguelike*: el jugador guía a un héroe por una torre (mazmorra) infinita habitada por monstruos mitológicos.

La muerte es **permanente**. Cuando el héroe cae, sus datos pasan a un histórico de héroes caídos en la base de datos y se exporta un **epitafio** en formato de texto plano.

El proyecto demuestra el uso de patrones de diseño, persistencia híbrida (base de datos relacional + archivos de texto) y una arquitectura Modelo-Vista-Controlador.

## Capturas

> 📌 _Añade aquí capturas o un GIF del juego. Es lo primero que se mira en un repositorio._

| Menú principal | Combate | Subida de nivel |
| :---: | :---: | :---: |
| ![Menú](docs/capturas/menu.png) | ![Combate](docs/capturas/combate.png) | ![Nivel](docs/capturas/subida-nivel.png) |

## Características principales

- **Cinco clases jugables** con roles diferenciados: Guerrero, Mago, Pícaro, Tanque y Arquero. Sus estadísticas base están calibradas para ofrecer una experiencia equilibrada frente al catálogo de enemigos.
- **Encuentros escalables.** La dificultad de los monstruos se adapta al piso actual. Sus atributos base se multiplican en los **minijefes** (cada 5 pisos) y en los **jefes de zona** (cada 10 pisos).
- **Bestiario.** Registro persistente de las criaturas descubiertas por el héroe durante la partida.
- **Muerte permanente (permadeath).** Si la vida del héroe llega a cero, la partida se cierra de forma definitiva y el personaje pasa al cementerio de héroes.
- **Persistencia híbrida:**
  - **Oracle SQL:** partidas activas (máximo **3 ranuras** simultáneas), progreso del héroe, estadísticas base de las clases, catálogo de monstruos y cementerio.
  - **Archivos de texto:** al ser derrotado, se genera automáticamente un `.txt` estructurado con `BufferedWriter`, que actúa como epitafio físico del héroe.

## Arquitectura (MVC)

El proyecto sigue el patrón **Modelo-Vista-Controlador** para separar responsabilidades:

| Capa | Contenido |
| --- | --- |
| **modelo** | Entidades (`Personaje`, `Monstruo`) y componentes de acceso a datos: `ConexionBD` y `ConsultasRPG` (conexión JDBC y sentencias SQL). |
| **vista** | Interfaces Swing: menú principal, combate, victoria, derrota y la pantalla especializada `VistaSubidaNivel`. |
| **controlador** | Flujo del juego y reglas de negocio. Destaca `AccionUsuarioController`, implementado con el patrón **Singleton**, que centraliza los eventos de los botones y la persistencia en tiempo real, y delega las transiciones de pantalla en `NavegacionController`. |

## Modelo de datos

| Tabla | Descripción |
| --- | --- |
| `partidas` | Estado global y fecha de creación de cada sesión de juego. |
| `clases_personaje` | Plantillas con las estadísticas base de cada clase. |
| `personajes` | Estadísticas en tiempo real de los héroes, vinculados a su partida. |
| `monstruos` | Catálogo global de criaturas mitológicas. |
| `bestiario_partida` | Tabla de relación entre partidas y monstruos descubiertos. |
| `cementerio_heroes` | Registro histórico y definitivo de los héroes caídos. |

## Archivos de salida: el epitafio

Al morir un personaje, el controlador usa `BufferedWriter` y `FileWriter` para generar un archivo físico. Si los directorios no existen, el sistema los crea automáticamente.

- **Ruta de exportación:** `Panteon_rpg/src/main/java/txt/`
- **Nombre del archivo:** `epitafio_[NombreDelHeroe].txt`

## Instalación y ejecución

### Requisitos

- Java JDK 11 o superior
- Oracle Database (por ejemplo, Oracle XE) accesible desde tu equipo
- Driver JDBC de Oracle (`ojdbc`) añadido al classpath del proyecto

### 1. Clonar el repositorio

```bash
git clone https://github.com/RoberFdez06/Panteon_rpg.git
cd Panteon_rpg
```

### 2. Configurar la base de datos

1. Abre SQL Developer, SQL*Plus u otro cliente y conéctate con tu usuario.
2. Ejecuta el script SQL para crear las tablas e insertar los datos base (clases y monstruos):

   ```sql
   Panteon_rpg/src/main/java/sql/ScriptPanteon_rpg.sql
   ```

   > 📌 _Ajusta esta ruta a donde esté tu script. Si aún no está en el repositorio, súbelo para que cualquiera pueda ejecutar el juego._

### 3. Configurar las credenciales

Edita `ConexionBD.java` con tu usuario, contraseña y URL de conexión:

```java
private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
private static final String USUARIO = "tu_usuario";
private static final String PASSWORD = "tu_contraseña";
```

> Ajusta el SID o el nombre de servicio (`XE`, `XEPDB1`, etc.) según tu instalación.

### 4. Ejecutar

1. Importa el proyecto en tu IDE como proyecto existente.
2. Asegúrate de que el driver `ojdbc` está en el classpath.
3. Ejecuta la clase principal que instancia el menú inicial.

## Cómo se juega

1. **Menú principal:** inicia una partida nueva (máximo 3 guardadas), reanuda una anterior, consulta el Bestiario o visita el Cementerio.
2. **Combate:** en cada piso aparece un encuentro. Al atacar, el daño mutuo se calcula restando de la vida actual el ataque del emisor menos la defensa del receptor.
3. **Progresión:** cada monstruo derrotado suma experiencia. Al derrotar a **3 monstruos** se produce la subida de nivel; el sistema clona el estado previo del personaje para mostrar con precisión cuánto crece cada atributo.
4. **Derrota:** al llegar la vida a cero, el controlador guarda el fallecimiento en la base de datos de forma transaccional, escribe el epitafio en disco y lleva al jugador a la pantalla de desenlace.

## Hoja de ruta

Ideas y ampliaciones previstas:

- [ ] _Por definir_
- [ ] _Por definir_
- [ ] _Por definir_

## Autor

**Roberto Fernández Mañero**
Estudiante de Desarrollo de Aplicaciones Multiplataforma (DAM)

- GitHub: [@RoberFdez06](https://github.com/RoberFdez06)
- LinkedIn: [robertofernandezmanerodev](https://www.linkedin.com/in/robertofernandezmanerodev)

> Proyecto final de 1.º de DAM (Programación).
