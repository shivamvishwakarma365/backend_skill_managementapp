# Employee Management System Backend

## Overview

This repository contains the backend implementation of an Employee Management System built using Spring Boot. The system provides RESTful APIs for managing employee data, including registration, login, updating employee details, and retrieving employee information.

## Table of Contents

1. [Employee Controller](#employee-controller)
2. [Employee Service](#employee-service)
3. [Skill Controller](#skill-controller)
4. [Skill Service](#skill-service)
5. [Getting Started](#getting-started)

## Employee Controller

The `EmployeeController` class defines RESTful APIs for managing employee-related operations. It includes the following endpoints:

- `GET /employees/all`: Retrieve details of all employees.
- `GET /employees/under-manager/{managerEmail}`: Retrieve employees under a specified manager.
- `GET /employees/{id}`: Retrieve details of a specific employee by ID.
- `POST /employees/register`: Register a new employee.
- `PUT /employees/{id}`: Update details of a specific employee by ID.
- `DELETE /employees/{id}`: Delete a specific employee by ID.
- `POST /employees/sorted-by-skills`: Retrieve employees sorted by specified skills.
- `POST /employees/login`: Authenticate an employee based on email and password.

## Employee Service

The `EmployeeService` class provides business logic for employee-related operations. It includes methods for:

- Retrieving all employees.
- Retrieving details of a specific employee by ID.
- Retrieving employees under a specified manager.
- Adding a new employee.
- Updating employee details.
- Deleting an employee.
- Authenticating an employee based on email and password.

## Skill Controller

The `SkillController` class defines RESTful APIs for managing skill-related operations. It includes the following endpoints:

- `POST /api/skills`: Save a new skill.
- `GET /api/skills`: Retrieve details of all skills.
- `GET /api/skills/{id}`: Retrieve details of a specific skill by ID.
- `PUT /api/skills/{skillId}/rating`: Update the rating of a specific skill.
- `PUT /api/skills/update`: Update details of multiple skills.
- `DELETE /api/skills/{id}`: Delete a specific skill by ID.

## Skill Service

The `SkillService` class provides business logic for skill-related operations. It includes methods for:

- Saving a new skill.
- Retrieving details of all skills.
- Retrieving details of a specific skill by ID.
- Updating the rating of a specific skill.
- Updating details of multiple skills.
- Deleting a specific skill by ID.

## Getting Started

1. Clone this repository.
2. Import the project into your preferred Java IDE.
3. Configure the application properties in `application.properties` with your database settings.
4. Run the Spring Boot application.

Note: Ensure that you have a compatible database (e.g., MySQL, PostgreSQL) and update the application properties accordingly.

Feel free to explore and customize the code based on your project requirements. Happy coding!
