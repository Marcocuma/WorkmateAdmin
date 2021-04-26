package com.example.workmateadmin.ui.projects;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.workmateadmin.R;
import com.example.workmateadmin.modelo.Empresa;
import com.example.workmateadmin.modelo.Proyecto;
import com.example.workmateadmin.ui.companies.CompaniesRecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.ArrayList;

public class ProjectsFragment extends Fragment implements ProjectsRecyclerViewAdapter.OnProjectClickListener{
    private ArrayList<Proyecto> projectsList, projectsListFill;
    private RecyclerView projectsRec;
    private ProjectsRecyclerViewAdapter adapter;
    private ProgressBar loading;
    private SwipeRefreshLayout swipe;
    private EditText projectname;
    private ImageButton search;
    private boolean isComp;
    private String idTarget;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_projects, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        projectsList = new ArrayList<>();
        projectsListFill = new ArrayList<>();
        projectsRec = view.findViewById(R.id.recViewProjects);
        loading = view.findViewById(R.id.progressBarLoadingProjects);
        projectname = view.findViewById(R.id.editTextTextProjectName);
        search = view.findViewById(R.id.buttonSearchListProjects);
        swipe = view.findViewById(R.id.swipeRefreshProjectsList);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadProjects();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchProy();
            }
        });
        projectsRec.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProjectsRecyclerViewAdapter(projectsList, getContext(), this);
        projectsRec.setAdapter(adapter);
        if( getArguments() != null ){
            idTarget = getArguments().getString("id");
            isComp = getArguments().containsKey("comp");
            loadProjects();
        }
        loadProjects();
    }

    private void loadProjects(){
        // Si es un usuario, busca sus proyectos, si es una empresa, los proyectos en los que aparece
        projectsRec.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        if(!isComp){
            final DocumentReference user = FirebaseFirestore.getInstance().collection("users").document(idTarget);
            FirebaseFirestore.getInstance().collection("projects").orderBy("creado", Query.Direction.DESCENDING).whereEqualTo("usuario", user).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        projectsList.clear();
                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                            projectsList.add(doc.toObject(Proyecto.class));
                        }
                        projectsListFill.clear();
                        projectsListFill.addAll(projectsList);
                        adapter.notifyDataSetChanged();
                        projectsRec.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                        swipe.setRefreshing(false);
                    }
                }
            });
        } else {
            FirebaseFirestore.getInstance().collection("projects").orderBy("creado", Query.Direction.DESCENDING).whereArrayContains("empresas", idTarget).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        projectsList.clear();
                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                            projectsList.add(doc.toObject(Proyecto.class));
                        }
                        projectsListFill.clear();
                        projectsListFill.addAll(projectsList);
                        adapter.notifyDataSetChanged();
                        projectsRec.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                        swipe.setRefreshing(false);
                    }
                }
            });
        }
    }
    private void searchProy(){
        final String name = projectname.getText().toString();
        projectsRec.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        if(!name.isEmpty()){
            projectsList.clear();
            projectsList.addAll(Collections2.filter(projectsListFill, new Predicate<Proyecto>() {
                @Override
                public boolean apply(@NullableDecl Proyecto input) {
                    assert input != null;
                    return input.getPresentacion().toLowerCase().replace(" ","").contains(name.toLowerCase().replace(" ",""));
                }
            }));
        } else {
            projectsList.clear();
            projectsList.addAll(projectsListFill);
        }
        loading.setVisibility(View.GONE);
        projectsRec.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void goDetail(Proyecto p) {
        if(getView() != null) {
            Bundle b = new Bundle();
            b.putString("proy", p.getIdProyecto());
            Navigation.findNavController(getView()).navigate(R.id.action_navigation_projects_to_detailsProjectFragment,b);
        }
    }
}