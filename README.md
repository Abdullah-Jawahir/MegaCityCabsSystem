# Megacity Cab System

The **Megacity Cab System** is a web application designed to manage a popular cab service in Colombo, Sri Lanka. Built as a pure Java/Maven project, it provides a robust platform for customers to book cabs, manage bookings, and handle payments, while offering administrators tools for driver assignment, billing, and customer support. The application is deployed on Apache Tomcat and uses a CI/CD pipeline with GitHub Actions to automate builds and deploy documentation to GitHub Pages.

## Features
- **User Authentication**: Secure login and registration for customers and admins.
- **Booking Management**: Create, view, and cancel cab bookings with a user-friendly interface.
- **Billing and Payments**: Generate and print bills, process payments with client-side validation.
- **Driver Assignment**: Assign drivers to bookings with nullable driver IDs for flexibility.
- **Email Notifications**: Confirmation emails with tracking buttons for customers.
- **Customer Support**: Dedicated help pages for both customers and admins.
- **Dynamic UI**: Interactive dashboards, searchable content, and responsive layouts.

## Technologies Used
- **Java**: Core programming language for backend logic.
- **Maven**: Build automation and dependency management.
- **Apache Tomcat**: Servlet container for deploying the web application.
- **JSP/HTML/CSS**: Frontend for rendering web pages.
- **MySQL**: Database for storing user, booking, and billing data.
- **GitHub Actions**: CI/CD pipeline for building and deploying documentation.
- **GitHub Pages**: Hosting for project documentation.

## Prerequisites
Before setting up the project, ensure you have the following installed:
- **Java 17**: Required for compiling and running the application.
- **Apache Tomcat**: Version 9 or later for deploying the application.
- **Git**: For cloning the repository.
- **MySQL**: Database server (optional, if you need to set up the database locally).
- **Eclipse/IntelliJ IDEA**: Recommended IDEs for project setup.

## Installation and Local Run Setup

### 1. Clone the Repository
```bash
git clone https://github.com/Abdullah-Jawahir/MegaCityCabsSystem.git
```

### 2. Import the Project into Eclipse or IntelliJ
#### **Eclipse**
1. Open Eclipse and select `File > Import`.
2. Choose `Existing Maven Projects` and click `Next`.
3. Browse to the cloned project folder and select the `megacitycabsystem` directory.
4. Click `Finish` to import the project.
5. Right-click the project and select `Maven > Update Project` to download dependencies.

#### **IntelliJ IDEA**
1. Open IntelliJ IDEA and select `File > Open`.
2. Navigate to the cloned project folder and open the `megacitycabsystem` directory.
3. IntelliJ will detect the Maven project and prompt for auto-import. Click `Yes`.
4. Wait for the dependencies to download.

### 3. Configure Apache Tomcat in Your IDE
#### **Eclipse**
1. Open `Window > Preferences`.
2. Navigate to `Server > Runtime Environments`.
3. Click `Add`, select `Apache Tomcat v9.0` or higher, and specify the installation path.
4. Click `Finish`.
5. Right-click the project and select `Run As > Run on Server`.
6. Select the configured Tomcat server and click `Finish`.

#### **IntelliJ IDEA**
1. Open `File > Settings`.
2. Navigate to `Build, Execution, Deployment > Application Servers`.
3. Click `+`, select `Tomcat Server`, and specify the installation path.
4. Click `OK`.
5. Open `Run > Edit Configurations`.
6. Click `+` and select `Tomcat Server > Local`.
7. Select your project as the deployment artifact.
8. Click `Apply` and `OK`.
9. Click `Run` to start the Tomcat server with the project.

### 4. Set Up Application Properties
If the project requires configuration properties, ensure they are set up correctly:
- Create an `application.properties` file or update `DBConnection.java`.
- Modify database connection details in `src/main/java/com/system/utils/DBConnection.java`:
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/megacity_cabs";
private static final String DB_USER = "your_username";
private static final String DB_PASSWORD = "your_password";
```

### 5. Run the Application
- After configuring Tomcat, start the server from your IDE.
- Open a browser and navigate to:
```bash
http://localhost:8080/megacitycabsystem/
```
- You should see the index page of the Megacity Cab System.

## Email Service Configuration
To enable email notifications (e.g., confirmation emails), configure Gmail credentials in the environment variables.

- Use an App Password generated from a Google account with 2-Factor Authentication (2FA) enabled:
  - Enable 2FA on your Gmail account.
  - Generate an App Password for the application.
  - Set the environment variables:
    ```bash
    EMAIL_FROM=your-email@gmail.com
    EMAIL_USERNAME=your-email@gmail.com
    EMAIL_PASSWORD=your-app-password
    ```
  - Ensure these credentials are used by the application to send emails.

## Documentation
For detailed information on the project’s version control, CI/CD pipeline, and development process, refer to the official documentation hosted on GitHub Pages:

- [Megacity Cab System Documentation](https://github.com/Abdullah-Jawahir/MegaCityCabsSystem/blob/master/megacitycabsystem/docs/github-workflow.md)

The documentation includes:
- Commit history on the `master` branch.
- Branching strategy.
- CI/CD workflow details using GitHub Actions.
- Screenshots of the development process.

## Contributing
We welcome contributions! To contribute:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Make your changes and commit them (`git commit -m "Add your feature"`).
4. Push to your branch (`git push origin feature/your-feature`).
5. Open a Pull Request on GitHub.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact/Support
For questions, bug reports, or feature requests, please contact the project maintainer:

- **Abdullah Jawahir**
- **Email**: [mjabdullah33@gmail.com](mailto:mjabdullah33@gmail.com)
- **GitHub Issues**: [Open an issue](https://github.com/Abdullah-Jawahir/MegaCityCabsSystem/issues)

Thank you for using the Megacity Cab System!
