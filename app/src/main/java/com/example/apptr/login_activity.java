package com.example.apptr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import es.dmoral.toasty.Toasty;

public class login_activity extends AppCompatActivity implements Button.OnClickListener {


    private static final String TAG = "MSG:";
    private FirebaseAuth mAuth;


    private EditText TextMail;
    private EditText TextPassword;

    TextView txtbtnreg;
    Button btnlogin;

    ProgressDialog progressDialog;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Iniciar Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        //Referencies
        TextMail = (EditText)findViewById(R.id.emailEditText);
        TextPassword = (EditText)findViewById(R.id.passwordEditText);

        txtbtnreg = (TextView) findViewById(R.id.regbtn);
        btnlogin = (Button)findViewById(R.id.loginbtn);

        imageView = (ImageView)findViewById(R.id.imageView);

        progressDialog = new ProgressDialog(this);


    }


    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
    }

    private void signIn(){

        final String email = TextMail.getText().toString().trim();
        String password = TextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){

            Toasty.warning(this, "Indiqui el correu electrònic", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {

            Toasty.warning(this, "Indiqui la contrasenya", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Validant credencials...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toasty.info(login_activity.this, "Has iniciat sessió amb: " + email,
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            Intent i=new Intent(login_activity.this, agenda.class);
                            startActivity(i);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            Toasty.error(login_activity.this, "Autenticació fallida, comprobi els camps",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            progressDialog.dismiss();
                        }

                    }
                });
    }


    public void onClick(View view){
        signIn();
    }

    public void onClick2(View view){

        Intent a = new Intent(login_activity.this, signup_activity.class);
        startActivity(a);

    }


}

