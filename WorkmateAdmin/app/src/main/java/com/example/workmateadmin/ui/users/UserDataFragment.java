package com.example.workmateadmin.ui.users;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.workmateadmin.MainActivity;
import com.example.workmateadmin.R;
import com.example.workmateadmin.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class UserDataFragment extends Fragment {
    private String idUser;
    private Usuario usuario;
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
        return inflater.inflate(R.layout.fragment_user_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username = view.findViewById(R.id.textViewUserNameProfile);
        mail = view.findViewById(R.id.textViewMailUserProfile);
        profile = view.findViewById(R.id.imageViewUserImageProfile);
        swipe = view.findViewById(R.id.swipeRefreshUserProfile);
        id = view.findViewById(R.id.textViewUserNameIdProfile);
        usernamecode = view.findViewById(R.id.textViewUsernamecodeUserProfile);
        remove = view.findViewById(R.id.buttonRemoveUser);
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
            idUser = getArguments().getString("id");
            loadUser();
        }
    }
    private void loadUser(){
        final DocumentReference userDocument = FirebaseFirestore.getInstance().collection("users").document(idUser);
        userDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    usuario = task.getResult().toObject(Usuario.class);
                    loadUI();
                }
            }
        });
    }
    private void loadUI() {
        if (getView() != null && getContext() != null && getActivity() != null) {
            if (usuario != null) {
                username.setText(usuario.getNombre());
                mail.setText(usuario.getEmail());
                usernamecode.setText(usuario.getUsernamecode());
                id.setText(usuario.getIdUsuario());
                if (usuario.getFoto().equals("default"))
                    Glide.with(getContext())
                            .load(R.drawable.default_user)
                            .circleCrop()
                            .into(profile);
                else
                    Glide.with(getContext())
                            .load(usuario.getFoto())
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
            ((MainActivity) getActivity()).socket.once("removed", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    if(args[0].equals("correct")){
                        Toast.makeText(getContext(), "Eliminado correctamente", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ((MainActivity) getActivity()).socket.emit("removeUser", idUser);
        }
    }
}