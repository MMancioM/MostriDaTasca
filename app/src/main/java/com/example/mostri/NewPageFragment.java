package com.example.mostri;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.mostri.api.CommunicationController;
import com.example.mostri.api.UpdateUser;
import com.example.mostri.database.StoredUser;
import com.example.mostri.database.StoredUserRepository;
import com.example.mostri.model.SessionDataRepository;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewPageFragment extends Fragment {
    private String uid, sid;
    private Context context;
    private View itemView;
    String TAG = "AppMostri - NuovaPaginaFragment";

    //Metodo onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Metodo onCreateView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Convertire un file XML di layout in un oggetto View
        View rootView = inflater.inflate(R.layout.fragment_new_page, container, false);

        //Recupero il contesto
        context = rootView.getContext();

        //Nascondo i bottoni
        AtomicReference<LinearLayout> bottomButtonsLayout = new AtomicReference<>(requireActivity().findViewById(R.id.bottom_buttons_layout));
        bottomButtonsLayout.get().setVisibility(View.GONE);

        AtomicReference<LinearLayout> buttonsZoom = new AtomicReference<>(requireActivity().findViewById(R.id.buttons_zoom));
        buttonsZoom.get().setVisibility(View.GONE);

        AtomicReference<LinearLayout> buttonsPagine = new AtomicReference<>(requireActivity().findViewById(R.id.nuovePagine));
        buttonsPagine.get().setVisibility(View.GONE);

        // Chiamata al metodo per inizializzare le view
        initViews(rootView);

        // Toolbar
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            Objects.requireNonNull(activity.getSupportActionBar()).setDisplayShowTitleEnabled(false);
        }

        // ImageView e pulsante di ritorno
        ImageView backArrow = rootView.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            startActivity(intent);
        });

        // Riferimenti agli EditText
        EditText nomeEditText = rootView.findViewById(R.id.editTextText2);
        EditText cognomeEditText = rootView.findViewById(R.id.editTextText3);
        EditText emailEditText = rootView.findViewById(R.id.editTextTextEmailAddress);
        EditText dataNascitaEditText = rootView.findViewById(R.id.editTextDate);

        // Riferimento al pulsante
        Button inviaDatiButton = rootView.findViewById(R.id.button);
        inviaDatiButton.setOnClickListener(v -> {
            // Ottenere i valori dagli EditText
            String nome = nomeEditText.getText().toString();
            String cognome = cognomeEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String dataNascita = dataNascitaEditText.getText().toString();

            // Log dei dati
            Log.d(TAG, "Nome: " + nome);
            Log.d(TAG, "Cognome: " + cognome);
            Log.d(TAG, "Email: " + email);
            Log.d(TAG, "Data di nascita: " + dataNascita);

            // Aggiungi qui la logica per inviare i dati dove vuoi
        });

        Log.d(TAG, "Entrato in profileFragment");

        return rootView;
    }

    //Funzione per inizializzare le view
    private void initViews(View rootView) {
        this.sid = SessionDataRepository.getSid();
        this.uid = SessionDataRepository.getUid();
        this.itemView = rootView;

    }
}