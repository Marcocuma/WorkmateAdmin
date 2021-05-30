package com.example.workmateadmin.ui.users;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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
    private Button seeProjects;
    private boolean deleted;


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
        seeProjects = view.findViewById(R.id.buttonSeeProjectsUser);
        deleted = false;
        // Redirecciona a la lista de proyectos de este usuario pasandole el id
        seeProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getView() != null) {
                    Bundle b = new Bundle();
                    b.putString("id", usuario.getIdUsuario());
                    Navigation.findNavController(getView()).navigate(R.id.action_userDataFragment_to_navigation_projects,b);
                }
            }
        });
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
            // Nos ayudara a comprobar que metodo del servidor debemos llamar al borrarlo y de que base de datos hay que cargarlo
            deleted = getArguments().containsKey("deleted");
            loadUser();
        }
    }
    // Carga el detalle del usuario, comprobando si el usuario es eliminado o no
    private void loadUser(){
        String path = "users";
        if(deleted){
            // Si esta eliminado, obtiene el usuario de la siguiente ruta
            path = "deletedUsers";
        }
        final DocumentReference userDocument = FirebaseFirestore.getInstance().collection(path).document(idUser);
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
    // Carga la interfaz con los datos
    private void loadUI() {
        if (getView() != null && getContext() != null && getActivity() != null) {
            if (usuario != null) {
                username.setText(usuario.getNombre());
                mail.setText(usuario.getEmail());
                usernamecode.setText(usuario.getUsernamecode());
                id.setText(usuario.getIdUsuario());
                if (usuario.getFoto().equals("default") || deleted)
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
    // Envia la peticion de eliminancion del usuario al servidor
    // Crea los listener que recibiran la respuesta
    private void remove(){
        if(getContext() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.delete))
                    .setMessage(R.string.deletemessage)
                    .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (getActivity() != null) {
                                // Si el usuario esta eliminado, lo borra de la coleccion de eliminados, si no de la de usuarios
                                // Muesta el mensaje de que se ha eliminado correctamente cuando el servidor termine de hacer la tarea
                                if(deleted){
                                    ((MainActivity) getActivity()).socket.once("removedDeletedUser", new Emitter.Listener() {
                                        @Override
                                        public void call(Object... args) {
                                            if (args[0].equals("correct") && getActivity() != null) {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getActivity(), R.string.removedcorrect, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } else if(args[0].equals("incorrect") && getActivity() != null){
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getActivity(), R.string.removedincorrect, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                    ((MainActivity) getActivity()).socket.emit("removeDeletedUser", idUser, ((MainActivity) getActivity()).socket.id());
                                } else {
                                    ((MainActivity) getActivity()).socket.once("removedUser", new Emitter.Listener() {
                                        @Override
                                        public void call(Object... args) {
                                            if (args[0].equals("correct") && getActivity() != null) {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getActivity(), R.string.removedcorrect, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } else if(args[0].equals("incorrect") && getActivity() != null){
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getActivity(), R.string.removedincorrect, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                    ((MainActivity) getActivity()).socket.emit("removeUser", idUser, ((MainActivity) getActivity()).socket.id());
                                }
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}