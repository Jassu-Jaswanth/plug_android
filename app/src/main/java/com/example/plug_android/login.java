package com.example.plug_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class login extends AppCompatActivity {

    private static final String db_url = "https://plug-android-default-rtdb.asia-southeast1.firebasedatabase.app/";
    EditText email;
    EditText passwd;

    private static final int RC_SIGN_IN = 2;
    SignInButton button;
    Button guest_button;
    Button login_button;
    private FirebaseAuth fAuth;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseUser user;
    FirebaseAuth.AuthStateListener fAuthListener;
    DatabaseReference mRef;

    TextView taketoregis;

    @Override
    protected void onStart() {
        super.onStart();

        fAuth.addAuthStateListener(fAuthListener);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(login.this, profile.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mRef = FirebaseDatabase.getInstance(db_url).getReference();
        email = findViewById(R.id.l_email);
        passwd = findViewById(R.id.l_password);

        taketoregis = findViewById(R.id.taketoregis);
        taketoregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this, register.class));
            }
        });

        login_button = findViewById(R.id.login);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_str = email.getText().toString();
                String passwd_str = passwd.getText().toString();
                if (email_str.isEmpty()){
                    email.setError("Please enter an email");
                }
                else if (passwd_str.isEmpty()){
                    passwd.setError("Please enter password");
                }
                else{
                    esignin(email_str,passwd_str, mRef);
                }
            }
        });

        guest_button = findViewById(R.id.guest_lgn_btn);

        guest_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo: make a function to get data from https://randomuser.me/ and use them as credentials
            }
        });

        button = findViewById(R.id.sign_in_button);
        button.setSize(SignInButton.SIZE_STANDARD);
        fAuth = FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gsignIn();
            }
        });

        fAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Toast.makeText(login.this, "listening for user loign", Toast.LENGTH_SHORT).show();
                if(firebaseAuth.getCurrentUser() != null){
                    updateUI(firebaseAuth.getCurrentUser());
                }
            }
        };



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .requestProfile()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



    }

    private void esignin(String email, String password, DatabaseReference mref){
        fAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user = fAuth.getCurrentUser();
                            UpdateDb(mref);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(login.this, "Invalid Email/Password",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void gsignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(String idToken, DatabaseReference mref) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(login.this, "Login Success", Toast.LENGTH_LONG).show();
                            user = fAuth.getCurrentUser();
                            UpdateDb(mref);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(login.this, "Sign in Failed, Try again", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account.getIdToken(), mRef);
            // Signed in successfully, show authenticated UI
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.

            Toast.makeText(this, "Sign-in Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void UpdateDb(DatabaseReference dbRef) {
        user = fAuth.getCurrentUser();
        if(user != null){
            User user1 = new User();
            user1.setDisplayName(user.getDisplayName());
            user1.setEmail(user.getEmail().toString());
            user1.setPhone(user.getPhoneNumber());
            user1.setPhotoURL(user.getPhotoUrl().toString());
            dbRef.child("users").child(user.getUid()).setValue(user1);
        }
    }

}