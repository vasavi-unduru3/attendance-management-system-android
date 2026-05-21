package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ManageTeacherStudentActivity extends AppCompatActivity {

    Button addBtn, manageBtn;
    String type;  // teacher or student
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_teacher_student);

        // Get the type (teacher/student)
        type = getIntent().getStringExtra("type");
        text = findViewById(R.id.text);
        addBtn = findViewById(R.id.addBtn);
        manageBtn = findViewById(R.id.manageBtn);
        if(type.equals("student"))
        {
            text.setText("Manage Students data");
            addBtn.setText("Add Students");
            manageBtn.setText("View Students");
        }
        // Add button click
        addBtn.setOnClickListener(v -> {
            // If it's a teacher, open AddTeacherActivity, if it's a student, open AddStudentActivity
            if (type.equals("teacher")) {
                startActivity(new Intent(this, AddUserActivity.class).putExtra("type", "teacher"));
            } else {
                startActivity(new Intent(this, AddUserActivity.class).putExtra("type", "student"));
            }
        });

        // Manage button click
        manageBtn.setOnClickListener(v -> {
            // If it's a teacher, open TeacherListActivity, if it's a student, open StudentListActivity
            if (type.equals("teacher")) {
                startActivity(new Intent(this, TeacherListActivity.class));
            } else {
                startActivity(new Intent(this, StudentListActivity.class));
            }
        });
    }
}
