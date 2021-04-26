package com.example.workmateadmin.ui.projects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.workmateadmin.R;
import com.example.workmateadmin.modelo.Proyecto;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProjectsRecyclerViewAdapter extends RecyclerView.Adapter<ProjectsRecyclerViewAdapter.ViewHolder> {

    private final List<Proyecto> projects;
    private final Context context;
    private final OnProjectClickListener listener;

    public ProjectsRecyclerViewAdapter(List<Proyecto> projects, Context contexto, OnProjectClickListener listener2) {
        context = contexto;
        listener = listener2;
        this.projects = projects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.project = projects.get(position);
        holder.nombre.setText(projects.get(position).getPresentacion());
        // Añado las categorias al grupo de chips
        holder.group.removeAllViews();
        if (projects.get(position).getCaracteristicas() != null) {
            for (String cat : projects.get(position).getCaracteristicas()){
                Chip chip = new Chip(context);
                chip.setText(cat);
                holder.group.addView(chip);
            }
        }
        // Si tiene imagenes, las muestra, si no tiene o si esta a nulo pone una indicando que no hay fotos
        List<SlideModel> slideModels = new ArrayList<>();
        if (projects.get(position).getImagenes() != null) {
            if(projects.get(position).getImagenes().size() > 0) {
                for (String image : projects.get(position).getImagenes()) {
                    slideModels.add(new SlideModel(image, ScaleTypes.CENTER_CROP));
                }
            } else {
                slideModels.add(new SlideModel(R.drawable.imagenotfound, ScaleTypes.CENTER_CROP));
            }
        } else {
            slideModels.add(new SlideModel(R.drawable.imagenotfound, ScaleTypes.CENTER_CROP));
        }
        holder.galerySlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);
        if(projects.get(position).getLocalidad() != null){
            holder.localidad.setText(projects.get(position).getLocalidad());
        }
        if(projects.get(position).getProvincia() != null){
            holder.provincia.setText(projects.get(position).getProvincia());
        }
        if(projects.get(position).getCreado() != null){
            Date date=new Date(projects.get(position).getCreado());
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            holder.fecha.setText(String.format(Locale.getDefault(),"%d/%d/%d %d:%d", cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR),
                    cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)));
        }
    }
    public interface OnProjectClickListener {
        public void goDetail(Proyecto p);
    }
    @Override
    public int getItemCount() {
        return projects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Proyecto project;
        public TextView nombre, localidad, provincia, fecha;
        public Button boton;
        public ChipGroup group;
        public ImageSlider galerySlider;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            boton = view.findViewById(R.id.buttonProjectSeeDetails);
            nombre = view.findViewById(R.id.textViewProyectPresentation);
            localidad = view.findViewById(R.id.textViewTownCardProject);
            provincia = view.findViewById(R.id.textViewProvinceCardProject);
            fecha = view.findViewById(R.id.textViewDateCreationCardProject);
            group = view.findViewById(R.id.chipGroupCardProject);
            galerySlider = view.findViewById(R.id.imageSliderProjectCard);
            boton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.goDetail(project);
                }
            });
        }

    }
}