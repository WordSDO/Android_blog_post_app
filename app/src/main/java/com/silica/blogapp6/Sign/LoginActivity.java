package com.silica.blogapp6.Sign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.silica.blogapp6.R;
import com.silica.blogapp6.UI.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private String EMAIL, PASSWORD;
    private TextView login;
    private ProgressBar bar;
    private ImageView emailValid, togglePass;
    FirebaseAuth firebaseAuth;
    private int toggle = 0;
    public static final String EMAIL_PATTERN = "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        gettingLayoutIDs();
        firebaseAuth = FirebaseAuth.getInstance();
        textWatcher();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setText("");
                bar.setVisibility(View.VISIBLE);
                initialize();
                if (validate()){
                    loginUserWithEmailAndPassword();
                }
            }
        });

        togglePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (toggle){
                    case 0:
                        togglePass.setImageResource(R.drawable.invisible);
                        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        toggle++;
                        break;

                    case 1:
                        togglePass.setImageResource(R.drawable.visible);
                        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        toggle--;
                        break;
                }
            }
        });
    }

    private void loginUserWithEmailAndPassword() {
        firebaseAuth.signInWithEmailAndPassword(EMAIL, PASSWORD)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            bar.setVisibility(View.GONE);
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        else {
                            Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                            login.setText("Login");
                            bar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void textWatcher(){
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (email.getText().toString().matches(EMAIL_PATTERN)){
                    emailValid.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.colorGreen));
                }

                else emailValid.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.colorDarkGrey));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private boolean validate(){
        boolean valid = true;

        if (EMAIL.isEmpty()){
            email.setError("Enter your email");
            email.requestFocus();
            valid = false;
        }

        else if (!EMAIL.matches(EMAIL_PATTERN)){
            email.setError("Enter a valid email");
            email.requestFocus();
            valid = false;
        }

        else if (PASSWORD.isEmpty()){
            password.setError("Define a password");
            password.requestFocus();
            valid = false;
        }

        else if (PASSWORD.length() < 6 || PASSWORD.length() > 20){
            password.setError("Password should be 6 to 20 characters");
            password.requestFocus();
            valid = false;
        }

        return valid;
    }

    private void initialize(){
        EMAIL = email.getText().toString();
        PASSWORD = password.getText().toString();
    }

    private void gettingLayoutIDs(){
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        bar = findViewById(R.id.login_progress_bar);
        emailValid = findViewById(R.id.email_valid);
        togglePass = findViewById(R.id.toggle_pass);
    }
}
