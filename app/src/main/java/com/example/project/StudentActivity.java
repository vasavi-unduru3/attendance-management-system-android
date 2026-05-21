package com.example.project;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.model.Student;
import com.google.firebase.database.*;

import java.util.Locale;

public class StudentActivity extends AppCompatActivity {

    TextView attendanceText;
    DatabaseReference dbRef;
    String username;  // Ideally passed via login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        username = getIntent().getStringExtra("username"); // Now it's safe to call
        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Username not passed", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        attendanceText = findViewById(R.id.attendanceText);
        dbRef = FirebaseDatabase.getInstance().getReference();

        // Get username from intent (replace hardcoded value)


        loadAttendance();
    }

    private void loadAttendance() {
        dbRef.child("students").orderByChild("username").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot studentSnap : snapshot.getChildren()) {
                            Long present = studentSnap.child("presentClasses").getValue(Long.class);
                            Long total = studentSnap.child("totalClasses").getValue(Long.class);

                            if (present == null) present = 0L;
                            if (total == null || total == 0L) {
                                attendanceText.setText("No attendance records yet.");
                                return;
                            }

                            double percentage = (present * 100.0) / total;
                            attendanceText.setText(String.format(Locale.getDefault(),
                                    "Present: %d/%d (%.2f%%)", present, total, percentage));
                            ProgressBar attendanceProgress = findViewById(R.id.attendanceProgress);
                            attendanceProgress.setProgress((int) percentage);
                        }


                        //attendanceText.setText("Student not found.");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(StudentActivity.this, "Failed to load attendance.", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}
