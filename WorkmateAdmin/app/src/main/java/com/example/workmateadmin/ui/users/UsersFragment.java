package com.example.workmateadmin.ui.users;

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
import com.example.workmateadmin.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.ArrayList;

public class UsersFragment extends Fragment implements UsersRecyclerViewAdapter.OnUserChatClickListener {
    private ArrayList<Usuario> usersList, usersListFill;

    private RecyclerView usersRec;
    private UsersRecyclerViewAdapter adapter;
    private ProgressBar loading;
    private SwipeRefreshLayout swipe;
    private EditText username;
    private ImageButton search;
    private Button goToDeleted;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_users, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usersList = new ArrayList<>();
        usersListFill = new ArrayList<>();
        usersRec = view.findViewById(R.id.recViewUsers);
        loading = view.findViewById(R.id.progressBarLoadingUsers);
        username = view.findViewById(R.id.editTextTextPersonName);
        search = view.findViewById(R.id.buttonSearchListUsers);
        swipe = view.findViewById(R.id.swipeRefreshUsersList);
        goToDeleted = view.findViewById(R.id.buttonSeeDeletedUsers);
        goToDeleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDeletedUsers();
            }
        });
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadUsers();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUser();
            }
        });
        usersRec.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UsersRecyclerViewAdapter(usersList, getContext(), this);
        usersRec.setAdapter(adapter);
        loadUsers();
    }
    // Cargo los usuarios y los añado a la lista
    public void loadUsers(){
        usersRec.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        usersList.clear();
        usersListFill.clear();
        FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
             if(task.isSuccessful() && task.getResult() != null){
                 for (DocumentSnapshot doc : task.getResult().getDocuments()){
                     Usuario user = doc.toObject(Usuario.class);
                     usersList.add(user);
                 }
                 usersListFill.addAll(usersList);
                 adapter.notifyDataSetChanged();
                 loading.setVisibility(View.GONE);
                 usersRec.setVisibility(View.VISIBLE);
                 swipe.setRefreshing(false);
             }
            }
        });
    }
    // Filtra la lista de usuarios por el nombre introducido como parametro
    // Si el nombre esta vacio, deja la lista en el estado inicial
     private void searchUser(){
        final String name = username.getText().toString();
         usersRec.setVisibility(View.GONE);
         loading.setVisibility(View.VISIBLE);
        if(!name.isEmpty()){
            usersList.clear();
            usersList.addAll(Collections2.filter(usersListFill, new Predicate<Usuario>() {
                @Override
                public boolean apply(@NullableDecl Usuario input) {
                    assert input != null;
                    return input.getNombre().toLowerCase().replace(" ","").contains(name.toLowerCase().replace(" ",""));
                }
            }));
        } else {
            usersList.clear();
            usersList.addAll(usersListFill);
        }
         loading.setVisibility(View.GONE);
         usersRec.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
     }
    @Override
    // Redirecciona a la pantalla de detalle del usuario, pasandole el id.
    public void irUser(String c) {
        if(getView() != null) {
            Bundle b = new Bundle();
            b.putString("id", c);
            Navigation.findNavController(getView()).navigate(R.id.action_navigation_users_to_userDataFragment,b);
        }
    }
    // Redirecciona a la pantalla de usuarios eliminados
    private void goToDeletedUsers(){
        if(getView() != null) {
            Navigation.findNavController(getView()).navigate(R.id.action_navigation_users_to_deletedUsersFragment);
        }
    }
}