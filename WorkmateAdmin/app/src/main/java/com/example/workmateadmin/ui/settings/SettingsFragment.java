package com.example.workmateadmin.ui.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.workmateadmin.Login;
import com.example.workmateadmin.MainActivity;
import com.example.workmateadmin.R;

import io.socket.emitter.Emitter;

public class SettingsFragment extends Fragment {
    private EditText lastPassword, newPassword;
    private ImageButton savePassword;
    private Button logout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lastPassword = view.findViewById(R.id.editTextTextLastPassword);
        newPassword = view.findViewById(R.id.editTextTextNewPassword);
        savePassword = view.findViewById(R.id.imageButtonSavePaswordConfig);
        logout = view.findViewById(R.id.buttonLogOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }
    private void changePassword(){
        String lastPasswordSTR = lastPassword.getText().toString();
        String newPasswordSTR = newPassword.getText().toString();
        if(getContext() != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            if (lastPasswordSTR.equals(prefs.getString("password", "admin"))) {
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("password",newPasswordSTR);
                edit.apply();
                Toast.makeText(getContext(), R.string.passwordchangedcorrectly, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), R.string.incorrectpass, Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.logout))
                .setMessage(R.string.logoutmessage)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (getActivity() != null) {
                            // Cierra la actividad, devolviendole al login
                            getActivity().finish();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}