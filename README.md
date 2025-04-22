#  LazyWallet – Smart Personal Budget

**LazyWallet** is a web application for managing personal finances. It helps users track income and expenses, categorize them, and visualize financial statistics with interactive charts. This project was developed as a final assignment during a Java programming course.

##  Features

- ✅ User registration and login
- ✅ Add income or expenses using a calculator-style interface
- ✅ Select a category for each transaction
- ✅ Add custom categories
- ✅ Store all transactions in the database
- ✅ Visualize expenses by category using a chart
- ✅ Load and display real-time data from the database
- ✅ (Planned) CSV import and reporting

##  Technologies

- **Backend:**
  - Java 23
  - Spring Boot
  - Spring Security (authentication)
  - Hibernate / Spring Data JPA
  - H2 database (embedded)

- **Frontend:**
  - HTML + CSS + JavaScript (Vanilla JS)
  - Thymeleaf template engine
  - Chart.js (for charts and graphs)

- **Build:**
  - Maven

- **Version Control:**
  - Multiple branches for different logic implementations (e.g., `SimpleLogic`, `BusinessLogic`, `temp-branch`)
  - Collaboration through GitHub

##  User Interface

The application is a single-page layout:

- Top-left: expense pie chart
- Right side: calculator for entering new transactions
- Bottom: action buttons (import, report, logout)
- Modal window for adding a new category

##  Security

- Basic Spring Security configuration
- Passwords are hashed during registration
- Session-based authentication

## 🛠 How to Run

1. Clone the repository: git clone https://github.com/your-profile/LazyWalletSpring.git
2. Open the project in your IDE (IntelliJ IDEA, Eclipse)
3. Make sure you have JDK 17+ installed
4. Run the LazyWalletApplication.java class
5. Visit: http://localhost:8080

 Status
The project is under development. Planned features include:
- Generating reports
- CSV import support
- Financial goals tracking
- Enhanced UI

Authors
Created by Kateryna Lukianenko & Andrey ? as part of a Java course.

🎓 "LazyWallet – a wallet that does everything for you!"




