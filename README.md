# ProyectoRabbit

# Arquitectura del Sistema

# Producer (Emisor)  

- Consume datos de una API externa mediante GET.

- Clasifica las transacciones por banco y las publica en colas específicas de RabbitMQ.

- Broker (RabbitMQ):  Actúa como buffer intermedio garantizando que ninguna transacción se pierda.

- Organiza los mensajes en colas independientes (BAC, BI, BANRURAL, etc.).

# Consumer (Receptor):

- Escucha las colas de forma constante.

- Al recibir un mensaje, lo deserializa y realiza un POST hacia la API final en Amazon AWS para su almacenamiento definitivo.


# Tecnologías Utilizadas

- Java 11

- Gestor de Dependencias: Maven

- Broker de Mensajería: RabbitMQ

- Librería JSON: Jackson (con soporte para JavaTime)

- Infraestructura: AWS (Amazon Web Services)

# Cómo ejecutar el proyecto

Pre-requisitos

- Tener RabbitMQ corriendo localmente (Docker o instalación nativa).

- Java JDK 11

# Pasos

Clonar el repositorio:

git clone https://github.com/OscarSandoval19/ProyectoRabbit.git

- Iniciar el Producer: Ejecutar la clase Main del proyecto Producer para descargar las transacciones y enviarlas a RabbitMQ.

- Iniciar el Consumer: Ejecutar la clase Main del proyecto Consumer para que empiece a escuchar las colas.

- Observar la consola del Consumer para confirmar los códigos 200 y 201 OK de la API de Amazon.

Video de explicacion: https://drive.google.com/file/d/1GYiDIsbx5l9ApmKpk9I7c-YRFeEI1QUz/view?usp=sharing
