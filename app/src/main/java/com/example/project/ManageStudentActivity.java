package com.example.project;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.model.Student;
import com.google.firebase.database.*;

public class ManageStudentActivity extends AppCompatActivity {

    EditText usernameInput, passwordInput,section;
    Button saveBtn;
    String studentId;
    FirebaseDatabase database;
    DatabaseReference studentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_student);

        // Get the studentId passed from StudentListActivity
        studentId = getIntent().getStringExtra("studentId");

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        section = findViewById(R.id.section);
        saveBtn = findViewById(R.id.saveBtn);

        database = FirebaseDatabase.getInstance();
        studentRef = database.getReference("students").child(studentId);

        // Fetch current student data to populate the input fields
        studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getValue(Student.class);
                if (student != null) {
                    usernameInput.setText(student.getUsername());
                    passwordInput.setText(student.getPassword());
                    section.setText(student.getSection());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ManageStudentActivity.this, "Failed to load student data.", Toast.LENGTH_SHORT).show();
            }
        });

        saveBtn.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String sec = section.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            } else {
                // Update student data in Firebase
                studentRef.child("username").setValue(username);
                studentRef.child("password").setValue(password);
                studentRef.child("section").setValue(sec);
                Toast.makeText(this, "Student data updated successfully.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
