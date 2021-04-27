package com.example.workmateadmin.ui.companies;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workmateadmin.R;
import com.example.workmateadmin.modelo.Empresa;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CompaniesImagesFragment extends Fragment {
    private Empresa comp;
    private RecyclerView imagesRec;
    private String companyId;
    private SwipeRefreshLayout swipe;
    private ArrayList<String> images;
    private CompaniesImagesRecyclerViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_companies_images, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        images = new ArrayList<>();
        adapter = new CompaniesImagesRecyclerViewAdapter(images,getContext());
        swipe = view.findViewById(R.id.swipeRefreshCompaniesImagesList);
        imagesRec = view.findViewById(R.id.recViewCompanieImages);
        imagesRec.setLayoutManager(new LinearLayoutManager(getContext()));
        imagesRec.setAdapter(adapter);
        if( getArguments() != null ){
            companyId = getArguments().getString("id");
            loadData();
            // Si tiene id, le asigna el listener
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

    private void loadData(){
        final DocumentReference userDocument = FirebaseFirestore.getInstance().collection("company").document(companyId);
        userDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    comp = task.getResult().toObject(Empresa.class);
                    if(comp != null) {
                        images.clear();
                        images.addAll(comp.getImagenes());
                    }
                    adapter.notifyDataSetChanged();
                    swipe.setRefreshing(false);
                }
            }
        });
    }
}