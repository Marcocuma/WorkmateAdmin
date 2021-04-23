package com.example.workmateadmin.ui.companies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workmateadmin.R;
import com.example.workmateadmin.modelo.Empresa;
import com.example.workmateadmin.modelo.Usuario;

import java.util.List;

public class CompaniesRecyclerViewAdapter extends RecyclerView.Adapter<CompaniesRecyclerViewAdapter.ViewHolder> {

    private final List<Empresa> companies;
    private final Context context;
    private OnCompanyChatClickListener listener;

    public CompaniesRecyclerViewAdapter(List<Empresa> companies, Context contexto, OnCompanyChatClickListener listener) {
        context = contexto;
        this.companies = companies;
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
        if(companies.get(position).getLogo() == null) {
            Glide.with(this.context)
                    .load(R.drawable.default_user)
                    .circleCrop()
                    .into(holder.userImage);
        } else {
            if (companies.get(position).getLogo().equals("default"))
                Glide.with(this.context)
                        .load(R.drawable.default_user)
                        .circleCrop()
                        .into(holder.userImage);
            else if (companies.get(position).getLogo().equals("Default"))
                Glide.with(this.context)
                        .load(R.drawable.default_empresa)
                        .circleCrop()
                        .into(holder.userImage);
            else {
                Glide.with(this.context)
                        .load(companies.get(position).getLogo())
                        .circleCrop()
                        .into(holder.userImage);
            }
            holder.username.setText(companies.get(position).getNombre());
        }
        holder.id = companies.get(position).getIdEmpresa();
    }
    @Override
    public int getItemCount() {
        return companies.size();
    }
    public interface OnCompanyChatClickListener {
        public void goCompany(String c);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView userImage;
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
                    listener.goCompany(id);
                }
            });
        }

    }
}