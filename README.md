# Northwind Entity Manager

Applicazione web full stack per la gestione CRUD della tabella `Categories` del database Northwind, realizzata con `Java Servlet`, `Hibernate`, `SQLite` e una SPA in `HTML/CSS/JavaScript` che usa `XMLHttpRequest`.

## Funzionalita'

- Visualizzazione completa delle categorie presenti nel database.
- Inserimento di una nuova categoria.
- Modifica di una categoria esistente.
- Eliminazione con conferma.
- Ricerca istantanea lato client.
- Interfaccia single-page con aggiornamento dinamico della tabella senza reload.
- API JSON centralizzata su `/api/categories`.

## Stack tecnologico

- Java 17
- Maven
- Jakarta Servlet 6
- Hibernate ORM 6
- SQLite JDBC
- Gson
- HTML, CSS, JavaScript vanilla con `XMLHttpRequest`

## Struttura del progetto

- `src/main/java/com/mycompany/progetto_finale/model/Category.java`: entity Hibernate per `Categories`.
- `src/main/java/com/mycompany/progetto_finale/dao/CategoryDao.java`: operazioni CRUD.
- `src/main/java/com/mycompany/progetto_finale/web/CategoryServlet.java`: controller REST JSON.
- `src/main/webapp/index.html`: SPA principale.
- `src/main/webapp/app.js`: logica frontend asincrona.
- `src/main/webapp/styles.css`: interfaccia grafica responsive.
- `src/main/webapp/northwind.db`: database Northwind SQLite.

## Endpoint API

### `GET /api/categories`

Restituisce tutte le categorie.

### `GET /api/categories?id=1`

Restituisce la categoria con ID specificato.

### `POST /api/categories`

Inserisce una nuova categoria.

Esempio body JSON:

```json
{
  "categoryName": "Seasonal Specials",
  "description": "Prodotti stagionali e promozioni limitate."
}
```

### `PUT /api/categories?id=8`

Aggiorna nome e descrizione della categoria indicata.

### `DELETE /api/categories?id=8`

Elimina la categoria indicata.

## Avvio con Tomcat

### 1. Requisiti

- JDK 17 installato
- Apache Maven 3.9+
- Apache Tomcat 10.1+ oppure NetBeans/IDE compatibile con Jakarta EE 10

### 2. Build

Apri un terminale nella cartella del progetto ed esegui:

```bash
mvn clean package
```

Verrà generato il file:

```text
target/northwind-entity-manager.war
```

### 3. Deploy su Tomcat

Puoi fare deploy in uno di questi modi:

1. Copia `target/northwind-entity-manager.war` nella cartella `webapps` di Tomcat.
2. Oppure importa il progetto nell'IDE e avvialo direttamente sul server Tomcat configurato.

### 4. Apertura nel browser

Se deployato in Tomcat locale:

```text
http://localhost:8080/northwind-entity-manager/
```

## Note sul database

- Il database `northwind.db` e' incluso dentro `src/main/webapp`.
- All'avvio della servlet, il percorso reale del file viene risolto automaticamente e passato a Hibernate.
- La tabella gestita e' `Categories`, scelta per avere un CRUD pulito senza relazioni complesse come `Orders`.

## Screenshot

Per la consegna finale puoi inserire qui gli screenshot dei test effettuati:

- `docs/screenshots/home.png`
- `docs/screenshots/create.png`
- `docs/screenshots/update.png`
- `docs/screenshots/delete.png`

## Repository e consegna

Per la consegna consigliata:

1. Esegui il deploy su Tomcat e verifica il CRUD.
2. Cattura alcuni screenshot dell'app in funzione.
3. Comprimi il progetto in `.zip`.
4. Pubblica il contenuto anche su GitHub insieme a questo `README.md`.
