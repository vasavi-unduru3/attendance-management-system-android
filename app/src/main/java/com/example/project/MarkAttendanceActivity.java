package com.example.project;


import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MarkAttendanceActivity extends AppCompatActivity {

    TextView attendanceSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        attendanceSummary = findViewById(R.id.attendanceSummary);
        ArrayList<String> list = getIntent().getStringArrayListExtra("presentList");

        if (list != null && !list.isEmpty()) {
            StringBuilder result = new StringBuilder("Present Students:\n\n");
            for (String name : list) {
                result.append("- ").append(name).append("\n");
            }
            attendanceSummary.setText(result.toString());
        } else {
            attendanceSummary.setText("No students marked present.");
        }
    }
}
