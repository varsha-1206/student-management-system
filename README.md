
# Student Management System

A Java desktop application for managing student records using Java Swing,
JDBC, and MySQL.

## Features

- Add student records
- Display all students in a table
- Search students by ID
- Update student details
- Delete student records with confirmation
- Validate student IDs, names, and marks
- Store records permanently in MySQL

## Technologies Used

- Java
- Java Swing
- JDBC
- MySQL
- MySQL Connector/J
- Visual Studio Code

## Prerequisites

- JDK installed
- MySQL Server running
- MySQL Connector/J downloaded
- MySQL Workbench (recommended)

## Database Setup

Create the database and table by executing:

```sql
CREATE DATABASE student_db;
USE student_db;

CREATE TABLE students (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    marks DECIMAL(5,2) NOT NULL,
    CHECK (marks >= 0 AND marks <= 100)
);
```

## Configuration

Set the database password as an environment variable.

PowerShell:

```powershell
$env:DB_PASSWORD="YOUR_MYSQL_PASSWORD"
```

The application uses `root` as the default database username.
To use another username, set `DB_USER`.

## Run the Application

1. Download MySQL Connector/J from:
   https://dev.mysql.com/downloads/connector/j/

2. Extract the archive and place the JAR in the `lib` directory.
   Update the JAR filename in the commands if your version differs.

3. Open a terminal in the project directory.

4. Compile:

```powershell
javac -cp ".;lib\mysql-connector-j-26.10.0\mysql-connector-j-26.10.0.jar" Student.java DBConnection.java StudentDAO.java StudentManagementGUI.java
```

5. Run:

```powershell
java -cp ".;lib\mysql-connector-j-26.10.0\mysql-connector-j-26.10.0.jar" StudentManagementGUI
```

## Project Structure

- `Student.java` — Student model
- `DBConnection.java` — MySQL connection
- `StudentDAO.java` — Database operations
- `StudentManagementGUI.java` — Desktop interface
- `Main.java` — Console interface

## Learning Outcomes

This project demonstrates object-oriented programming,
database connectivity, SQL CRUD operations, input validation,
exception handling, and Java Swing GUI development.