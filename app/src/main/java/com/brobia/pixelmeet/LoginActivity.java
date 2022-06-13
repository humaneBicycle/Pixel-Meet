package com.brobia.pixelmeet;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    TextView emailLogin, gmailLogin, registerButton, facebookLogin;
    boolean emailAlreadyExist;
    private static final int SIGN_IN_CODE = 100;
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;
    FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //logic if user quits after signing in but not registereing
        boolean isSignIn = (new PreferenceGetter(this).getBoolean(PreferenceGetter.IS_SIGNED_IN));
        boolean isRegistered = (new PreferenceGetter(this).getBoolean(PreferenceGetter.IS_REGISTERED));
        if( isSignIn && isRegistered ){
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
        }else if(isSignIn){
            startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
        }
        setContentView(R.layout.activity_login);

        Log.d("pwd", "login activity created");

        emailLogin = findViewById(R.id.login_activity_email);
        gmailLogin = findViewById(R.id.login_activity_google);
        facebookLogin = findViewById(R.id.login_activity_facebook);
        registerButton = findViewById(R.id.login_activity_register);

        mAuth = FirebaseAuth.getInstance();


        emailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,EmailLoginActivity.class));
            }
        });
        gmailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.google_web_client_id))
                        .requestEmail()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
                //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
                // TODO updateUI(account);

                googleSignIn();
            }
        });

        callbackManager = CallbackManager.Factory.create();
        facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //callback for facebook login
                FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));
                FacebookSdk.setClientToken(getResources().getString(R.string.facebook_client_token));
                FacebookSdk.sdkInitialize(LoginActivity.this);
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                //App code
                                //get email id using graph api
                                handleFacebookAccessToken(loginResult.getAccessToken());
                                GraphRequest request = GraphRequest.newMeRequest(
                                        loginResult.getAccessToken(),
                                        new GraphRequest.GraphJSONObjectCallback() {
                                            @Override
                                            public void onCompleted(
                                                    JSONObject object,
                                                    GraphResponse response) {
                                                // Application code
                                                try {
                                                    String mail = object.getString("email");
                                                    Toast.makeText(LoginActivity.this, "Sign in successful " , Toast.LENGTH_SHORT).show();
                                                    loginHandler(mail,true);
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "email");
                                request.setParameters(parameters);
                                request.executeAsync();
                            }

                            @Override
                            public void onCancel() {
                                // App code
                                Toast.makeText(LoginActivity.this, "Sign in cancelled " , Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                // App code
                                Toast.makeText(LoginActivity.this, "Unable to Sign in. Please try again later." , Toast.LENGTH_SHORT).show();
                            }
                        });

                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile","email"));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, SIGN_IN_CODE);
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
                Toast.makeText(LoginActivity.this, "Task Failed. Please Try again", Toast.LENGTH_SHORT).show();
            }
        });
        return emailAlreadyExist;
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d("pwd", "handleSignInResult: sign in using google successful.");
            firebaseAuthWithGoogle(account.getIdToken());
            Log.d("pwd", "handleSignInResult: credentials stored with firebase");
            Toast.makeText(this, "SIgn in using " + account.getEmail()+account.getIdToken(), Toast.LENGTH_SHORT).show();

            Log.d("pwd", "handleSignInResult: credentials stored with firebase"+account.getIdToken()+account.getEmail());

            // Signed in successfully, show authenticated UI.
            loginHandler(account.getEmail(),true);
//            doesUserWithThisMailExist(account.getEmail());
//            if(emailAlreadyExist){
//                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
//                new PreferenceGetter(this).putBoolean(PreferenceGetter.IS_SIGNED_IN,true);
//            }
            //TODO updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("handleSignInresult", "signInResult:" +e.getStatus() + e.getMessage());
            Toast.makeText(this, "SIgn In Failed. Please try again later.", Toast.LENGTH_SHORT).show();
            //TODO updateUI(null);
        }
    }

    private void loginHandler(String email, boolean isLoginSuccessful){
        if(isLoginSuccessful){
            doesUserWithThisMailExist(email);
            if(emailAlreadyExist){
                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                new PreferenceGetter(this).putBoolean(PreferenceGetter.IS_SIGNED_IN,true);
                finish();
            }else{
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class).putExtra("email",email));
                new PreferenceGetter(this).putBoolean(PreferenceGetter.IS_SIGNED_IN,true);
                finish();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //facebook login
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == SIGN_IN_CODE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        Log.d("pwd", "firebaseAuthWithGoogle: started");
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("pwd", "signInWithCredential:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("pwd", "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }
}