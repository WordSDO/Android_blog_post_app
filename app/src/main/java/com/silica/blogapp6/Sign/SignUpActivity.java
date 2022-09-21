package com.silica.blogapp6.Sign;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.silica.blogapp6.Model.UserDetails;
import com.silica.blogapp6.R;
import com.silica.blogapp6.UI.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    private EditText fName, lName, email, phone, password, confirmPass;
    private String FNAME, LNAME, EMAIL, PHONE, PASSWORD, CONFIRMPASS, URL;
    private LinearLayout layout_one, layout_two, layout_three;
    private TextView next, previous, step_2, step_3;
    private View view1, view2;
    private Uri uri;
    private CircleImageView picture;
    private ImageView back, two, three, emailValid, phoneValid, pass_matched, toggle_pass;
    private Animation slide_right, slide_left, left_second, right_second;
    private ProgressBar bar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;
    private StorageReference storageRef;

    public static final String EMAIL_PATTERN = "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";

    private int c = 0, toggle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        gettingLayoutIDs();
        textWatcher();

        firebaseAuth = FirebaseAuth.getInstance();

        toggle_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (toggle){
                    case 0:
                        toggle_pass.setImageResource(R.drawable.invisible);
                        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        confirmPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        toggle++;
                        break;

                    case 1:
                        toggle_pass.setImageResource(R.drawable.visible);
                        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        confirmPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        toggle--;
                        break;
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (c){
                    case 0:
                        initialize();
                        if (validateLayoutOne()){
                            layout_one.startAnimation(slide_left);
                            layout_two.startAnimation(left_second);
                            layout_two.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    layout_one.setVisibility(View.GONE);
                                    previous.setVisibility(View.VISIBLE);
                                    two.setColorFilter(ContextCompat.getColor(SignUpActivity.this, R.color.colorGreen));
                                    view1.setBackgroundResource(R.color.colorGreen);
                                    step_2.setTextColor(getResources().getColor(R.color.colorGreen));
                                }
                            },300);
                            c++;
                        }
                        break;

                    case 1:
                        initialize();
                        if (validateLayoutTwo()){
                            layout_two.startAnimation(slide_left);
                            layout_three.startAnimation(left_second);
                            layout_three.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    layout_two.setVisibility(View.GONE);
                                    three.setColorFilter(ContextCompat.getColor(SignUpActivity.this, R.color.colorGreen));
                                    view2.setBackgroundResource(R.color.colorGreen);
                                    step_3.setTextColor(getResources().getColor(R.color.colorGreen));
                                    next.setText("Sign Up");
                                }
                            },300);
                            c++;
                        }
                        break;

                    case 2:
                        if (uri == null){
                            Toast.makeText(SignUpActivity.this, "Define your profile picture", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            bar.setVisibility(View.VISIBLE);
                            next.setText("");
                            signUpUser();
                            c++;
                        }
                        break;
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (c){
                    case 1:
                        layout_two.startAnimation(slide_right);
                        layout_one.startAnimation(right_second);
                        layout_one.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                layout_two.setVisibility(View.GONE);
                                previous.setVisibility(View.GONE);
                                two.setColorFilter(ContextCompat.getColor(SignUpActivity.this, R.color.colorDarkGrey));
                                view1.setBackgroundResource(R.color.colorDarkGrey);
                                step_2.setTextColor(getResources().getColor(R.color.colorDarkGrey));
                            }
                        },300);
                        c--;
                        break;

                    case 2:
                        layout_three.startAnimation(slide_right);
                        layout_two.startAnimation(right_second);
                        layout_two.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                layout_three.setVisibility(View.GONE);
                                three.setColorFilter(ContextCompat.getColor(SignUpActivity.this, R.color.colorDarkGrey));
                                view2.setBackgroundResource(R.color.colorDarkGrey);
                                step_3.setTextColor(getResources().getColor(R.color.colorDarkGrey));
                                next.setText("Next Step");
                            }
                        },300);
                        c--;
                        break;
                }
            }
        });
    }

    //--------------------------------------------------------------------------------Sign Up User-------------------------------------------------------------------

    private void signUpUser(){
        firebaseAuth.createUserWithEmailAndPassword(EMAIL, PASSWORD)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            uploadImage();
                        }

                        else {
                            Toast.makeText(SignUpActivity.this, "Email already registered", Toast.LENGTH_SHORT).show();
                            c--;
                            next.setText("Sign Up");
                        }
                    }
                });
    }



    //--------------------------------------------------------------------------------Sign Up User-------------------------------------------------------------------





    //--------------------------------------------------------------------------------Upload Image-------------------------------------------------------------------

    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri = data.getData();
            picture.setImageURI(uri);
        }
    }

    private void uploadImage(){
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //here you can choose quality factor in third parameter(ex. i choosen 20)
        bmp.compress(Bitmap.CompressFormat.JPEG, 15, baos);
        byte[] fileInBytes = baos.toByteArray();

        final StorageReference photoref = storageRef.child(uri.getLastPathSegment());

        photoref.putBytes(fileInBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                photoref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        URL = uri.toString();

                        UserDetails user = new UserDetails(FirebaseAuth.getInstance().getUid(), FNAME + " " + LNAME, EMAIL, PHONE, PASSWORD, URL);
                        ref.child(FirebaseAuth.getInstance().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(SignUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                    bar.setVisibility(View.GONE);
                                    next.setText("Login");
                                    Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                else {
                                    Toast.makeText(SignUpActivity.this, "Account creation failed", Toast.LENGTH_SHORT).show();
                                    bar.setVisibility(View.GONE);
                                    next.setText("Sign Up");
                                }
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                bar.setVisibility(View.GONE);
                next.setText("Sign Up");
                Toast.makeText(SignUpActivity.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //--------------------------------------------------------------------------------Upload Image-------------------------------------------------------------------

    private boolean validateLayoutOne(){
        boolean valid = true;

        if (FNAME.isEmpty()){
            fName.setError("Enter your first name");
            fName.requestFocus();
            valid = false;
        }

        else if (LNAME.isEmpty()){
            lName.setError("Enter your last name");
            lName.requestFocus();
            valid = false;
        }

        else if (EMAIL.isEmpty()){
            email.setError("Enter your email");
            email.requestFocus();
            valid = false;
        }

        else if (!EMAIL.matches(EMAIL_PATTERN)){
            email.setError("Enter a valid email");
            email.requestFocus();
            valid = false;
        }

        else if (PHONE.isEmpty()){
            phone.setError("Enter your number");
            phone.requestFocus();
            valid = false;
        }

        else if (PHONE.length() < 11){
            phone.setError("Enter a valid number");
            phone.requestFocus();
            valid = false;
        }

        return valid;
    }

    private boolean validateLayoutTwo(){
        boolean valid = true;

        if (PASSWORD.isEmpty()){
            password.setError("Define a password");
            password.requestFocus();
            valid = false;
        }

        else if (PASSWORD.length() < 6 || PASSWORD.length() > 20){
            password.setError("Password should be 6 to 20 characters");
            password.requestFocus();
            valid = false;
        }

        else if (!CONFIRMPASS.equals(PASSWORD)){
            confirmPass.setError("Password didn't match");
            confirmPass.requestFocus();
            valid = false;
        }

        return valid;
    }

    private void textWatcher(){
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (email.getText().toString().matches(EMAIL_PATTERN)){
                    emailValid.setColorFilter(ContextCompat.getColor(SignUpActivity.this, R.color.colorGreen));
                }

                else emailValid.setColorFilter(ContextCompat.getColor(SignUpActivity.this, R.color.colorDarkGrey));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (phone.getText().toString().length() == 11){
                    phoneValid.setColorFilter(ContextCompat.getColor(SignUpActivity.this, R.color.colorGreen));
                }

                else phoneValid.setColorFilter(ContextCompat.getColor(SignUpActivity.this, R.color.colorDarkGrey));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String P = password.getText().toString();
                if (confirmPass.getText().toString().equals(P) && P.length() > 5){
                    pass_matched.setColorFilter(ContextCompat.getColor(SignUpActivity.this, R.color.colorGreen));
                }

                else pass_matched.setColorFilter(ContextCompat.getColor(SignUpActivity.this, R.color.colorDarkGrey));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initialize(){
        FNAME = fName.getText().toString();
        LNAME = lName.getText().toString();
        EMAIL = email.getText().toString();
        PHONE = phone.getText().toString();
        PASSWORD = password.getText().toString();
        CONFIRMPASS = confirmPass.getText().toString();
    }

    private void gettingLayoutIDs(){
        slide_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right);
        slide_left = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left);
        left_second = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left_second);
        right_second = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right_second);

        fName = findViewById(R.id.first_name);
        lName = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.number);
        password = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirm_pass);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        step_2 = findViewById(R.id.step2);
        step_3 = findViewById(R.id.step3);
        view1 = findViewById(R.id.view_one);
        view2 = findViewById(R.id.view_two);
        picture = findViewById(R.id.choose_picture);

        layout_one = findViewById(R.id.step_one_layout);
        layout_two = findViewById(R.id.step_two_layout);
        layout_three = findViewById(R.id.step_three_layout);

        emailValid = findViewById(R.id.email_valid);
        phoneValid = findViewById(R.id.phone_valid);
        pass_matched = findViewById(R.id.pass_matched);
        toggle_pass = findViewById(R.id.toggle_pass);

        back = findViewById(R.id.back);
        two = findViewById(R.id.img_two);
        three = findViewById(R.id.img_three);
        bar = findViewById(R.id.sign_up_progress_bar);

        ref = FirebaseDatabase.getInstance().getReference().child("Blog App").child("Users");
        storageRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
