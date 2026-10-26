# Community Events Platform
The platform allows visitors to browse published commnuity events and register anonymously, organisers to submit events and an administrator to review and publish submissions.

 Features
   Visitors: View published events, open event details and register for an event.
   Organisers: Create events and view events submitted under an organiser ID.
   Administrators: View all events and publish events that are awaiting review.
   Validation: Event title and description are required, events dates must be in the future and       capacity must be greater than zero.
   Registration rules: Visitors can register only for published events. Registrations are rejected    when an event has reached capacity.
   Persistence: H2 database stores event and registration data.
   Activity logging: Publishing and registration actions are logged by the application.
   Automated tests: Unit tests cover event and registration service behaviour.
Technology stack
  Java21
  Spring Boot
  Maven
  Spring Web
  Spring Data JPA
  H2 Database
  HTML, CSS and JavaScript
  JUnit and Mockito for tests
Requirements
  Install the following before running the project:
    1. JDK 21 (make sure java -version reports Java 21).
    2. Git, if you want to clone the repository.
    3. An IDE or editor such as Visual Studio Code.
The project includes the Maven Wrapper (mvnw.cmd), so a separate Maven installation is not normally required.

Setup instructions
  1. Get the source code
    Clone the repository after it has been uploaded to GitHub:
      git clone <https://github.com/Chelsea-cks/ClickATell>
      cd communityeventsplatform
    Alternatively, download the repository as a ZIP from GitHub and extract it.
2. Check Java
   Open a terminal in the project root and run:
    java -version
   Confirm that Java 21 is installed.
3. Run the application
  On Windows PowerShell, run:
    .\mvnw.cmd spring-boot:run
  Wait for Spring Boot to finish starting, then open:
    - Website: http://localhost:8080/
    - Events API: http://localhost:8080/api/events
    - H2 console: http://localhost:8080/h2-console
  To stop the application, press Ctrl+C in the terminal.
4. H2 console connection details
  Use these values on the H2 console login page when the application is running:
    - JDBC URL: jdbc:h2:file:./data/communityeventsdb
    - User Name: sa
    - Password: leave blank
  The H2 console is enabled for local development. Do not expose it publicly in a production      deployment.
5. Run automated tests
  In a second terminal opened at the project root, run:
    .\mvnw.cmd test
   The test configuration uses an in-memory H2 database so tests can run separately from the       local file-based application database.
   
Main API endpoints
   api/events - list published events
   /api/events/{id} - get an event by ID
   /api/events - create an event
   /api/events/admin/all - list all events for the demo admin view
   /api/events/organiser/{organiserId} - list events for an organiser
   /api/events/{id}/publish - publish an event
   /api/events/{eventId}/registrations - register for published event

Example: create an event
    Send a POST request to http://localhost:8080/api/events with Content-Type: application/json     and a JSON body such as:
    {
      "title": "Community Coding Workshop",
      "description": "A beginner-friendly workshop to learn programming.",
      "dateTime": "2026-11-15T10:00:00",
      "capacity": 30,
      "organiserId": "organiser1"
    }
Use a date and time in the future when testing. Newly created events start with PENDING_REVIEW and must be published before they appear in the visitor list or can accept registrations.

Project structure
src/
  main/
    java/za/co/communityevents/communityeventsplatform/
      Event.java
      EventRepository.java
      EventService.java
      EventController.java
      Registration.java
      RegistrationRepository.java
      RegistrationService.java
      RegistrationController.java
      GlobalExceptionHandler.java
      EventNotFoundException.java
      HomeController.java
      CommunityeventsplatformApplication.java
    resources/
      application.properties
      static/
        index.html
        style.css
        app.js
  test/
    java/
      ...automated tests...
    resources/
      application.properties
The exact test filenames may vary depending on the files present in the repository.

Data model
Event
  Stores the event identifier, title, description, date and time, capacity, publication status,   and organiser identifier. Events are initially created with PENDING_REVIEW; an administrator    can change the status to PUBLISHED.
Registration
  Stores a generated registration ID, the related event ID, and the registration timestamp. The   demo does not request or store a visitor's name, email address, or other personal details.
Error handling and validation
  The API returns meaningful HTTP status codes for common outcomes:
    - 201 Created when an event or registration is created successfully.
    - 400 Bad Request when supplied data fails validation or a business rule.
    - 404 Not Found when the requested event does not exist.
  The application validates required fields, future event date/time, positive capacity,           publication status, and remaining event capacity.
Demo role selector and security limitations
  The Visitor, Organiser, and Admin options in the website are demo views only. They are not a    real authentication or authorisation system. The organiser ID is supplied by the user, and      the admin endpoints are not protected by login roles. This is acceptable only as a              demonstration limitation; a production application would require secure authentication,         server-side role-based authorisation, stronger input controls, and additional security          configuration.
Database files
  The local H2 database is stored in the data/ directory. Keep the database file locally if you   want to retain test/demo data. Do not commit local database files or secrets to GitHub unless   the assignment specifically requires them. The database is recreated separately for automated   tests.
Troubleshooting
  - Website shows only a welcome message: Check that index.html, style.css, and app.js are in       src/main/resources/static/. Ensure HomeController does not return a text response for /,         then restart the application and hard-refresh the browser with Ctrl+F5.
  - Port 8080 is already in use: Stop the other application using the port, or configure this       application to use another port.
  - Java version issue: Install/configure JDK 21 and check java -version.
  - Events do not appear: Check that the event status is PUBLISHED and visit                         http://localhost:8080/api/events.
  - Tests fail because of a database lock: Confirm src/test/resources/application.properties         uses an in-memory H2 URL such as jdbc:h2:mem:communityevents_testdb, then rerun the tests.
    
AI use declaration

  AI assistance was used during development to help understand Spring Boot concepts, plan and troubleshoot implementation steps, review code, and draft documentation. The project was run and tested by me during development. I am responsible for reviewing the submitted source code, understanding how it works, verifying the final behaviour, and ensuring the submission follows the institution's rules for AI-assisted work.

Author
  Chelsea Solomons

Demo Video 
https://youtu.be/iBSJTveUPRM 
