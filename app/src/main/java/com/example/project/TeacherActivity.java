package com.example.project;

import android.os.Bundle;

import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.model.Teacher;
import com.google.firebase.database.*;
import com.google.firebase.database.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.*;

public class TeacherActivity extends AppCompatActivity {
    EditText dateEditText, dayEditText;
    Spinner sectionSpinner;
    Button loadBtn, submitBtn;
    ListView studentListView;
    ArrayAdapter<String> studentAdapter;
    List<String> studentList = new ArrayList<>();
    Map<String, Boolean> attendanceMap = new HashMap<>();

    String teacherUsername; // Assume you pass this from login
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        teacherUsername = getIntent().getStringExtra("username");
        if (teacherUsername == null || teacherUsername.isEmpty()) {
            Toast.makeText(this, "Teacher username missing!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        dateEditText = findViewById(R.id.dateEditText);
        dayEditText = findViewById(R.id.dayEditText);
        fillCurrentDateAndDay();

        sectionSpinner = findViewById(R.id.sectionSpinner);
        loadBtn = findViewById(R.id.loadStudentsBtn);
        submitBtn = findViewById(R.id.submitAttendanceBtn);
        studentListView = findViewById(R.id.studentListView);

        dbRef = FirebaseDatabase.getInstance().getReference();

        // Get teacher username from login (you should pass it via Intent ideally)
        //teacherUsername = getIntent().getStringExtra("username");

        loadSections(teacherUsername);

        loadBtn.setOnClickListener(v -> {
            String section = sectionSpinner.getSelectedItem().toString();
            if (!section.isEmpty()) {
                loadStudents(section);
            }
        });

        submitBtn.setOnClickListener(v -> {
            String section = sectionSpinner.getSelectedItem().toString();
            if (!section.isEmpty()) {
                submitAttendance(section);
                finish();
            }
        });

    }
    private void fillCurrentDateAndDay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        String currentDate = dateFormat.format(new Date());
        String currentDay = dayFormat.format(new Date());

        dateEditText.setText(currentDate);
        dayEditText.setText(currentDay);
    }

    private void loadSections(String teacherUsername) {
        dbRef.child("teachers")
                .orderByChild("username").equalTo(teacherUsername)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            String sectionsStr = snap.child("section").getValue(String.class);
                            String[] sections = sectionsStr.split(",");

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(TeacherActivity.this,
                                    android.R.layout.simple_spinner_item, sections);
                            sectionSpinner.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(TeacherActivity.this, "Failed to load sections", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadStudents(String section) {
        dbRef.child("students")
                .orderByChild("section").equalTo(section)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        studentList.clear();
                        attendanceMap.clear();

                        for (DataSnapshot snap : snapshot.getChildren()) {
                            String username = snap.child("username").getValue(String.class);
                            studentList.add(username);
                            attendanceMap.put(username, false); // default: absent
                        }

                        studentAdapter = new ArrayAdapter<String>(TeacherActivity.this,
                                android.R.layout.simple_list_item_multiple_choice, studentList);

                        studentListView.setAdapter(studentAdapter);
                        studentListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                        // Track checkbox states
                        studentListView.setOnItemClickListener((parent, view, position, id) -> {
                            String name = studentList.get(position);
                            boolean checked = studentListView.isItemChecked(position);
                            attendanceMap.put(name, checked);
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(TeacherActivity.this, "Failed to load students", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void submitAttendance(String section) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        dbRef.child("students").orderByChild("section").equalTo(section)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            String username = snap.child("username").getValue(String.class);
                            String studentId = snap.getKey();

                            if (username != null && studentId != null) {
                                boolean present = attendanceMap.getOrDefault(username, false);

                                DatabaseReference studentRef = dbRef.child("students").child(studentId);

                                // Increment totalClasses
                                studentRef.child("totalClasses").runTransaction(new Transaction.Handler() {
                                    @NonNull
                                    @Override
                                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                        Integer total = currentData.getValue(Integer.class);
                                        currentData.setValue((total == null ? 0 : total) + 1);
                                        return Transaction.success(currentData);
                                    }

                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) { }
                                });

                                // If present, increment presentClasses
                                if (present) {
                                    studentRef.child("presentClasses").runTransaction(new Transaction.Handler() {
                                        @NonNull
                                        @Override
                                        public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                            Integer presentVal = currentData.getValue(Integer.class);
                                            currentData.setValue((presentVal == null ? 0 : presentVal) + 1);
                                            return Transaction.success(currentData);
                                        }

                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) { }
                                    });
                                }
                            }
                        }

                        Toast.makeText(TeacherActivity.this, "Attendance Submitted & Stats Updated", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(TeacherActivity.this, "Attendance update failed.", Toast.LENGTH_SHORT).show();
                    }
                });

        // Also save attendance snapshot for records
        dbRef.child("attendance").child(section).child(date)
                .setValue(attendanceMap);
    }

}

