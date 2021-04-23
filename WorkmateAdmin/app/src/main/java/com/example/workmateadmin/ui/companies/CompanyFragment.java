package com.example.workmateadmin.ui.companies;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.workmateadmin.MainActivity;
import com.example.workmateadmin.R;
import com.example.workmateadmin.modelo.Empresa;
import com.example.workmateadmin.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import io.socket.emitter.Emitter;

public class CompanyFragment extends Fragment {
    private String idCompany;
    private Empresa comp;
    private TextView username, mail, usernamecode, id;
    private ImageView profile;
    private SwipeRefreshLayout swipe;
    private ImageButton remove;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username = view.findViewById(R.id.textViewCompanyNameProfile);
        mail = view.findViewById(R.id.textViewMailCompanyProfile);
        profile = view.findViewById(R.id.imageViewCompanyLogoProfile);
        swipe = view.findViewById(R.id.swipeRefreshCompanyProfile);
        id = view.findViewById(R.id.textViewCompanyNameIdProfile);
        usernamecode = view.findViewById(R.id.textViewUsernamecodeCompanyProfile);
        remove = view.findViewById(R.id.buttonRemoveCompany);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadUser();
            }
        });
        if( getArguments() != null ){
            idCompany = getArguments().getString("id");
            loadUser();
        }
    }
    private void loadUser(){
        final DocumentReference userDocument = FirebaseFirestore.getInstance().collection("company").document(idCompany);
        userDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    comp = task.getResult().toObject(Empresa.class);
                    loadUI();
                }
            }
        });
    }
    private void loadUI() {
        if (getView() != null && getContext() != null && getActivity() != null) {
            if (comp != null) {
                username.setText(comp.getNombre());
                mail.setText(comp.getEmail());
                usernamecode.setText(comp.getUsernamecode());
                id.setText(comp.getIdEmpresa());
                if (comp.getLogo().equals("default"))
                    Glide.with(getContext())
                            .load(R.drawable.default_empresa)
                            .circleCrop()
                            .into(profile);
                else
                    Glide.with(getContext())
                            .load(comp.getLogo())
                            .circleCrop()
                            .into(profile);
                swipe.setRefreshing(false);
            } else {
                Toast.makeText(getContext(),R.string.usernotfound, Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        }
    }
    private void remove(){
        if(getActivity() != null) {
            // Muesta el mensaje de que se ha eliminado correctamente cuando el servidor termine de hacer la tarea
            ((MainActivity) getActivity()).socket.once("removedCompany", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println(args[0]);
                    if(args[0].equals("correct") && getActivity() != null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Eliminado correctamente", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
            ((MainActivity) getActivity()).socket.emit("removeCompany", idCompany, ((MainActivity) getActivity()).socket.id());
        }
    }
}