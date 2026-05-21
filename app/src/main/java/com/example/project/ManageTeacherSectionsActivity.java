package com.example.project;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.model.Student;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class ManageTeacherSectionsActivity extends AppCompatActivity {

    EditText sectionInput;
    Button assignSectionBtn;
    TextView assignedSectionsText;
    String teacherId;
    FirebaseDatabase database;
    DatabaseReference teacherRef;
    List<String> assignedSections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_teacher_sections);

        sectionInput = findViewById(R.id.sectionInput);
        assignSectionBtn = findViewById(R.id.assignSectionBtn);

        teacherId = getIntent().getStringExtra("teacherId");

        if (teacherId == null || teacherId.isEmpty()) {
            Toast.makeText(this, "Teacher ID missing!", Toast.LENGTH_SHORT).show();
            return;  // Prevent further execution
        }

        database = FirebaseDatabase.getInstance();
        teacherRef = database.getReference("teachers").child(teacherId);
        teacherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getValue(Student.class);
                if (student != null) {
                    sectionInput.setText(student.getSection());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ManageTeacherSectionsActivity.this, "Failed to load student data.", Toast.LENGTH_SHORT).show();
            }
        });
        assignSectionBtn.setOnClickListener(v -> {
            String section = sectionInput.getText().toString().trim();
            if (section.isEmpty()) {
                Toast.makeText(this, "Please enter section(s)", Toast.LENGTH_SHORT).show();
            } else {
                teacherRef.child("section").setValue(section)
                        .addOnSuccessListener(aVoid -> {


                            Toast.makeText(this, "Sections assigned successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Disable temporarily if crashing
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });


            }

        });



    }

}
