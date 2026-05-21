package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.model.Student;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class StudentListActivity extends AppCompatActivity {

    ListView studentListView;
    ArrayList<String> studentNames;
    ArrayList<String> studentIds;
    FirebaseDatabase database;
    DatabaseReference studentsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        studentListView = findViewById(R.id.studentListView);
        studentNames = new ArrayList<>();
        studentIds = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        studentsRef = database.getReference("students");

        // Load student data from Firebase
        studentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                studentNames.clear();
                studentIds.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Student student = snapshot.getValue(Student.class);
                    if (student != null) {
                        studentNames.add(student.getUsername());  // or you can add student name if you have it
                        studentIds.add(snapshot.getKey()); // Store the student ID (Firebase key)
                    }
                }

                // Update the ListView with student names
                ArrayAdapter<String> adapter = new ArrayAdapter<>(StudentListActivity.this,
                        android.R.layout.simple_list_item_1, studentNames);
                studentListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(StudentListActivity.this, "Failed to load student data.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle student item click to view their details (or manage attendance/sections)
        studentListView.setOnItemClickListener((parent, view, position, id) -> {
            String studentId = studentIds.get(position);
            // Pass the selected studentId to the next activity (Manage Student Attendance or Profile)
            Intent intent = new Intent(StudentListActivity.this, ManageStudentActivity.class);
            intent.putExtra("studentId", studentId);
            startActivity(intent);
        });
    }
}
