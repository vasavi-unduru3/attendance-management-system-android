package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.model.Student;
import com.example.project.model.Teacher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText email,name, password;
    RadioButton adminRadio, teacherRadio, studentRadio;
    Button loginBtn;

    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference studentRef = database.getReference("student");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //email = findViewById(R.id.email);
        name = findViewById(R.id.username);
        password = findViewById(R.id.password);
        adminRadio = findViewById(R.id.adminRadio);
        teacherRadio = findViewById(R.id.teacherRadio);
        studentRadio = findViewById(R.id.studentRadio);
        loginBtn = findViewById(R.id.loginBtn);

        mAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(v -> {


            String username = name.getText().toString();
            //String userEmail = email.getText().toString();
            String userPass = password.getText().toString();
            String role = getSelectedRole().toString();

//            if (userEmail.isEmpty() || userPass.isEmpty() || role == null) {
//                Toast.makeText(this, "Fill in all fields and select role", Toast.LENGTH_SHORT).show();
//                return;
//            }
            if (role.equals("admin"))
            {
                DatabaseReference adminRef = database.getReference("admin");
                validateRoleLogin(role,username, userPass, adminRef);
            }
            if (role.equals("student"))
            {
                DatabaseReference studentRef = database.getReference("students");
                validateRoleLogin(role,username, userPass,  studentRef);
            }
            else if(role.equals("teacher")) {
                DatabaseReference teacherRef = database.getReference("teachers");
                validateRoleLogin(role, username, userPass, teacherRef);
            }

//            switch (role) {
//                case "admin":
//                    startActivity(new Intent(this, AdminActivity.class));
//                    break;
//                case "teacher":
//                    startActivity(new Intent(this, TeacherActivity.class));
//                    break;
//                case "student":
//                    startActivity(new Intent(this, StudentActivity.class));
//                    break;
//            }


        });
    }

    private String getSelectedRole() {
        if (adminRadio.isChecked()) return "admin";
        if (teacherRadio.isChecked()) return "teacher";
        if (studentRadio.isChecked()) return "student";
        return null;
    }
    // Method for validating a specific role login
    void validateRoleLogin(String role, String inputName, String inputPassword, DatabaseReference reference) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isValid = false;
                String dbName;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    dbName = ds.child("username").getValue(String.class);
                    String dbPassword = ds.child("password").getValue(String.class);

                    if (dbName != null && dbName.equals(inputName) && dbPassword != null && dbPassword.equals(inputPassword)) {
                        isValid = true;
                        if (role.equals("admin"))
                        {
                            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                            intent.putExtra("username", inputName);
                            startActivity(intent);

                        }
                        else if (role.equals("teacher"))
                        {
                            Intent intent = new Intent(LoginActivity.this, TeacherActivity.class);
                            intent.putExtra("username", inputName);
                            startActivity(intent);
                            //finish();
                        }
                        else if (role.equals("student"))
                        {

                            Intent intent = new Intent(LoginActivity.this, StudentActivity.class);
                            intent.putExtra("username", inputName);
                            startActivity(intent);
                            //finish();

                        }


                        break;
                    }
                }



                if (isValid) {
                    Toast.makeText(LoginActivity.this, "Login Successful for " + role, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Credentials for " + role, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to validate login for " + role, error.toException());
            }
        });
    }

}
