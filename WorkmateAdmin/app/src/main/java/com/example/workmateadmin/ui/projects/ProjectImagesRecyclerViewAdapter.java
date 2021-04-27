package com.example.workmateadmin.ui.projects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workmateadmin.R;

import java.util.List;

public class ProjectImagesRecyclerViewAdapter extends RecyclerView.Adapter<ProjectImagesRecyclerViewAdapter.ViewHolder> {

    private final List<String> images;
    private final Context context;

    public ProjectImagesRecyclerViewAdapter(List<String> images, Context contexto) {
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