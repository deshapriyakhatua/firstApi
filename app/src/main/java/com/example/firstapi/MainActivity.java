package com.example.firstapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // upload button
        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upLoadToDatabase();
                Toast.makeText(MainActivity.this,"Upload Button pressed",Toast.LENGTH_SHORT).show();
            }
        });

        // download button
        Button button1 = findViewById(R.id.button2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downLoadToDatabase();
                Toast.makeText(MainActivity.this,"Download Button pressed",Toast.LENGTH_SHORT).show();
            }
        });

    }

    // upload data 
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
        reference.child(userName).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MainActivity.this,"Successfully Uploaded",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Upload failed : "+e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });

    }

    // download data
    private  void downLoadToDatabase(){

        TextView textView = findViewById(R.id.textView);
        EditText editTextUserName = findViewById(R.id.editTextUserNameDownload);
        String userName = editTextUserName.getText().toString();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("users-data").child(userName).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().getValue()!=null){

                        Toast.makeText(MainActivity.this,"Data downloaded : " + task.getResult().child("name").getValue(),Toast.LENGTH_LONG).show();

                        textView.setText("Name : " + task.getResult().child("name").getValue() + "\n" + "Mobile : " + task.getResult().child("mobile").getValue() + "\n" + "Pin : " + task.getResult().child("pin").getValue());
                    }
                    else{
                        Toast.makeText(MainActivity.this,"User not available !",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this,"Download failed : "+task.getException(),Toast.LENGTH_LONG).show();
                }
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(MainActivity.this,"Download failed",Toast.LENGTH_LONG).show();
            }
        });
    }

    // create User class
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
