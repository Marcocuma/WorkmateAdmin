package com.example.workmateadmin.ui.companies;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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
import com.example.workmateadmin.modelo.Empresa;
import com.example.workmateadmin.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import io.socket.emitter.Emitter;

public class CompanyFragment extends Fragment {
    private String idCompany;
    private Empresa comp;
    private TextView username, mail, usernamecode, id, town,province,address, description;
    private ImageView profile;
    private SwipeRefreshLayout swipe;
    private Button openmaps;
    private ChipGroup categoriesGroup;
    private ImageButton remove, seeGalery;
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
        return inflater.inflate(R.layout.fragment_company, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deleted = false;
        username = view.findViewById(R.id.textViewCompanyNameProfile);
        mail = view.findViewById(R.id.textViewMailCompanyProfile);
        profile = view.findViewById(R.id.imageViewCompanyLogoProfile);
        swipe = view.findViewById(R.id.swipeRefreshCompanyProfile);
        id = view.findViewById(R.id.textViewCompanyNameIdProfile);
        usernamecode = view.findViewById(R.id.textViewUsernamecodeCompanyProfile);
        remove = view.findViewById(R.id.buttonRemoveCompany);
        seeGalery = view.findViewById(R.id.buttonSeeImagesCompanyFragment);
        seeProjects = view.findViewById(R.id.buttonSeeProjectsCompany);
        address = view.findViewById(R.id.editTextTextAddressDetailsCompany);
        town = view.findViewById(R.id.editTextTextTownDetailsCompany);
        province = view.findViewById(R.id.editTextTextProvinceDetailsCompany);
        openmaps = view.findViewById(R.id.buttonOpenInMapsDetailsCompany);
        description = view.findViewById(R.id.textViewDescriptionCompanyProfile);
        categoriesGroup = view.findViewById(R.id.chipGroupComapnyDetailsProject);
        // Redirecciona a la ventana de proyectos, pasando como parametro el id de la empresa y el boolean
        // que indica que se esta pidiendo los proyectos de una empresa
        seeProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getView() != null) {
                    Bundle b = new Bundle();
                    b.putString("id", comp.getIdEmpresa());
                    b.putBoolean("comp", true);
                    Navigation.findNavController(getView()).navigate(R.id.action_companyFragment_to_navigation_projects,b);
                }
            }
        });
        // Redirecciona a la galeria de imagenes pasando el id de la empresa
        seeGalery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getView() != null && comp != null) {
                    Bundle b = new Bundle();
                    b.putString("id", comp.getIdEmpresa());
                    Navigation.findNavController(getView()).navigate(R.id.action_companyFragment_to_companiesImagesFragment,b);
                }
            }
        });
        // Si esta eliminada, oculta el boton de ver galeria
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
        // Obtiene el id y el boolean que indica si esta eliminado de los argumentos
        if( getArguments() != null ){
            idCompany = getArguments().getString("id");
            deleted = getArguments().containsKey("deleted");
            loadUser();
        }
        if(deleted){
            seeGalery.setVisibility(View.GONE);
        }
    }
    private void loadUser(){
        // Si no esta borrado, lo coge de la colleccion de las empresas, si lo esta de las borradas
        String path = "company";
        if(deleted)
            path = "deletedCompanies";
        final DocumentReference userDocument = FirebaseFirestore.getInstance().collection(path).document(idCompany);
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

    //Carga la interfaz con los datos obtenidos de la base de datos
    private void loadUI() {
        if (getView() != null && getContext() != null && getActivity() != null) {
            if (comp != null) {
                username.setText(comp.getNombre());
                mail.setText(comp.getEmail());
                usernamecode.setText(comp.getUsernamecode());
                id.setText(comp.getIdEmpresa());
                if (comp.getLogo().equals("Default") || deleted)
                    Glide.with(getContext())
                            .load(R.drawable.default_empresa)
                            .circleCrop()
                            .into(profile);
                else
                    Glide.with(getContext())
                            .load(comp.getLogo())
                            .circleCrop()
                            .into(profile);
                address.setText(comp.getDireccion());
                town.setText(comp.getLocalidad());
                province.setText(comp.getProvincia());
                // Si la empresa tiene categorias, las muestro
                if (comp.getCategorias() != null) {
                    categoriesGroup.removeAllViews();
                    for (String cat : comp.getCategorias()) {
                        Chip chip = new Chip(getContext());
                        chip.setText(cat);
                        categoriesGroup.addView(chip);
                    }
                }
                // Si tiene localizacion, le da funcionalidad al boton de abrir en mapa
                if(comp.getDireccionGMaps() != null && !comp.getDireccionGMaps().isEmpty()){
                    openmaps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openInMaps();
                        }
                    });
                } else {
                    openmaps.setVisibility(View.GONE);
                }
                description.setText(comp.getDescripcion());
                swipe.setRefreshing(false);
            } else {
                Toast.makeText(getContext(),R.string.usernotfound, Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        }
    }
    // Abre la localizacion de la empresa en un mapa externo
    private void openInMaps(){
        if(getContext() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.externalmap))
                    .setMessage(R.string.openmapintentmessage)
                    .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("geo:0,0?q=" + comp.getDireccionGMaps()));
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(R.string.cancel, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    // Envia la solicitud de eliminar la empresa al servidor, esperando su respuesta
    private void remove(){
        if(getContext() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.delete))
                    .setMessage(R.string.deletemessage)
                    .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Compruebo que este conectado al servidor, si no cierra la ventana principal devolviendolo al login y le muestra el mensaje de error
                            if(((MainActivity) getActivity()).socket.connected()){
                                if (getActivity() != null) {
                                    // Comprueba de que base de datos tiene que borrarlo para diferenciar la peticion del server
                                    // Muesta el mensaje de que se ha eliminado correctamente cuando el servidor termine de hacer la tarea
                                    if(!deleted) {
                                        ((MainActivity) getActivity()).socket.once("removedCompany", new Emitter.Listener() {
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
                                        ((MainActivity) getActivity()).socket.emit("removeCompany", idCompany, ((MainActivity) getActivity()).socket.id());
                                    } else {
                                        ((MainActivity) getActivity()).socket.once("removedDeletedCompany", new Emitter.Listener() {
                                            @Override
                                            public void call(Object... args) {
                                                if (args[0].equals("correct") && getActivity() != null) {
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getActivity(), R.string.removedcorrect, Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                } else if (args[0].equals("incorrect") && getActivity() != null) {
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getActivity(), R.string.removedincorrect, Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                        ((MainActivity) getActivity()).socket.emit("removeDeletedCompany", idCompany, ((MainActivity) getActivity()).socket.id());
                                    }
                                }
                            } else {
                                if (getContext() != null) {
                                    Toast.makeText(getContext(), R.string.errorserver, Toast.LENGTH_SHORT).show();
                                    if (getActivity() != null)
                                        getActivity().finish();
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