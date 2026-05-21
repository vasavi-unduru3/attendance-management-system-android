package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    Button manageTeachersBtn, manageStudentsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        manageTeachersBtn = findViewById(R.id.manageTeachersBtn);
        manageStudentsBtn = findViewById(R.id.manageStudentsBtn);

        // On click, navigate to manage teachers or students
        manageTeachersBtn.setOnClickListener(v -> {
            // Go to Manage Teacher screen
            startActivity(new Intent(this, ManageTeacherStudentActivity.class).putExtra("type", "teacher"));
        });

        manageStudentsBtn.setOnClickListener(v -> {
            // Go to Manage Student screen
            startActivity(new Intent(this, ManageTeacherStudentActivity.class).putExtra("type", "student"));
        });
    }
}
