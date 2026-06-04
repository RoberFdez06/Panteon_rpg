# Panteon_rpg
Panteón RPG — Mazmorra Roguelike
Panteón RPG es un videojuego de rol táctico en texto y entornos gráficos desarrollado en Java utilizando la librería Swing. El sistema implementa mecánicas de estilo Roguelike, donde el usuario debe guiar a un héroe a través de una torre o mazmorra infinita habitada por monstruos mitológicos. La muerte del personaje es permanente, lo que desencadena la transferencia de sus registros a un histórico de héroes caídos en la base de datos y la exportación de un epitafio físico en formato de texto plano.

Este software demuestra la integración de patrones de diseño de software, persistencia híbrida (Bases de datos relacionales junto con archivos de texto plano) y una arquitectura basada en el patrón arquitectónico MVC (Modelo-Vista-Controlador).

Características Principales
Sistema de Clases Balanceado: Dispone de 5 arquetipos con roles e identidades diferenciadas (Guerrero, Mago, Pícaro, Tanque y Arquero) cuyas estadísticas base han sido calibradas matemáticamente para ofrecer una experiencia equilibrada frente al catálogo de enemigos.

Generación de Encuentros Escalables: La dificultad de los monstruos se adapta al piso actual del jugador. El sistema identifica y escala los atributos base mediante multiplicadores específicos cuando se trata de un minijefe (cada 5 pisos) o de un jefe de zona (cada 10 pisos).

Bestiario y Registro de Avistamientos: Implementación de un registro dinámico que almacena de manera persistente las criaturas descubiertas por el héroe a lo largo de su partida.

Muerte Permanente (Permadeath): Si los puntos de vida del héroe se reducen a cero, la partida activa se gestiona de manera definitiva y los datos del personaje pasan a formar parte del registro histórico del juego.

Persistencia Híbrida de Datos:

Base de Datos (MySQL): Gestión de estados de partidas activas (con un límite de 3 ranuras simultáneas), progreso del héroe, estadísticas base de las clases, catálogo de monstruos y el registro del cementerio.

Archivos de Texto (BufferedWriter): Al producirse la derrota, el sistema genera automáticamente un archivo .txt con un formato estructurado que actúa como epitafio físico del héroe en el sistema de archivos local.

Arquitectura del Software (MVC)
El proyecto se estructura bajo el patrón Modelo-Vista-Controlador, garantizando la separación de responsabilidades:

modelo: Aloja las entidades de datos del sistema (Personaje, Monstruo) así como los componentes encargados de la conexión y ejecución de sentencias SQL (ConexionBD, ConsultasRPG).

vista: Compuesto por las interfaces gráficas de usuario desarrolladas en Java Swing, incluyendo las pantallas de menú principal, combate, victoria, derrota y la interfaz especializada VistaSubidaNivel.

controlador: Dirige los flujos y las reglas de negocio del juego. Destaca la clase AccionUsuarioController, la cual se implementa bajo el patrón de diseño Singleton para centralizar la gestión de eventos de botones, persistencia en tiempo real y la delegación de transiciones de pantalla a través de NavegacionController.

Modelo de Datos (Esquema de la BD)
El diseño relacional de la base de datos se compone de las siguientes entidades:

partidas: Almacena el estado global y fecha de creación de la sesión de juego.

clases_personaje: Contiene las plantillas de atributos base balanceados competitivamente.

personajes: Registra las estadísticas en tiempo real de los héroes en juego, vinculados a su partida correspondiente.

monstruos: Catálogo global con los atributos de las criaturas mitológicas del juego.

bestiario_partida: Tabla relacional de correspondencia que asocia las partidas con los monstruos descubiertos.

cementerio_heroes: Registro histórico y definitivo de los personajes fallecidos en combate.

Estructura de Salida de Archivos
Al ocurrir el deceso de un personaje, el controlador utiliza la optimización de flujos de Java (BufferedWriter y FileWriter) para generar un archivo físico. El sistema incluye una lógica de verificación que crea automáticamente la estructura de directorios en caso de que esta no exista en el disco duro.

Ruta de exportación parametrizada: Panteon_rpg/src/main/java/txt/

Nomenclatura del archivo: epitafio_[NombreDelHeroe].txtRequisitos e Instalación
Prerrequisitos
Java JDK 11 o versión superior.

MySQL Server 8.0 o versión superior.

Conector JDBC de MySQL (mysql-connector-java).

Configuración de la Base de Datos
Acceda a su entorno de gestión de bases de datos (MySQL Workbench, CLI u otro).

Ejecute el script SQL proporcionado para inicializar las tablas e insertar los datos base tanto de las clases de personajes como del catálogo de monstruos mitológicos.

Ejecución del Proyecto
Importe el proyecto en su Entorno de Desarrollo Integrado (IDE) como un proyecto existente.

Verifique y configure las credenciales de acceso correctas en la clase ConexionBD.java.

Inicie la ejecución desde la clase principal que instancia el menú inicial del juego.

Instrucciones de Uso y Flujo de Juego
Menú Principal: Permite iniciar una nueva partida (el sistema valida el límite máximo de 3 registros), reanudar partidas previas, examinar el Bestiario o consultar el Cementerio.

Combate: En cada piso se genera un encuentro. Al accionar el comando de ataque, se procesa el daño mutuo restando la vida actual en función del ataque del emisor y la defensa del receptor.

Progresión: La derrota de enemigos incrementa el contador de experiencia. Al alcanzar 3 monstruos derrotados, se invoca el proceso de subida de nivel, el cual clona el estado previo del personaje para contrastar de forma precisa el incremento de atributos antes de avanzar en la partida.

Derrota: Si los puntos de vida del héroe llegan a cero, el controlador procesa de forma transaccional el almacenamiento del deceso en la base de datos, ejecuta la escritura del archivo de texto plano mediante un flujo empaquetado y redirige al usuario a la interfaz de desenlace.