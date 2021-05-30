package com.example.workmateadmin.ui.projects;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.workmateadmin.MainActivity;
import com.example.workmateadmin.R;
import com.example.workmateadmin.modelo.Proyecto;
import com.example.workmateadmin.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;

public class DetailsProjectFragment extends Fragment{

    private String idProy;
    private Proyecto proy;
    private Usuario usuario;

    //Interfaz
    private TextView title, userName,town,province,address,description, projectState;
    private ImageView userImage;
    private Button openmaps,seeCompanies, removeProject;
    private ConstraintLayout layoutImagesGallery;
    private ImageSlider galerySlider;
    private Chip square, maxbudget;
    private ProgressBar spinner, progress;
    private NestedScrollView contentView;
    private ChipGroup categoriesGroup;
    private SwipeRefreshLayout refreshLayout;
    private ConstraintLayout userLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_project, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.textViewTitleProjectDetails);
        userName = view.findViewById(R.id.textViewUserNameProjectDetails);
        userImage = view.findViewById(R.id.imageViewUserImageProjectDetails);
        galerySlider = view.findViewById(R.id.galerySliderProject);
        openmaps = view.findViewById(R.id.buttonOpenInMapsDetailsProject);
        square = view.findViewById(R.id.chipSquareMetersDetailsProject);
        maxbudget = view.findViewById(R.id.chipMaxBudgetDetailsProject);
        projectState = view.findViewById(R.id.textViewProjectStateDetailsProject);
        spinner = view.findViewById(R.id.progressBarLoadProjectDetails);
        contentView = view.findViewById(R.id.scrollViewContentProjectDetails);
        categoriesGroup = view.findViewById(R.id.chipGroupCategoriesDetailsProject);
        description = view.findViewById(R.id.editTextTextMultiLineDescriptionDetailsProject);
        seeCompanies = view.findViewById(R.id.buttonProjectDetailsSeeCompanies);
        progress = view.findViewById(R.id.progressBarDetailsProject);
        refreshLayout = view.findViewById(R.id.swiperefreshDetailsProyect);
        address = view.findViewById(R.id.editTextTextAddressDetailsProject);
        town = view.findViewById(R.id.editTextTextTownDetailsProject);
        province = view.findViewById(R.id.editTextTextProvinceDetailsProject);
        userLayout = view.findViewById(R.id.linearLayoutUserLayoutDetailsProject);
        layoutImagesGallery = view.findViewById(R.id.layour_details_gallery_proyect);
        // Va a la galeria pasandole el id
        layoutImagesGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getView() != null) {
                    Bundle b = new Bundle();
                    b.putString("id", proy.getIdProyecto());
                    Navigation.findNavController(getView()).navigate(R.id.action_detailsProjectFragment_to_projectImagesFragment,b);
                }
            }
        });
        removeProject = view.findViewById(R.id.buttonRemoveProject);
        removeProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeProject();
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadInfo();
            }
        });
        // Obtiene el id del proyecto de los argumentos
        if(getArguments() != null) {
            idProy =  getArguments().getString("proy");
            loadInfo();
        }
    }

    // carga el proyecto de la base de datos y su usuario creador
    public void loadInfo(){
        spinner.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.INVISIBLE);
        FirebaseFirestore.getInstance().collection("projects").document(idProy).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult() != null){
                    proy = task.getResult().toObject(Proyecto.class);
                    // Si el usuario esta nulo, crea uno con los datos de un usuario eliminado
                    // Si no existe el proyecto, vuelve a la pantalla anterior
                    if(proy != null) {
                        if (proy.getUsuario() != null)
                            proy.getUsuario().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                    if (task2.isSuccessful() && task2.getResult() != null && task2.getResult().exists())
                                        usuario = task2.getResult().toObject(Usuario.class);
                                    else
                                        usuario = new Usuario("Deleted", "default", "deleted@workmate.com", "", "");
                                    loadUI();
                                }
                            });
                    } else {
                        Toast.makeText(getContext(), R.string.projectnotfound, Toast.LENGTH_SHORT).show();
                        if(getActivity() != null)
                            getActivity().onBackPressed();
                    }
                }
            }
        });
    }

    // Carga al interfaz con los datos del proyecto
    public void loadUI(){
        if(getView() != null && getContext() != null) {
            if(usuario.getIdUsuario().isEmpty()){
                userLayout.setOnClickListener(null);
            }
            title.setText(proy.getPresentacion());
            if (usuario.getFoto().equals("default")) {
                Glide.with(getContext())
                        .load(R.drawable.default_user)
                        .circleCrop()
                        .into(userImage);
            } else {
                Glide.with(getContext())
                        .load(usuario.getFoto())
                        .circleCrop()
                        .into(userImage);
            }
            userName.setText(usuario.getNombre());
            // Si tiene imagenes en la galeria, la rellena con estas, si no
            // Pone un placeholder
            List<SlideModel> slideModels = new ArrayList<>();
            if (proy.getImagenesTerminado() != null && !proy.getImagenesTerminado().isEmpty()) {
                for (String image : proy.getImagenesTerminado()) {
                    slideModels.add(new SlideModel(image, ScaleTypes.CENTER_CROP));
                }
            }
            if(proy.getImagenes() != null && proy.getImagenes().size() > 0) {
                for (String image : proy.getImagenes()) {
                    slideModels.add(new SlideModel(image, ScaleTypes.CENTER_CROP));
                }
            } else {
                slideModels.add(new SlideModel(R.drawable.imagenotfound, ScaleTypes.CENTER_CROP));
            }
            galerySlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);
            // Si no tiene direccion la oculta
            if (proy.getDireccion() != null && !proy.getDireccion().isEmpty())
                address.setText(proy.getDireccion());
            else
                address.setVisibility(View.GONE);
            town.setText(proy.getLocalidad());
            province.setText(proy.getProvincia());
            // Si el proyecto tiene caracteristicas, las muestro
            if (proy.getCaracteristicas() != null) {
                categoriesGroup.removeAllViews();
                for (String cat : proy.getCaracteristicas()) {
                    Chip chip = new Chip(getContext());
                    chip.setText(cat);
                    categoriesGroup.addView(chip);
                }
            }
            // Dependiendo del estado del proyecto, coloco la barra de progreso en un estado y
            // Cambio el texto
            if (proy.getEstado().equals("desarrollo")) {
                progress.setProgress(50);
                projectState.setText(R.string.indevelopment);
            } else if (proy.getEstado().equals("terminado")) {
                progress.setProgress(100);
                projectState.setText(R.string.ended);
            } else {
                projectState.setText(R.string.open);
            }
            description.setText(proy.getDescripcion());
            // Va al fragment de la lista de empresas pasandole el id
            seeCompanies.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getView() != null) {
                        Bundle b = new Bundle();
                        b.putString("id", proy.getIdProyecto());
                        Navigation.findNavController(getView()).navigate(R.id.action_detailsProjectFragment_to_projectCompaniesFragment,b);
                    }
                }
            });
            // Añado un listener al layout del usuario para que vaya a su perfil
            if(!usuario.getIdUsuario().isEmpty()) {
                userLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle b = new Bundle();
                        b.putString("id", usuario.getIdUsuario());
                        Navigation.findNavController(getView()).navigate(R.id.action_detailsProjectFragment_to_userDataFragment, b);
                    }
                });
            }
            // Pongo listener al boton que abre el intent
            if(proy.getDireccionGMaps() != null && !proy.getDireccionGMaps().isEmpty()){
                openmaps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openInMaps();
                    }
                });
            } else {
                openmaps.setVisibility(View.GONE);
            }
            //Muestro el precio max y los metros cuadrados si los tiene, si no lo oculto
            if(proy.getPrecioMax() != null){
                maxbudget.setText(String.format("%d €", proy.getPrecioMax()));
            } else {
                maxbudget.setVisibility(View.GONE);
            }
            if(proy.getMetrosCuadrados() != null){
                square.setText(String.format("%dm2", proy.getMetrosCuadrados()));
            } else {
                square.setVisibility(View.GONE);
            }
            // No puede escribir
            description.setOnClickListener(null);
            spinner.setVisibility(View.INVISIBLE);
            contentView.setVisibility(View.VISIBLE);
            refreshLayout.setRefreshing(false);
        }
    }
    // Abre la localizacion en un mapa externo
    private void openInMaps(){
        if(getContext() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.externalmap))
                    .setMessage(R.string.openmapintentmessage)
                    .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("geo:0,0?q=" + proy.getDireccionGMaps()));
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(R.string.cancel, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    // Manda al servidor la peticion de eliminar el proyecto, y crea un hilo de espera de la respuesta
    private void removeProject(){
        if(getContext() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.delete))
                    .setMessage(R.string.deletemessage)
                    .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (getActivity() != null) {
                                // Muesta el mensaje de que se ha eliminado correctamente cuando el servidor termine de hacer la tarea
                                ((MainActivity) getActivity()).socket.once("removedProject", new Emitter.Listener() {
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
                                ((MainActivity) getActivity()).socket.emit("removeProject", idProy, ((MainActivity) getActivity()).socket.id());
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}