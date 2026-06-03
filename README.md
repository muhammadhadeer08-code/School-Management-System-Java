🏫 School Management System (Desktop Application)

A modern, high-performance Desktop Application built using **Java Swing** and a **MySQL** backend. This project features an optimized user interface with multi-threaded dynamic updates and background data binding.

## ✨ Key Features
- **Secure Authentication:** Integrated Admin Login/Signup module syncing directly with local/remote databases.
- **Dynamic Sidebar UI:** A modern sidebar navigation with custom hover state animations and layout stability.
- **Live Clock Widget:** Implemented an asynchronous background thread using `javax.swing.Timer` to display a real-time running system clock.
- **Robust CRUD Panels:** Complete management tabs for **Students, Attendance, Teachers, Classes, Fees, and Reports**.
- **Smart Data Mapping:** Solved runtime row synchronization issues by mapping visible sequential IDs against hidden relational Database Primary Keys (`ArrayList` caching).

## 🛠️ Tech Stack & Concepts Used
- **Language:** Java Core (JDK 11+)
- **GUI Framework:** Java Swing & AWT (`CardLayout`, `BoxLayout`, `BorderLayout`)
- **Graphics:** Custom 2D Rendering (`Graphics2D` and Gradient Fills)
- **Database Connection:** JDBC (Java Database Connectivity) with MySQL Connector/J
- **Concurrency:** Basic Multi-threading for UI non-freezing responsiveness
- **Data Structures:** Java `ArrayList` for background tracking
