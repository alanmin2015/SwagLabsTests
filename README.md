🚀 Selenium Java Automation Demo (Sauce Demo)
This repository contains a professional-grade QA Automation framework designed for Sauce Demo. It implements advanced design patterns to ensure scalability, maintainability, and high-speed execution.

🛠 Tech Stack
Language: Java 21

Testing Framework: TestNG

Automation Tool: Selenium

Build Tool: Maven

Design Pattern: Page Object Model (POM)

CI/CD: GitHub Actions

🏗 Framework Architecture
This project follows a Level 3 (Advanced) framework design:

Page Object Model (POM): UI elements and actions are decoupled from test scripts for better maintenance.

BaseTest Logic: Centralized driver initialization and teardown using TestNG @BeforeMethod and @AfterMethod.

Parallel Execution: Configured via testng.xml using ThreadLocal<WebDriver> to ensure thread-safe browser instances.

Automated Listeners: A custom TestListener captures timestamped screenshots automatically upon test failure.

Data-Driven Testing: Uses @DataProvider to validate multiple user personas (Standard, Problem, Locked, etc.) in a single script.

🚦 Getting Started
Prerequisites
JDK 21 or higher

Maven installed

Google Chrome

Running Tests Locally
You can run the entire suite using Maven from your terminal:

Bash
mvn clean test
Alternatively, right-click on testng.xml in your IDE and select Run.

CI/CD Integration
This project is integrated with GitHub Actions. Every push to the master branch triggers the full suite in headless mode.

Failures: If a test fails in the cloud, the screenshots are uploaded as build artifacts for debugging.
Notice: this test is set up to fail in purpose to demonstrate the screenshot feature.

🧪 Test Coverage
Authentication: Validates all user types and verifies error handling for locked-out accounts.

Product Sorting: Verifies that price sorting (Low to High) works mathematically across the inventory.

E2E Checkout: Simulates a complete user journey from adding items to the cart to the final order confirmation.

Visual/Functional Validation: Specifically monitors the problem_user and visual_user for broken images and layout shifts.
