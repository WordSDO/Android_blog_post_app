package com.silica.blogapp6.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.silica.blogapp6.Model.PostModel;
import com.silica.blogapp6.R;
import com.silica.blogapp6.UI.DetailsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>{

    Activity activity;
    ArrayList<PostModel> list;

    public ProfileAdapter(Activity activity, ArrayList<PostModel> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Picasso.get().load(list.get(position).getPicture()).into(holder.image);
        holder.title.setText(list.get(position).getTitle());
        holder.blog.setText(list.get(position).getBlog());

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Blog App");

        if (!FirebaseAuth.getInstance().getUid().equals(list.get(position).getUid())){
            holder.delete.setVisibility(View.GONE);
        }

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

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                dialog.setMessage("The post will be deleted");
                dialog.setPositiveButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ref.child("Users Post").child(FirebaseAuth.getInstance().getUid()).child(list.get(position).getId()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                ref.child("Posts").child(list.get(position).getCategory()).child(list.get(position).getId()).removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                ref.child("Likes").child(list.get(position).getId()).removeValue();
                                                            }
                                                        });
                                            }
                                        });
                            }
                        }
                );
                Dialog Dialog = dialog.create();
                Dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout layout;
        ImageView image, delete;
        TextView title, blog;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.post_layout);
            image = itemView.findViewById(R.id.image);
            delete = itemView.findViewById(R.id.delete);
            title = itemView.findViewById(R.id.blog_title);
            blog = itemView.findViewById(R.id.blog);
        }
    }
}
