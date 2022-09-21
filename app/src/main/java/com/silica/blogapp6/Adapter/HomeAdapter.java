package com.silica.blogapp6.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.silica.blogapp6.Model.PostModel;
import com.silica.blogapp6.R;
import com.silica.blogapp6.UI.DetailsActivity;
import com.silica.blogapp6.UI.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{

    Activity activity;
    ArrayList<PostModel> list;

    public HomeAdapter(Activity activity, ArrayList<PostModel> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Blog App").child("Users").child(list.get(position).getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.get().load(snapshot.child("picture").getValue(String.class)).into(holder.pp);
                holder.name.setText(snapshot.child("name").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Picasso.get().load(list.get(position).getPicture()).into(holder.bp);
        holder.title.setText(list.get(position).getTitle());
        holder.category.setText(list.get(position).getCategory());
        holder.blog.setText(list.get(position).getBlog());
        holder.likes.setText(list.get(position).getLikes());
        holder.views.setText(list.get(position).getViews());
        holder.date.setText(list.get(position).getDate());

        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Blog App").child("Likes").child(list.get(position).getId());

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String L = Long.toString(snapshot.getChildrenCount());
                holder.likes.setText(L);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reff.orderByChild("id").equalTo(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    holder.like.setBackgroundResource(R.drawable.liked);
                    holder.likeIco.setColorFilter(ContextCompat.getColor(activity, R.color.colorBlue));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ProfileActivity.class);
                intent.putExtra("uid", list.get(position).getUid());
                activity.startActivity(intent);
            }
        });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Blog App").child("Likes").child(list.get(position).getId());

                ref.orderByChild("id").equalTo(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            ref.child(FirebaseAuth.getInstance().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    holder.like.setBackgroundResource(R.drawable.stroke);
                                    holder.likeIco.setColorFilter(ContextCompat.getColor(activity, R.color.colorDarkGrey));
                                    //By: Caffeine Software
                                    //Author: Saad Ahmed
                                }
                            });
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String L = Long.toString(snapshot.getChildrenCount());
                                    holder.likes.setText(L);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        else {
                            ref.child(FirebaseAuth.getInstance().getUid()).child("id").setValue(FirebaseAuth.getInstance().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    holder.like.setBackgroundResource(R.drawable.liked);
                                    holder.likeIco.setColorFilter(ContextCompat.getColor(activity, R.color.colorBlue));
                                }
                            });

                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String L = Long.toString(snapshot.getChildrenCount());
                                    holder.likes.setText(L);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Blog App").child("Posts").child(list.get(position).getCategory())
                        .child(list.get(position).getId());
                DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Blog App").child("Users Post").child(list.get(position).getUid())
                        .child(list.get(position).getId());
                int view = Integer.parseInt(list.get(position).getViews()) + 1;
                ref.child("views").setValue(Integer.toString(view));
                reff.child("views").setValue(Integer.toString(view));

                Intent intent = new Intent(activity, DetailsActivity.class);
                intent.putExtra("id", list.get(position).getId());
                intent.putExtra("uid", list.get(position).getUid());
                intent.putExtra("cate", list.get(position).getCategory());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView pp, bp, likeIco;
        TextView name, title, blog, likes, views, category, date;
        LinearLayout like, layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            likeIco = itemView.findViewById(R.id.like_icon);
            name = itemView.findViewById(R.id.name);
            pp = itemView.findViewById(R.id.profile_picture);
            bp = itemView.findViewById(R.id.blog_image);
            title = itemView.findViewById(R.id.blog_title);
            blog = itemView.findViewById(R.id.description);
            likes = itemView.findViewById(R.id.likes);
            views = itemView.findViewById(R.id.views);
            like = itemView.findViewById(R.id.like_btn);
            layout = itemView.findViewById(R.id.post_layout);
            category = itemView.findViewById(R.id.category);
            date = itemView.findViewById(R.id.date);
        }
    }
}
