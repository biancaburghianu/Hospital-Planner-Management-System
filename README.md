# Proiect Programare Avansata
Repository pentru proiectul Hospital Planner de la materia Programare Avansata.
Echipa: Burghianu Bianca, Ghioc Otilia

# Hospital Planner Management System

The Hospital Planner Management System is an application built with Spring Boot and PostgreSQL, designed to facilitate the management of hospital appointments.

## Features

- Register patients and doctors
- Authenticate users using JSON Web Tokens (JWT)
- Create, view, and manage appointments
- Retrieve available appointment times
- Allows a patient to choose favorite doctors and see their availability

## Technologies Used

- Java
- Spring Boot
- PostgreSQL
- Hibernate
- JSON Web Tokens (JWT)
- Maven

## Prerequisites

Before running the application, ensure that you have the following installed:

- Java Development Kit (JDK) - version 17
- PostgreSQL database
- IDE (Integrated Development Environment) - IntelliJ IDEA, Eclipse, or any other Java IDE of your choice

## Getting Started

1. Clone the repository:

   ```bash
   git clone https://github.com/biancaburghianu/proiectPA.git
2. Open the project in your IDE.

3. Set up the PostgreSQL database:

   - Create a new database named HospitalPlanner.
   - Configure the database connection properties in the application.properties file.

4. Build the project:

    Using an IDE: Run the main class HospitalPlanner.

5. Access the application in your web browser:

    URL: http://localhost:8080/swagger-ui/index.html

## User Actions

### Patient Actions

1. **Register as a Patient**
    - Endpoint: `/hospitalplanner/register/patient`
    - Method: `POST`
    - Description: Register a new patient account by providing the required information.

2. **Authenticate as a Patient**
    - Endpoint: `/hospitalplanner/authenticate/patient`
    - Method: `POST`
    - Description: Authenticate as a registered patient to obtain an authentication token.

3. **View Personal Details**
    - Endpoint: `/hospitalplanner/patients/personalDetails`
    - Method: `GET`
    - Description: Retrieve the personal details of the authenticated patient.

4. **Delete Patient Account**
    - Endpoint: `/hospitalplanner/patients/deleteAccount`
    - Method: `DELETE`
    - Description: Delete the account and associated data of the authenticated patient.

5. **View Available Doctors**
    - Endpoint: `/hospitalplanner/doctors`
    - Method: `GET`
    - Description: Retrieve a list of available doctors.

6. **View Doctor Details**
    - Endpoint: `/hospitalplanner/doctors/{doctorId}`
    - Method: `GET`
    - Description: Retrieve detailed information about a specific doctor.

7. **Schedule an Appointment**
    - Endpoint: `/hospitalplanner/appointments`
    - Method: `POST`
    - Description: Schedule a new appointment with a doctor.

8. **View Favorite Doctor Program**
    - Endpoint: `/hospitalplanner/favoriteDoctorProgram`
    - Method: `GET`
    - Description: Retrieve the program details of the favorite doctor selected by the authenticated patient.

9. **Manage Preferences**
    - Endpoint: `/hospitalplanner/preferences`
    - Method: `GET`, `POST`, `PUT`, `DELETE`
    - Description: View, create, update, or delete preferences for the authenticated patient.


### Doctor Actions

1. **Register as a Doctor**
    - Endpoint: `/hospitalplanner/register/doctor`
    - Method: `POST`
    - Description: Register a new doctor account by providing the required information.

2. **Authenticate as a Doctor**
    - Endpoint: `/hospitalplanner/authenticate/doctor`
    - Method: `POST`
    - Description: Authenticate as a registered doctor to obtain an authentication token.

3. **View Patient Details**
    - Endpoint: `/hospitalplanner/patients/{name}`
    - Method: `GET`
    - Description: Retrieve detailed information about a specific patient.

4. **Delete Doctor Account**
    - Endpoint: `/hospitalplanner/doctors/deleteAccount`
    - Method: `DELETE`
    - Description: Delete the account and associated data of the authenticated doctor.

5. **View Appointment Details**
    - Endpoint: `/hospitalplanner/appointments/{appointmentId}`
    - Method: `GET`
    - Description: Retrieve detailed information about a specific appointment.

