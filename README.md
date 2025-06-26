# 📝 SimpleNotes – Personal Notes & Reminder Backend

Ein sicheres, modular aufgebautes Spring Boot Backend für persönliche Notizen und Erinnerungen – mit JWT-Authentifizierung, intelligenter Suche und geplanter Zeitsteuerung.

## 🚀 Features

- ✅ User Registration & Login mit JWT
- ✅ Passwort-Hashing mit BCrypt
- ✅ CRUD für Notizen (mit Tags & Kategorien)
- ✅ Reminder-Logik mit geplanter Ausführung
- ✅ DTO-Validierung mit globalem Error Handling
- 🔜 Intelligente Suche nach Titel & Inhalt
- 🔜 Google Login (OAuth2)
- 🔜 Unit Tests, Swagger, GitHub Actions
- 🔜 Offline-Modus & Cloud-Sync

## 🧠 Ziel

Dieses Projekt dient als Lernumgebung für moderne Backend-Entwicklung mit Java. Fokus liegt auf:

- sauberer Architektur (DTOs, Services, Repositories, Filter)
- Testbarkeit & Erweiterbarkeit
- echter Anwendung statt Tutorials

## ⚙️ Tech Stack

| Technologie        | Einsatzbereich           |
|--------------------|--------------------------|
| Java 21            | Hauptsprache             |
| Spring Boot 3      | Backend Framework        |
| Spring Security    | Authentifizierung        |
| PostgreSQL         | Datenbank                |
| jjwt               | JWT-Erstellung           |
| Hibernate / JPA    | Datenbankzugriff         |
| Maven              | Build-Tool               |

## 🧪 Build & Run

### Voraussetzungen
- Java 21
- PostgreSQL (lokal oder remote)
- Maven

### Starten

```bash
git clone https://github.com/FExB17/simplenotes.git
cd simplenotes-backend
# application.properties anpassen
mvn clean install
mvn spring-boot:run
```

## 🔒 Sicherheit

- Authentifizierung über JWT mit Refresh-Token-System
- Tokens enthalten Session-IDs & werden bei Logout deaktiviert
- Zugang zu Notizen und Erinnerungen nur durch Besitzer

## 🔄 Roadmap

```text
[x] JWT Login
[x] Reminder mit Zeitsteuerung
[x] Tagging & Suche
[ ] OAuth2 Login
[ ] Unit Tests
[ ] Swagger UI & Doku
[ ] Cloud-Sync
```

## 📁 Projektstruktur

```text
controller/   → REST-Endpoints
service/      → Business-Logik
repository/   → Datenbank-Zugriff
dto/          → Datenübertragung (Request/Response)
model/        → JPA-Entities
security/     → JWT & Filter
config/       → Security, CORS, Swagger
exception/    → Globales Error-Handling
```

## 👤 Autor

**[Furkan]**  
Java-Lernender mit Fokus auf Backend-Entwicklung & Clean Code.  
Bootcamp SDET · Spring Boot · PostgreSQL · Open to work  
🌐 LinkedIn: [linkedin.com/in/furkan-ervan](https://linkedin.com/in/furkan-ervan)

---

**Ich freue mich über Feedback, Ideen oder Code-Reviews!** 😊
