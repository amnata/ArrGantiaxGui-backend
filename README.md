ArrGantiaxGui – Backend Spring Boot

Ce projet représente le backend de la plateforme ArrGantiaxGui.
Il fournit l’API REST, la gestion de la sécurité, l’accès aux données ainsi que toute la logique métier utilisée par le frontend Angular et les microservices associés.

🚀 Technologies Utilisées

Java 17+

Spring Boot

Spring Security + JWT

Spring Data JPA

Hibernate

MySQL / PostgreSQL (selon configuration)

Maven

📂 Structure du Projet
src/
├── app/
│   ├── config/          # Configuration générale de l'application (CORS, Beans, etc.)
│   ├── controller/      # Contrôleurs REST (points d'accès API)
│   ├── dto/             # Data Transfer Objects (entrées/sorties API)
│   ├── entity/          # Entités JPA représentant les tables de la base de données
│   ├── model/           # Modèles métier utilisés dans la logique interne
│   ├── repository/      # Interfaces JPA pour la gestion des données
│   ├── security/        # Gestion de l’authentification (JWT, filtres, services)
│   └── service/         # Services métier (logique applicative)
├── resources/
│   ├── application.properties   # Configuration du backend
│   ├── data.sql                # Données initiales
│   └── schema.sql              # Structure de la base (si utilisée)
└── pom.xml                     # Dépendances Maven

▶️ Lancement du Projet
1. Cloner le projet
git clone <https://github.com/amnata/ArrGantiaxGui-backend.git>
cd ArrGantiaxGui-backend

2. Configurer la base de données

Modifier src/main/resources/application.properties :

spring.datasource.url=jdbc:mysql://localhost:3306/arrgantiax
spring.datasource.username=root
spring.datasource.password=ton_mdp
spring.jpa.hibernate.ddl-auto=update

3. Lancer le backend

Avec Maven :

mvn spring-boot:run


Ou via l’IDE :

➡️ Lancer AgriAppApplication.java

Le backend sera disponible sur :

http://localhost:8080

🔐 Authentification (JWT)

Le backend utilise un système d’authentification basé sur JSON Web Tokens (JWT).
Les endpoints sécurisés nécessitent un header :

Authorization: Bearer <votre_token>
Les tokens sont générés lors du login.

📚 Pré-requis

Java 17+

Maven 3+

MySQL ou PostgreSQL

IDE (IntelliJ, VS Code, Eclipse…)

