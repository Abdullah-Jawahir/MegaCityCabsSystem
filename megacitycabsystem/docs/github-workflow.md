# GitHub Workflow Documentation for Megacity Cab System

## Introduction
This document outlines the version control and deployment process for the Megacity Cab System, a pure Java/Maven project designed to manage cab services in Colombo. The project was developed on the `master` branch, with a CI/CD pipeline implemented to automate building, testing, and deploying documentation.

## Version Control Details
### Commits on Master Branch
All development occurred on the `master` branch, with commits reflecting progress over several days:
- February 23, 2025  
    - "Implemented the initial version of the user interface for crud" (Commit: bb2e95a).  
    - "Implemented the main crud functionalities of the system" (Commit: 3ee3702).  
    - "Implemented the main models" (Commit: ad73e6e).  
    - "Implemented the common user model and its classes" (Commit: 68ebec7).  
    - "Tested the connection" (Commit: 872285c).  
    - "Implemented the connection" (Commit: 096ded9).  
    - "Added some necessary images for the landing page" (Commit: d9fbbb8).  
    - "Initialized the mega city cab system" (Commit: 9263123).  
- February 24, 2025  
  - "Adjusted the star styles" (Commit: 96ecbfd).  
  - "Maintained the main dashboard theme for the edit forms" (Commit: 9997b8b).  
  - "Set the admin name dynamically" (Commit: 462468c).  
  - "Added the proper route for the register page" (Commit: 0f535c9).  
  - "Enhanced the styling and layouts of the system" (Commit: cae7224).  
- February 27, 2025  
  - "Enhanced the user interface and functionalities" (Commit: 2233b06).  
- February 28, 2025  
  - "Cleared the log statement" (Commit: 30859de).  
  - "Created a my booking page to view and manage the bookings by customer" (Commit: 0b89bfc).  
  - "Enhanced the customer interface with more interactivity" (Commit: eefd040).  
- March 5, 2025  
  - "Implemented the print bill functionality" (Commit: 7d8e674).  
  - "Implemented the driver assignment functionality" (Commit: befe13a).  
  - "Allowed the driver id to be nullable" (Commit: ebdcfa4).  
- March 6, 2025  
  - "Implemented an email confirmation service for customers" (Commit: b6983e3).  
  - "Adjusted the layout and styles of the recent booking section" (Commit: 94bc5bd).  
  - "Fixed the time period selection process" (Commit: ad1df89).  
  - "Fixed the active indicator for help nav" (Commit: 162f72a).  
  - "Enhanced the customer support page" (Commit: f15341d).  
  - "Made the search bar to be dynamically visible or certain pages" (Commit: dc1823e).  
  - "Implemented the help page for the Admin" (Commit: 8d0ae6d).  
  - "Updated the customer profile with header and footer sections" (Commit: 0436bdb).  
  - "Implemented the customer profile page" (Commit: 4406fdba).  
- March 7, 2025  
  - "Implemented a client side validation for the card details" (Commit: 2d72365).  
  - "Enhanced the billing process and its UI" (Commit: 0d7f9b8).  
  - "Implemented a trigger in DB for status updates after bill processing" (Commit: dbce324).  
- March 9, 2025  
  - "Enhanced the email template and added a tracking button" (Commit: 8424ce8).  
- March 10, 2025  
  - "Cleaned the code" (Commit: 72ae83e).  
  - "Implemented test cases for main components of the system" (Commit: 572d0412).  
- March 11, 2025  
  - "Implemented DB functions to replace the direct DAO queries" (Commit: e6795bc).  
  - "Implemented a stored procedure for create booking" (Commit: 4736731).  
  - "Enhanced the cancel booking feature" (Commit: 178a23b).  
  - "Fixed the vehicle status update issue while booking" (Commit: cc8998).  
  - "Fixed the header search functionality" (Commit: 66ebff3).  
  - "Enhanced the email process" (Commit: 82f4697).  
  - "Enhanced the email service with observer pattern" (Commit: 0934af1).  
- March 12, 2025  
  - "Cleaned the unnecessary comments" (Commit: 8cc9351).  
  - "Cleaned the codes" (Commit: 746a907).  
  - "Added the rate per km for vehicle modal" (Commit: 3bcb3b2).  
  - "Cleaned the codes" (Commit: 9e4d445).  
  - "Commented the report nav" (Commit: 9413938).  
  - "Adjusted the growth percentage decimal point" (Commit: f52482f).  
  - "Adjusted a customer validation part" (Commit: 619c185).  
  - "Implemented client side validation for registration details" (Commit: a82c078).  
  - "Updated the comment" (Commit: ed7b317).

### Branching Strategy
- A `feature/docs` branch was created from `master` to add this documentation (`git checkout -b feature/docs`).
- Changes were committed (`git commit -m "Added GitHub workflow documentation"`) and merged into `master` (`git checkout master; git merge feature/docs`).
- The `master` branch was merged into `main` to align branches (`git checkout main; git merge master`).

## CI/CD Workflow
A CI/CD pipeline was implemented using GitHub Actions to automate the build and deployment process:
- **Trigger**: The pipeline runs on pushes to `master` or `main`.
- **Steps**:
  1. Compile the project: `mvn clean compile`
  2. Run tests: `mvn test`
  3. Deploy documentation to GitHub Pages from the `docs/` folder.
- **Purpose**: Ensures code quality through compilation and testing, and makes documentation accessible via GitHub Pages.

The workflow configuration is stored in `.github/workflows/ci-cd.yml`.

## Deployed Documentation
The latest documentation (v1.0.0) is deployed to GitHub Pages at: https://github.com/Abdullah-Jawahir/MegaCityCabsSystem.git.  
This version includes features like authentication, booking, billing, and car/driver management.