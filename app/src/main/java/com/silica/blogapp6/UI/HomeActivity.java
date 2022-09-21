package com.silica.blogapp6.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.silica.blogapp6.Adapter.HomeAdapter;
import com.silica.blogapp6.Model.PostModel;
import com.silica.blogapp6.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ImageView profile;
    private TextView createPost, all, one, two, three, four, five;
    private RecyclerView recyclerView;
    private DatabaseReference postRef;
    private ArrayList<PostModel> list;
    private Activity activity;
    private LinearLayout notFound;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        gettingLayoutIDs();
        getPosts();
        getProfilePicture();
        onClickListeners();
        activity = HomeActivity.this;

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("uid", FirebaseAuth.getInstance().getUid());
                startActivity(intent);
            }
        });

        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CreatePostActivity.class);
                startActivity(intent);
            }
        });

        dialog.setContentView(R.layout.progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void onClickListeners(){
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.setBackgroundResource(R.drawable.selected);
                one.setBackgroundResource(R.drawable.unsilectec);
                two.setBackgroundResource(R.drawable.unsilectec);
                three.setBackgroundResource(R.drawable.unsilectec);
                four.setBackgroundResource(R.drawable.unsilectec);
                five.setBackgroundResource(R.drawable.unsilectec);
                getPosts();
            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.setBackgroundResource(R.drawable.unsilectec);
                one.setBackgroundResource(R.drawable.selected);
                two.setBackgroundResource(R.drawable.unsilectec);
                three.setBackgroundResource(R.drawable.unsilectec);
                four.setBackgroundResource(R.drawable.unsilectec);
                five.setBackgroundResource(R.drawable.unsilectec);

                getCategorizedPosts(one.getText().toString());
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.setBackgroundResource(R.drawable.unsilectec);
                one.setBackgroundResource(R.drawable.unsilectec);
                two.setBackgroundResource(R.drawable.selected);
                three.setBackgroundResource(R.drawable.unsilectec);
                four.setBackgroundResource(R.drawable.unsilectec);
                five.setBackgroundResource(R.drawable.unsilectec);

                getCategorizedPosts(two.getText().toString());
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.setBackgroundResource(R.drawable.unsilectec);
                one.setBackgroundResource(R.drawable.unsilectec);
                two.setBackgroundResource(R.drawable.unsilectec);
                three.setBackgroundResource(R.drawable.selected);
                four.setBackgroundResource(R.drawable.unsilectec);
                five.setBackgroundResource(R.drawable.unsilectec);

                getCategorizedPosts(three.getText().toString());
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.setBackgroundResource(R.drawable.unsilectec);
                one.setBackgroundResource(R.drawable.unsilectec);
                two.setBackgroundResource(R.drawable.unsilectec);
                three.setBackgroundResource(R.drawable.unsilectec);
                four.setBackgroundResource(R.drawable.selected);
                five.setBackgroundResource(R.drawable.unsilectec);

                getCategorizedPosts(four.getText().toString());
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.setBackgroundResource(R.drawable.unsilectec);
                one.setBackgroundResource(R.drawable.unsilectec);
                two.setBackgroundResource(R.drawable.unsilectec);
                three.setBackgroundResource(R.drawable.unsilectec);
                four.setBackgroundResource(R.drawable.unsilectec);
                five.setBackgroundResource(R.drawable.selected);

                getCategorizedPosts(five.getText().toString());
            }
        });
    }

    private void getCategorizedPosts(String cate){
        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("Blog App").child("Posts").child(cate);
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot ds: snapshot.getChildren()){
                    list.add(ds.getValue(PostModel.class));
                }
                HomeAdapter adapter = new HomeAdapter(activity, list);

                if (adapter.getItemCount() > 0){
                    dialog.dismiss();
                    notFound.setVisibility(View.GONE);
                }
                else {
                    notFound.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getProfilePicture(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Blog App").child("Users");
        ref.child(FirebaseAuth.getInstance().getUid()).child("picture").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String url = snapshot.getValue(String.class);
                Picasso.get().load(url).into(profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getPosts(){
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dss: snapshot.getChildren()){
                    for (DataSnapshot ds: dss.getChildren()){
                        list.add(ds.getValue(PostModel.class));
                    }
                }
                HomeAdapter adapter = new HomeAdapter(activity, list);if (adapter.getItemCount() > 0){
                    dialog.dismiss();
                    notFound.setVisibility(View.GONE);
                }
                else {
                    notFound.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void gettingLayoutIDs(){
        profile = findViewById(R.id.profile_btn);
        createPost = findViewById(R.id.create_post_btn);
        all = findViewById(R.id.all);
        one = findViewById(R.id.cone);
        two = findViewById(R.id.ctwo);
        three = findViewById(R.id.cthree);
        four = findViewById(R.id.cfour);
        five = findViewById(R.id.cfive);
        notFound = findViewById(R.id.not_found);

        dialog = new Dialog(this);

        recyclerView = findViewById(R.id.home_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        postRef = FirebaseDatabase.getInstance().getReference().child("Blog App").child("Posts");
    }
}
