package com.example.mostri;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.mostri.api.CommunicationController;
import com.example.mostri.model.SessionDataRepository;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class SharedUserFragmentMap extends Fragment implements OnMapReadyCallback {
    public String userUid;
    private Double lat, lon;
    private TextView idUtenteTextView, nomeUtenteTextView, esperienzaUtenteTextView, vitaUtenteTextView, posizioneCondivisaUtenteTextView;
    private CircleImageView immagineUtenteImageView;
    private MapView posizioneUtenteCondiviso;
    private GoogleMap googleMap;
    String TAG = "AppMostri - SharedUserFragment";

    //Metodo onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Metodo onCreateView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Convertire un file XML di layout in un oggetto View
        View rootView = inflater.inflate(R.layout.fragment_shared_user, container, false);

        //Nascondo i bottoni
        AtomicReference<LinearLayout> bottomButtonsLayout = new AtomicReference<>(requireActivity().findViewById(R.id.bottom_buttons_layout));
        bottomButtonsLayout.get().setVisibility(View.GONE);

        AtomicReference<LinearLayout> buttonsZoom = new AtomicReference<>(requireActivity().findViewById(R.id.buttons_zoom));
        buttonsZoom.get().setVisibility(View.GONE);

        AtomicReference<LinearLayout> buttonsPagine = new AtomicReference<>(requireActivity().findViewById(R.id.nuovePagine));
        buttonsPagine.get().setVisibility(View.GONE);

        //Ottengo l'UID dell'utente
        if (getArguments() != null) {
            this.userUid = getArguments().getString("userUid", "");
        }

        userCall();

        //Inizializzo gli elementi della view
        idUtenteTextView = rootView.findViewById(R.id.idUtente);
        nomeUtenteTextView = rootView.findViewById(R.id.nomeUtente);
        esperienzaUtenteTextView = rootView.findViewById(R.id.esperienzaUtente);
        vitaUtenteTextView = rootView.findViewById(R.id.vitaUtente);
        posizioneCondivisaUtenteTextView = rootView.findViewById(R.id.posizioneCondivisaUtente);
        immagineUtenteImageView = rootView.findViewById(R.id.immagineUtente);
        posizioneUtenteCondiviso = rootView.findViewById(R.id.posizioneUtenteCondiviso);

        //Toolbar
        Toolbar toolbar = rootView.findViewById(R.id.toolbar_object);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            Objects.requireNonNull(activity.getSupportActionBar()).setDisplayShowTitleEnabled(false);
        }

        ImageView backArrow = rootView.findViewById(R.id.backArrow_object);
        backArrow.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
            bottomButtonsLayout.get().setVisibility(View.VISIBLE);
            buttonsZoom.get().setVisibility(View.VISIBLE);
        });

        Log.d(TAG, "Entrato in SharedUserFragment");

        // Inizializza la MapView
        posizioneUtenteCondiviso.onCreate(savedInstanceState);
        posizioneUtenteCondiviso.getMapAsync(this);

        return rootView;
    }

    //Metodo per richiamare gli utenti vicini
    private void userCall(){
        CommunicationController.getUserById(userUid, SessionDataRepository.getSid(),
                user -> {
                    idUtenteTextView.setText("ID: " + userUid);
                    nomeUtenteTextView.setText("Nome: " + user.getName());
                    esperienzaUtenteTextView.setText("Esperienza: " + user.getExperience());
                    vitaUtenteTextView.setText("Vita: " + user.getLife());
                    lat = user.getLat();
                    lon = user.getLon();

                    if (user.isPositionshare()) {
                        posizioneCondivisaUtenteTextView.setText("Posizione condivisa");
                        posizioneUtenteCondiviso.setVisibility(View.VISIBLE);
                    }else{
                        posizioneCondivisaUtenteTextView.setText("Posizione non condivisa");
                        posizioneUtenteCondiviso.setVisibility(View.GONE);
                    }

                    String base64Image = user.getPicture();
                    Bitmap bitmap = convertBase64ToBitmap(base64Image);
                    immagineUtenteImageView.setImageBitmap(bitmap);
                }, error -> {
                    Log.d(TAG, "Errore nel recupero degli oggetti: " + error.getMessage());
                });
    }

    //Metodo per convertire un'immagine da Base64 a Bitmap
    private Bitmap convertBase64ToBitmap(String base64Image) {
        if (base64Image == null || base64Image.isEmpty()) {
            return BitmapFactory.decodeResource(getResources(), R.mipmap.nophotoperson_round); //Immagine di default
        }

        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT); //Converto l'immagine da Base64 a byte[]
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); //Ritorno l'immagine
    }

    // Metodo onMapReady, chiamato quando la mappa è pronta
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        CommunicationController.getUserById(userUid, SessionDataRepository.getSid(),
                user -> {
                    if (lat != null && lon != null) {
                        LatLng userLocation = new LatLng(lat, lon);
                        googleMap.addMarker(new MarkerOptions().position(userLocation).title("Posizione di: " + user.getName()));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));
                    } else {
                        Log.e(TAG, "Latitudine o longitudine sono nulli");
                    }
                }, error -> {
                    Log.d(TAG, "Errore nel recupero degli oggetti: " + error.getMessage());
                });
    }
}

