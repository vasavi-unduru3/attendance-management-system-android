package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.model.Student;
import com.example.project.model.Teacher;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddUserActivity extends AppCompatActivity {

    EditText usernameInput, passwordInput, sectionInput;
    Button saveBtn;
    String type;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // Get the type (teacher or student)
        type = getIntent().getStringExtra("type");

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        sectionInput = findViewById(R.id.sectionInput);
        saveBtn = findViewById(R.id.saveBtn);
//        if (type.equals("student"))
//        {
//            sectionInput.setVisibility(View.GONE);
//        }
        saveBtn.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            String section = sectionInput.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username and password required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (type.equals("teacher") && section.isEmpty()) {
                Toast.makeText(this, "Section required for teachers", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new teacher or student
            String userId = database.push().getKey();
            if (type.equals("teacher")) {
               Teacher teacher = new Teacher(username, password, section);
                database.child("teachers").child(userId).setValue(teacher);
            } else {
                Student student = new Student(username, password,section);
                database.child("students").child(userId).setValue(student);
            }

            Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
