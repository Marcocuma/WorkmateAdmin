package com.example.workmateadmin.ui.projects;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.workmateadmin.R;
import com.example.workmateadmin.modelo.Empresa;
import com.example.workmateadmin.modelo.Proyecto;
import com.example.workmateadmin.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ProjectImagesFragment extends Fragment {
    private Proyecto proy;
    private RecyclerView imagesRec, finishedImages;
    private String projectId;
    private SwipeRefreshLayout swipe;
    private TextView finishedLabel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_projects_images, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipe = view.findViewById(R.id.swipeRefreshProjectsImagesList);
        imagesRec = view.findViewById(R.id.recViewProjectDevelopmentImages);
        imagesRec.setLayoutManager(new LinearLayoutManager(getContext()));
        finishedImages = view.findViewById(R.id.recViewProjectFinishedImages);
        finishedLabel = view.findViewById(R.id.textViewProjectImageFinishedGalery);
        finishedImages.setLayoutManager(new LinearLayoutManager(getContext()));
        // Obtiene el id del proyecto de los argumentos
        if( getArguments() != null ){
            projectId = getArguments().getString("id");
            loadData();
            // Si tiene el id, le asigna el listener
            swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadData();
                }
            });
        } else {
            swipe.setEnabled(false);
        }
    }
    // Carga los datos del proyecto y obtiene las imagenes
    private void loadData(){
        FirebaseFirestore.getInstance().collection("projects").document(projectId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult() != null){
                    proy = task.getResult().toObject(Proyecto.class);
                    // Si no existe el proyecto, vuelve a la pantalla anterior
                    if(proy != null && getContext() != null) {
                        if (proy.getImagenesTerminado() != null && !proy.getImagenesTerminado().isEmpty()){
                            finishedImages.setAdapter(new ProjectImagesRecyclerViewAdapter(proy.getImagenesTerminado(), getContext()));
                            finishedLabel.setVisibility(View.VISIBLE);
                        }else{
                            finishedLabel.setVisibility(View.GONE);
                        }
                        if (proy.getImagenes() != null && !proy.getImagenes().isEmpty()){
                            imagesRec.setAdapter(new ProjectImagesRecyclerViewAdapter(proy.getImagenes(), getContext()));
                        }
                        swipe.setRefreshing(false);
                    } else {
                        if(getContext() != null) {
                            Toast.makeText(getContext(), R.string.projectnotfound, Toast.LENGTH_SHORT).show();
                            if (getActivity() != null)
                                getActivity().onBackPressed();
                        }
                    }
                }
            }
        });
    }
}