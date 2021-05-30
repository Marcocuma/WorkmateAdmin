package com.example.workmateadmin.ui.companies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.workmateadmin.modelo.Usuario;
import com.example.workmateadmin.ui.users.UsersRecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.ArrayList;

public class CompaniesFragment extends Fragment implements CompaniesRecyclerViewAdapter.OnCompanyChatClickListener {

    private ArrayList<Empresa> companyList, companyListFill;
    private RecyclerView companiesRec;
    private CompaniesRecyclerViewAdapter adapter;
    private ProgressBar loading;
    private SwipeRefreshLayout swipe;
    private EditText compname;
    private ImageButton search;
    private Button goToDeleted;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_companies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        companyList = new ArrayList<>();
        companyListFill = new ArrayList<>();
        companiesRec = view.findViewById(R.id.recViewCompanies);
        loading = view.findViewById(R.id.progressBarLoadingCompanies);
        compname = view.findViewById(R.id.editTextTextCompanyName);
        search = view.findViewById(R.id.buttonSearchListCompanies);
        swipe = view.findViewById(R.id.swipeRefreshCompaniesList);
        goToDeleted = view.findViewById(R.id.buttonSeeDeletedCompanies);

        goToDeleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDeleted();
            }
        });
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadCompanies();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchComp();
            }
        });
        companiesRec.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CompaniesRecyclerViewAdapter(companyList, getContext(), this);
        companiesRec.setAdapter(adapter);
        loadCompanies();
    }
    // Carga las empresas de la base de datos y las añade a la lista
    public void loadCompanies(){
        companiesRec.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        companyList.clear();
        companyListFill.clear();
        FirebaseFirestore.getInstance().collection("company").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && task.getResult() != null){
                    for (DocumentSnapshot doc : task.getResult().getDocuments()){
                        Empresa comp = doc.toObject(Empresa.class);
                        companyList.add(comp);
                    }
                    companyListFill.addAll(companyList);
                    adapter.notifyDataSetChanged();
                    loading.setVisibility(View.GONE);
                    companiesRec.setVisibility(View.VISIBLE);
                    swipe.setRefreshing(false);
                }
            }
        });
    }
    // Filtra la lista de empresas por el nombre introducido
    // Si no se ha introducido nombre, devuelve la lista inicial
    private void searchComp(){
        final String name = compname.getText().toString();
        companiesRec.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        if(!name.isEmpty()){
            companyList.clear();
            companyList.addAll(Collections2.filter(companyListFill, new Predicate<Empresa>() {
                @Override
                public boolean apply(@NullableDecl Empresa input) {
                    assert input != null;
                    return input.getNombre().toLowerCase().replace(" ","").contains(name.toLowerCase().replace(" ",""));
                }
            }));
        } else {
            companyList.clear();
            companyList.addAll(companyListFill);
        }
        loading.setVisibility(View.GONE);
        companiesRec.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    @Override
    // Redirecciona a la ventana de perfil de empresa
    public void goCompany(String c) {
        if(getView() != null) {
            Bundle b = new Bundle();
            b.putString("id", c);
            Navigation.findNavController(getView()).navigate(R.id.action_navigation_home_to_companyFragment,b);
        }
    }
    // Redirecciona a la lista de empresas eliminadas
    private void goToDeleted(){
        if(getView() != null) {
            Navigation.findNavController(getView()).navigate(R.id.action_navigation_home_to_deletedCompaniesFragment);
        }
    }
}