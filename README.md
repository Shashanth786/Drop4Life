Live Link Of Website : https://shashanth786.github.io/Drop4Life/


# Drop4Life — Blood Donation Management System

Drop4Life is a Java-based web application designed to connect blood donors with people who need blood. It provides a platform for donor registration, blood requests, donor matching, and user management.

## Features

- **User Registration and Login:** Create an account and access the application.
- **Donor Registration:** Register as a blood donor and provide donor details.
- **Blood Requests:** Submit blood requests and view matching requests.
- **Donor Matching:** Find potential donors based on relevant blood group and location details.
- **User Profile:** Access profile information.
- **Password Reset:** Reset passwords using email OTP verification.
- **Contact Us:** Submit messages through the contact form.
- **Admin Dashboard:** Manage users, donors, blood requests, and contact messages.
- **Email Notifications:** Send relevant application emails.

## Technology Stack

**Backend**
- Java 21
- Spring MVC
- Hibernate ORM

**Frontend**
- HTML
- CSS
- JavaScript
- JSP

**Database**
- MySQL

**Build and Deployment**
- Maven
- Apache Tomcat 10.1

## Project Structure

```text
Drop4Life/
├── pom.xml
└── src/
    └── main/
        ├── java/com/drop4life/
        │   ├── config/
        │   ├── controller/
        │   ├── dao/
        │   ├── model/
        │   └── service/
        ├── resources/
        └── webapp/
            ├── CSS/
            ├── HTML/
            ├── Images/
            ├── JS/
            └── WEB-INF/
```

## Local Setup

1. Install Java 21, Maven, MySQL, and Apache Tomcat 10.1.
2. Create a MySQL database named `drop4life`.
3. Configure your database password using the `DROP4LIFE_DB_PASSWORD` environment variable.
4. Configure email credentials using `DROP4LIFE_EMAIL` and `DROP4LIFE_EMAIL_APP_PASSWORD` if you want to use email features.
5. Configure the database connection and ensure the required environment variables are available to Tomcat.
6. Build the application from the project root:

   ```bash
   mvn clean package
   ```

7. Deploy the generated `target/Drop4Life.war` file to Tomcat's `webapps` directory and start Tomcat.
8. Open `http://localhost:8080/Drop4Life/` in your browser.

## Security Notes

- Never commit database passwords, email app passwords, API keys, or other credentials.
- Store sensitive configuration in environment variables.
- Public deployment requires appropriate production database, authentication, and file-storage configuration.

## Project Status

Developed as a Java web application project to demonstrate MVC architecture, database integration, user authentication, blood-request management, and donor matching.
