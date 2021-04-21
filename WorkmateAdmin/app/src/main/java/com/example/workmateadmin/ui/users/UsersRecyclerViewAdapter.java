package com.example.workmateadmin.ui.users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workmateadmin.R;
import com.example.workmateadmin.modelo.Usuario;

import java.util.List;

public class UsersRecyclerViewAdapter extends RecyclerView.Adapter<UsersRecyclerViewAdapter.ViewHolder> {

    private final List<Usuario> users;
    private final Context context;
    private OnUserChatClickListener listener;

    public UsersRecyclerViewAdapter(List<Usuario> users, Context contexto, OnUserChatClickListener listener) {
        context = contexto;
        this.users = users;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // Si el usuario esta nulo, es porque aun no se ha cargado, así que mostrara un
        // Texto indicando que esta cargando
        if(users.get(position).getFoto() == null) {
            Glide.with(this.context)
                    .load(R.drawable.default_user)
                    .circleCrop()
                    .into(holder.userImage);
        } else {
            if (users.get(position).getFoto().equals("default"))
                Glide.with(this.context)
                        .load(R.drawable.default_user)
                        .circleCrop()
                        .into(holder.userImage);
            else if (users.get(position).getFoto().equals("Default"))
                Glide.with(this.context)
                        .load(R.drawable.default_empresa)
                        .circleCrop()
                        .into(holder.userImage);
            else {
                Glide.with(this.context)
                        .load(users.get(position).getFoto())
                        .circleCrop()
                        .into(holder.userImage);
            }
            holder.username.setText(users.get(position).getNombre());
        }
        holder.id = users.get(position).getIdUsuario();
    }
    @Override
    public int getItemCount() {
        return users.size();
    }
    public interface OnUserChatClickListener {
        public void irUser(String c);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView userImage, notify;
        public TextView username;
        public String id;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            userImage = view.findViewById(R.id.imageViewUserCardAvatar);
            username = view.findViewById(R.id.textViewNameUserCard);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.irUser(id);
                }
            });
        }

    }
}