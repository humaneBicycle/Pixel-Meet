package com.brobia.pixelmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class EmailLoginActivity extends AppCompatActivity {

    EditText emailET, passwordET;
    ImageView next;
    FirebaseAuth mAuth;
    boolean emailAlreadyExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        emailET = findViewById(R.id.email_login_activity);
        passwordET = findViewById(R.id.password_login);
        next = findViewById(R.id.img_email_login_next);

        mAuth = FirebaseAuth.getInstance();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(EmailLoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(EmailLoginActivity.this, "Sign in successfull", Toast.LENGTH_SHORT).show();
                                    new PreferenceGetter(EmailLoginActivity.this).putBoolean(PreferenceGetter.IS_SIGNED_IN, true);
                                    loginHandler(email,true);
                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(EmailLoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }
                            }
                        });
            }
        });
    }
    private void loginHandler(String email, boolean isLoginSuccessful){
        if(isLoginSuccessful){
            doesUserWithThisMailExist(email);
            if(emailAlreadyExist){
                startActivity(new Intent(EmailLoginActivity.this,HomeActivity.class));
                new PreferenceGetter(this).putBoolean(PreferenceGetter.IS_SIGNED_IN,true);
            }else{
                startActivity(new Intent(EmailLoginActivity.this,RegisterActivity.class).putExtra("email",email));
                new PreferenceGetter(this).putBoolean(PreferenceGetter.IS_SIGNED_IN,true);
            }
        }
    }

    private boolean doesUserWithThisMailExist(String mail){

        FirebaseFirestore.getInstance().collection("users").whereEqualTo("email",mail).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    emailAlreadyExist = false;
                    //new PreferenceGetter(LoginActivity.this).putBoolean(PreferenceGetter.IS_REGISTERED,false);
                    //startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                }else{
                    emailAlreadyExist = true;
                    //new PreferenceGetter(LoginActivity.this).putBoolean(PreferenceGetter.IS_REGISTERED,true);
                    //startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EmailLoginActivity.this, "Task Failed. Please Try again", Toast.LENGTH_SHORT).show();
            }
        });
        return emailAlreadyExist;
    }
}