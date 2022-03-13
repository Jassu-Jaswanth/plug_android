package com.example.plug_android;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends AppCompatActivity {

    // Database link for getinstance https://plug-android-default-rtdb.asia-southeast1.firebasedatabase.app/
    private static final String db_url = "https://plug-android-default-rtdb.asia-southeast1.firebasedatabase.app/";
    ImageButton logout;
    FirebaseAuth fAuth;
    TextView email;
    ImageButton connect_people;
    ImageView profile_pic;
    FirebaseUser user;
    String db_email;

    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        email = findViewById(R.id.p_email);

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut();
                startActivity(new Intent(profile.this,login.class));
                finish();
            }
        });

        dbRef = FirebaseDatabase.getInstance(db_url).getReference();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                db_email = dataSnapshot.child("users").child(user.getUid()).child("email").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        email.setText(db_email);

        profile_pic = findViewById(R.id.profile_pic);


        connect_people = findViewById(R.id.connect_to_people);
        connect_people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(profile.this, people.class));
            }
        });
    }
}