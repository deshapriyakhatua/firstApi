package com.example.firstapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button = findViewById(R.id.button);

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                upLoadToDatabase();
//            }
//        });


    }

    private void upLoadToDatabase() {
        // accessing from xml
        EditText editTextUserName = findViewById(R.id.editTextUserName);
        EditText editTextName = findViewById(R.id.editTextPersonName);
        EditText editTextMobile = findViewById(R.id.editTextPhone);
        EditText editTextPin = findViewById(R.id.editTextPin);

        // getting values from xml
        String userName = editTextUserName.getText().toString();
        String name = editTextName.getText().toString();
        String mobile = editTextMobile.getText().toString();
        String pin = editTextPin.getText().toString();

        // creating Object from xml data
        User user = new User(name,mobile,pin);

        // uploading to firebase realTime Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("users-data");
        reference.child(userName).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this,"Successfuly Uploaded",Toast.LENGTH_LONG).show();
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(MainActivity.this,"Upload failed",Toast.LENGTH_LONG).show();
            }
        });

    }

    public class User{
        String name;
        String mobile;
        String pin;

        public User(String name, String mobile, String pin) {
            this.name = name;
            this.mobile = mobile;
            this.pin = pin;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPin() {
            return pin;
        }

        public void setPin(String pin) {
            this.pin = pin;
        }
    }
}