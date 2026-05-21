package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.model.Teacher;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class TeacherListActivity extends AppCompatActivity {

    ListView teacherListView;
    ArrayList<String> teacherNames;
    ArrayList<String> teacherIds;
    FirebaseDatabase database;
    DatabaseReference teachersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);

        teacherListView = findViewById(R.id.teacherListView);
        teacherNames = new ArrayList<>();
        teacherIds = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        teachersRef = database.getReference("teachers");

        // Load teacher data from Firebase
        teachersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                teacherNames.clear();
                teacherIds.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Teacher teacher = snapshot.getValue(Teacher.class);
                    if (teacher != null) {
                        teacherNames.add(teacher.getUsername());
                        teacherIds.add(snapshot.getKey()); // Store the teacher ID (Firebase key)
                    }
                }

                // Update the ListView with teacher names
                ArrayAdapter<String> adapter = new ArrayAdapter<>(TeacherListActivity.this,
                        android.R.layout.simple_list_item_1, teacherNames);
                teacherListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(TeacherListActivity.this, "Failed to load teacher data.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle teacher item click to manage sections
        teacherListView.setOnItemClickListener((parent, view, position, id) -> {
            String teacherId = teacherIds.get(position);
            // Pass the selected teacherId to the next activity (Manage Sections)
            Intent intent = new Intent(TeacherListActivity.this, ManageTeacherSectionsActivity.class);
            intent.putExtra("teacherId", teacherId);
            startActivity(intent);
        });
    }
}
