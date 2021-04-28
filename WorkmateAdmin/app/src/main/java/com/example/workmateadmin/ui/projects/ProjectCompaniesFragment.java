package com.example.workmateadmin.ui.projects;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.workmateadmin.MainActivity;
import com.example.workmateadmin.R;
import com.example.workmateadmin.modelo.Empresa;
import com.example.workmateadmin.modelo.Proyecto;
import com.example.workmateadmin.ui.companies.CompaniesRecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ProjectCompaniesFragment extends Fragment implements CompaniesRecyclerViewAdapter.OnCompanyChatClickListener{
    private String idProy;
    private Proyecto proy;
    private ArrayList<Empresa> companies;
    private CompaniesRecyclerViewAdapter adapter;
    //Interfaz
    private RecyclerView recView;
    private SwipeRefreshLayout refresh;
    private int size;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_project_companies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        companies = new ArrayList<>();
        adapter = new CompaniesRecyclerViewAdapter(companies,getContext(),this);
        refresh = view.findViewById(R.id.swipeRefreshProjectsCompaniesList);
        refresh.setEnabled(false);
        recView = view.findViewById(R.id.recViewCompaniesProject);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        recView.setAdapter(adapter);
        if(getArguments() != null) {
            idProy =  getArguments().getString("id");
            loadInfo();
            // Si trae id, añade el listener
            refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadInfo();
                }
            });
        }
    }

    public void loadInfo(){
        companies.clear();
        FirebaseFirestore.getInstance().collection("projects").document(idProy).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult() != null){
                        proy = task.getResult().toObject(Proyecto.class);
                        if(proy != null && proy.getEmpresas() != null && !proy.getEmpresas().isEmpty()) {
                            size= proy.getEmpresas().size();
                            for (String id : proy.getEmpresas()) {
                                FirebaseFirestore.getInstance().collection("company").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult() != null) {
                                                Empresa com = task.getResult().toObject(Empresa.class);
                                                if (com != null) {
                                                    companies.add(com);
                                                    adapter.notifyDataSetChanged();
                                                    if(companies.size() == size){
                                                        refresh.setRefreshing(false);
                                                        refresh.setEnabled(true);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        }else{
                            if (getActivity() != null && getContext() != null) {
                                Toast.makeText(getContext(), R.string.nocompaniesfound, Toast.LENGTH_SHORT).show();
                                ((MainActivity) getActivity()).onBackPressed();
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void goCompany(String c) {
        if(getView() != null) {
            Bundle b = new Bundle();
            b.putString("id", c);
            Navigation.findNavController(getView()).navigate(R.id.action_projectCompaniesFragment_to_companyFragment,b);
        }
    }
}