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
- **MySQL**: Database for storing user, booking, and billing data (assumed based on DB-related commits).
- **GitHub Actions**: CI/CD pipeline for building and deploying documentation.
- **GitHub Pages**: Hosting for project documentation.

## Prerequisites
Before setting up the project, ensure you have the following installed:
- **Java 17**: Required for compiling and running the application.
- **Maven**: For building the project (install via `apt`, `brew`, or download from [Maven](https://maven.apache.org/download.cgi)).
- **Apache Tomcat**: Version 9 or later for deploying the WAR file (download from [Tomcat](https://tomcat.apache.org/download-90.cgi)).
- **Git**: For cloning the repository.
- **MySQL**: Database server (optional, if you need to set up the database locally).

## Installation and Setup Instructions
Follow these steps to set up and run the Megacity Cab System locally:

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Abdullah-Jawahir/MegaCityCabsSystem.git
   ```

2. **Navigate to the Project Directory**:
   ```bash
   cd MegaCityCabsSystem/megacitycabsystem
   ```

3. **Build the Project Using Maven**:
   ```bash
   mvn clean package
   ```
   This generates a WAR file in `target/megacitycabsystem.war`.

4. **Deploy to Apache Tomcat**:
   - Copy the WAR file to the Tomcat `webapps` directory:
     ```bash
     cp target/megacitycabsystem.war /path/to/tomcat/webapps/
     ```
   - Start Tomcat:
     ```bash
     /path/to/tomcat/bin/startup.sh  # Unix/Mac
     /path/to/tomcat/bin/startup.bat  # Windows
     ```
   - Tomcat will automatically deploy the application.

5. **Access the Application**:
   - Open a browser and navigate to:
     ```
     http://localhost:8080/megacitycabsystem/
     ```
   - You should see the index page of the Megacity Cab System.

6. **(Optional) Set Up the Database**:
   - If the project uses a MySQL database, set up the database:
     - Create a MySQL database (e.g., `megacity_cabs`).
     - Update the database connection details in the `com.system.utils.DBConnection` class:
       - Open the file `src/main/java/com/system/utils/DBConnection.java` in your IDE.
       - Modify the following fields with your MySQL database credentials:
         ```
         private static final String DB_URL = "jdbc:mysql://localhost:3306/megacity_cabs";
         private static final String DB_USER = "your_username";
         private static final String DB_PASSWORD = "your_password";
         ```
     - Run any SQL scripts (if provided) to set up the schema and initial data.

## Usage
- **For Customers**:
  - Register or log in to your account.
  - Book a cab by selecting your pickup and drop-off locations.
  - View and manage your bookings on the "My Bookings" page.
  - Receive email confirmations with tracking options (requires email service setup).
- **For Admins**:
  - Log in to the admin dashboard.
  - Assign drivers to bookings.
  - Generate and print bills.
  - Access the help page for customer support tools.
- **Explore Features**:
  - Use the search bar for quick navigation (dynamically visible on certain pages).
  - Check the customer support page for assistance.
- **Email Service Configuration**:
  - To enable email notifications (e.g., confirmation emails), configure Gmail credentials in the environment variables.
  - Use an App Password generated from a Google account with 2-Factor Authentication (2FA) enabled:
    - Enable 2FA on your Gmail account (via Google Account Settings > Security).
    - Generate an App Password (under Security > App Passwords) for the application.
    - Set the environment variables (e.g., in Tomcat’s `setenv.sh` or system environment):
      ```
      EMAIL_FROM=your-email@gmail.com
      EMAIL_USERNAME=your-email@gmail.com
      EMAIL_PASSWORD=your-app-password
      ```
  - Ensure these credentials are used by the application to send emails (configured internally).

## Documentation
For detailed information on the project’s version control, CI/CD pipeline, and development process, refer to the official documentation hosted on GitHub Pages:

- [Megacity Cab System Documentation](https://github.com/Abdullah-Jawahir/MegaCityCabsSystem/blob/master/megacitycabsystem/docs/github-workflow.md)

The documentation includes:
- Commit history on the `master` branch.
- Branching strategy (`feature/docs` branch for documentation).
- CI/CD workflow details using GitHub Actions.
- Screenshots of the development process.

## Contributing
We welcome contributions to the Megacity Cab System! To contribute:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Make your changes and commit them (`git commit -m "Add your feature"`).
4. Push to your branch (`git push origin feature/your-feature`).
5. Open a Pull Request on GitHub with a detailed description of your changes.

Please ensure your code follows the project’s coding standards and includes appropriate tests (if applicable).

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact/Support
For questions, bug reports, or feature requests, please contact the project maintainer:

- **Abdullah Jawahir**
- **Email**: [mjabdullah33@gmail.com](mailto:mjabdullah33@gmail.com)
- **GitHub Issues**: [Open an issue](https://github.com/Abdullah-Jawahir/MegaCityCabsSystem/issues)

Thank you for using the Megacity Cab System!
