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


public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";


    @Bind(R.id.input_email) EditText emailText;
    @Bind(R.id.input_password) EditText passwordText;
    @Bind(R.id.input_reEnterPassword) EditText reEnterPasswordText;
    @Bind(R.id.btn_signup) Button signupButton;
    @Bind(R.id.link_login) TextView loginLink;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        /*declare object for Firebase*/
        mAuth = FirebaseAuth.getInstance();

        /*
         * method that sets up the signup button
         * the method takes user input, email and password and stores it into a string
         * method validate() is then called
         * if the validate method has failed the onSignupFailed() method is called
         * if the validate method has passed, the createAccount() method is called with passing two paramaters
         * @param email
         * @param password
         */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CIS3334", "Creating a new user account");
                //create account for new users
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                String reEnterPassword = reEnterPasswordText.getText().toString();
                if (!validate()) {
                    onSignupFailed();
                    return;
                } else {
                    createAccount(email, password);
                }
            }
            /* 8 @Override
            public void onClick(View v) {
                signup();
            } */
        });

         /*
         * method that sets up the login link
         * when button the login linked clicked the new activity is started
         * @param getApplicationContext returns the context for the entire application
         * @param LoginActivity is started
         */
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

        /*
         * method that that is called when user's credentials have passed the validation
         * the paramaters email and string stored in a string are fetched
         * if the password and email are valid credentials, the user is signed in and returned to MainActivity
         * if the user authentication fails, the Toast is displayed
          */
    private void createAccount(String email, String password) {
        //create account for new users
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {  //update listener.
                if (!task.isSuccessful()) { //when failed
                    Toast.makeText(SignupActivity.this, "createAccount--Authentication failed.",Toast.LENGTH_LONG).show();
                } else {
                    //return to MainActivity is login works
                    finish();
                }
            }
        });
    }

    /*
     * if validation failed, the method SignUpFailed is called()
     * The toast is being displayed
     * the signupButton is enabled then once again for user to try to sign in once again
     */

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        signupButton.setEnabled(true);
    }

    /*
     * the validate method is called when the user tries to sign up
     * the input email, password and reEnterPassword is stored in a string
     * if an email,password or reEnter input is empty an error is prompted and validation has failed
     * in order for password input to be pass validation the password must be within 4 and 10 digits
     * similarly the reEnterPassword must pass the same validation
     * in addition the reEnterPassword field must match the initial password field
     * once all three pass the validations, the method sends back that these fields have been successfully validated
     * user is then enabled to log in
     */
    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String reEnterPassword = reEnterPasswordText.getText().toString();


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            reEnterPasswordText.setError(null);
        }

        return valid;
    }
}