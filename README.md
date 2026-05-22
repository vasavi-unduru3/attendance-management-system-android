Attendance Management System Using Android Studio

An Android-based Attendance Management System developed using Android Studio, Java, and Firebase to automate attendance tracking for educational institutions. The application provides separate modules for Admin, Teacher, and Student with real-time attendance management and monitoring.

**Features**
Admin Module
Admin Login Authentication
Add Teachers and Students
View Teacher List
View Student List
Assign Sections to Teachers
Manage Student Details
Teacher Module
Teacher Login
View Assigned Sections
Load Students by Section
Mark Attendance
Submit Attendance Records
Student Module
Student Login
View Attendance Percentage
Track Present and Total Classes
**Technologies Used**
Frontend: Android Studio, XML
Programming Language: Java
Backend: Firebase Realtime Database
Authentication: Firebase Authentication
Database: Firebase Firestore / Realtime Database
UI Components: Material Design Components
**Project Screens**
Login Page
Admin Dashboard
Manage Teachers
Manage Students
Teacher Attendance Panel
Student Attendance View
**Project Structure**
project/
│
├── activities/
│   ├── LoginActivity.java
│   ├── AdminActivity.java
│   ├── TeacherActivity.java
│   ├── StudentActivity.java
│   ├── AddUserActivity.java
│   ├── ManageStudentActivity.java
│   ├── ManageTeacherSectionsActivity.java
│   └── TeacherListActivity.java
│
├── layouts/
│   ├── activity_login.xml
│   ├── activity_admin.xml
│   ├── activity_teacher.xml
│   ├── activity_student.xml
│   └── more XML layout files
│
├── model/
│   ├── Student.java
│   └── Teacher.java
│
└── Firebase Configuration
**Setup Instructions**
**1️.Clone the Repository**
git clone https://github.com/your-username/attendance-management-system.git
**2️.Open in Android Studio**
Open Android Studio
Select Open Existing Project
Choose the project folder
**3️.Connect Firebase**
Open Firebase Console
Create a Firebase Project
Add Android App
Download google-services.json
Paste it inside:
app/
**4️.Add Dependencies**
In build.gradle(:app):
implementation 'com.google.firebase:firebase-database:21.0.0'
implementation 'com.google.firebase:firebase-auth'
implementation 'com.google.android.material:material:1.11.0'
**5️.Sync Gradle**
Click:
Sync Now
**6️.Run the Application**
Connect Android Device / Emulator
Click ▶️ Run
**User Roles**
Role	Functionality
Admin	Manage teachers and students
Teacher	Mark and submit attendance
Student	View attendance details
**Attendance Calculation**
Attendance percentage is calculated using:
Attendance % = (Present Classes / Total Classes) × 100
**Firebase Database Structure**
Firebase
│
├── admin
├── teachers
├── students
└── attendance
**Future Enhancements**
QR Code Attendance
Face Recognition Attendance
Email Notifications
Attendance Reports PDF
Cloud Firestore Integration
Dark Mode Support
**Learning Outcomes**
Android App Development
Firebase Integration
Realtime Database Handling
UI/UX Design in Android
Authentication and Role Management
**Developed By**
Vasavi
**License**
This project is developed for educational purposes.
