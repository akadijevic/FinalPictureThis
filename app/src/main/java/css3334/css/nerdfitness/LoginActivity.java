package css3334.css.nerdfitness;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;


    @Bind(R.id.input_email) EditText emailText;
    @Bind(R.id.input_password) EditText passwordText;
    @Bind(R.id.btn_login) Button loginButton;
    @Bind(R.id.link_signup) TextView signupLink;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setupCreateButton();
        setupLoginButton();

        /* declares object for Firebase*/
        mAuth = FirebaseAuth.getInstance();
    }

        /*
         * method that sets up the login button
         * the method takes user input, email and password and stores it into a string
         * the method signIn is called passing the two parameters
         * @param email
         * @param password
         */
        private void setupLoginButton() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CIS3334", "Signing in the user");
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                signIn(email,password);
            }

        });

    }
        /*
         * method that sets up the create link
         * when button create new account clicked the new activity is started
         * @param getApplicationContext returns the context for the entire application
         * @param SignUpActivty is started
         */

    private void setupCreateButton() {
        signupLink.setOnClickListener(new View.OnClickListener() {

           @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

        /*
         * method that that is called when user signs in
         * the paramaters email and string stored in a string are fetched
         * if the password and email are valid credentials, the user is signed in and returned to MainActivity
         * if the user authentication fails, the Toast is displayed
         */

    private void signIn(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() { //add to listener
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "SignIn--Authentication failed.",Toast.LENGTH_LONG).show();
                } else {

                    finish();
                }
            }
        });
    }
    }

