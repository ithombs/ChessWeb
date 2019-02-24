# ChessWeb
Version 1.1 of ChessWeb. This time using Hibernate and other useful Spring tools.

This project is mostly a sandbox for prototyping, sketching out, and implementing various pieces of functionality that I come across that interest me. Points of interest within the project are as follows:

* Basic usage of Spring functionality
   * Login security
   * User management
   * Web socket usage
   * CRUD functionality via JPA and Hibernate
   * Aspect oriented programming

* Simple AI
   * Min-Max algorithm based AI as chess opponent

* Connecting with outside systems
   * RabbitMQ used as a message broker for web sockets(STOMP) used to communicate between a client and server for chess match information




##IMPORTANT NOTE##
The application.properties file is ommitted from the repository on purpose. In order to fully run the application, besides requiring other pieces such as a database and RabbitMQ, you need to create and fill out an application.properties file with the following keys.


jdbc.driverClassName=

jdbc.url=

jdbc.username=

jdbc.password=

hibernate.dialect=

hibernate.show_sql=

hibernate.format_sql=

hibernate.c3p0.min_size=

hibernate.c3p0.max_size= 
hibernate.c3p0.timeout=

hibernate.c3p0.max_statements=

rabbitmq.username=

rabbitmq.password=

rabbitmq.host=

rabbitmq.port=

 
