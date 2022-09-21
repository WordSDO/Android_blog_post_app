package com.silica.blogapp6.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.silica.blogapp6.Adapter.ProfileAdapter;
import com.silica.blogapp6.Model.PostModel;
import com.silica.blogapp6.Model.UserDetails;
import com.silica.blogapp6.R;
import com.silica.blogapp6.Sign.SignActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private ImageView pp, logout;
    private TextView all, name, email, phone;
    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private ArrayList<PostModel> list;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        gettingLayoutIDs();
        gettingUserDetails();
        gettingBlog();

        if (!uid.equals(FirebaseAuth.getInstance().getUid())){
            logout.setVisibility(View.GONE);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileActivity.this, SignActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void gettingBlog(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Blog App").child("Users Post").child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot ds: snapshot.getChildren()){
                    list.add(ds.getValue(PostModel.class));
                    ProfileAdapter adapter = new ProfileAdapter(ProfileActivity.this, list);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void gettingUserDetails(){
        reference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails user;
                user = snapshot.getValue(UserDetails.class);
                Picasso.get().load(user.getPicture()).into(pp);
                name.setText(user.getName());
                email.setText(user.getEmail());
                phone.setText(user.getPhone());

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Blog App").child("Users Post").child(uid);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String A = Long.toString(snapshot.getChildrenCount());
                        all.setText(A);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void gettingLayoutIDs(){
        pp = findViewById(R.id.profile_picture);
        logout = findViewById(R.id.logout);
        all = findViewById(R.id.all_posts);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.number);
        recyclerView = findViewById(R.id.profile_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reference = FirebaseDatabase.getInstance().getReference().child("Blog App").child("Users");
    }
}
