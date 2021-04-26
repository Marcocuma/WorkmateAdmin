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

import java.util.List;

public class CompaniesImagesRecyclerViewAdapter extends RecyclerView.Adapter<CompaniesImagesRecyclerViewAdapter.ViewHolder> {

    private final List<String> images;
    private final Context context;

    public CompaniesImagesRecyclerViewAdapter(List<String> images, Context contexto) {
        context = contexto;
        this.images = images;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Glide.with(this.context)
                .load(images.get(position))
                .fitCenter()
                .into(holder.image);

    }
    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView image;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            image = view.findViewById(R.id.imageViewImageCompanyGalery);
        }

    }
}