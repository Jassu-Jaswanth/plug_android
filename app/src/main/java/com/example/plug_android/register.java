package com.example.plug_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {

    EditText email;
    EditText passwd;
    EditText name;
    EditText phone;
    Button register;

    TextView taketologin;

    FirebaseAuth fAuth;
    FirebaseDatabase mDb;
    DatabaseReference mref;
    private static final String db_url = "https://plug-android-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.r_email);
        passwd = findViewById(R.id.r_password);
        name = findViewById(R.id.full_name);
        phone = findViewById(R.id.phone);

        mDb = FirebaseDatabase.getInstance(db_url);
        mref = mDb.getReference();

        register = findViewById(R.id.register_btn);
        fAuth = FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adduser(email.getText().toString(), passwd.getText().toString(),name.getText().toString(),phone.getText().toString(), mref);
            }
        });

        taketologin = findViewById(R.id.taketologin);
        taketologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(register.this,login.class));
            }
        });
    }

    private void adduser(String email, String password, String name, String phone, DatabaseReference dbRef) {
        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = fAuth.getCurrentUser();
                            UpdateDb(user, name, phone, dbRef);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(register.this, "Something gone wrong. Please try agin",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    private void UpdateDb(FirebaseUser user, String name, String phone, DatabaseReference dbRef) {
        User user1 = new User();
        user1.setDisplayName(name);
        user1.setEmail(user.getEmail().toString());
        user1.setPhone(phone);
        user1.setPhotoURL(user.getPhotoUrl().toString());
        dbRef.child("users").child(user.getUid()).setValue(user1);
    }
}
