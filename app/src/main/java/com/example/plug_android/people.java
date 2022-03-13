package com.example.plug_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class people extends AppCompatActivity {
    private static final String db_url = "https://plug-android-default-rtdb.asia-southeast1.firebasedatabase.app/";
    FirebaseDatabase db;
    DatabaseReference  fRef;
    RecyclerView people_list;
    people_list_Adapter adapter;
    User[] users;   //Get from database
    ArrayList<User> user_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        users = new User[10];
        user_data = new ArrayList<User>(users.length);

        db = FirebaseDatabase.getInstance(db_url);
        fRef = db.getReference();

        fRef.child("users").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            user_data.add(childSnapshot.getValue(User.class));
                        }
                    }
                    // onCancelled()...
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                }
        );
        users = user_data.toArray(users);
        people_list = findViewById(R.id.people);
        adapter = new people_list_Adapter(users);
        people_list.setLayoutManager(new LinearLayoutManager(this));
        people_list.setAdapter(adapter);
    }
}