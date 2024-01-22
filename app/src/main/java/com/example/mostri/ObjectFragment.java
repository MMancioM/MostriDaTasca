package com.example.mostri;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.example.mostri.api.CommunicationController;
import com.example.mostri.model.ObjectActivationResponse;
import com.example.mostri.model.SessionDataRepository;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ObjectFragment extends Fragment {
    public String objectUid;
    private TextView idOggettoTextView, nomeOggettoTextView, tipoOggettoTextView, livelloOggettoTextView, rangeVitaPersaTextView;
    private CircleImageView immagineOggettoImageView;
    String TAG = "AppMostri - ObjectFragment";

    //Metodo onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Metodo onCreateView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Convertire un file XML di layout in un oggetto View
        View rootView = inflater.inflate(R.layout.fragment_object, container, false);

        //Nascondo i bottoni
        AtomicReference<LinearLayout> bottomButtonsLayout = new AtomicReference<>(requireActivity().findViewById(R.id.bottom_buttons_layout));
        bottomButtonsLayout.get().setVisibility(View.GONE);

        AtomicReference<LinearLayout> buttonsZoom = new AtomicReference<>(requireActivity().findViewById(R.id.buttons_zoom));
        buttonsZoom.get().setVisibility(View.GONE);

        AtomicReference<LinearLayout> buttonsPagine = new AtomicReference<>(requireActivity().findViewById(R.id.nuovePagine));
        buttonsPagine.get().setVisibility(View.GONE);

        //Ottengo l'UID dell'oggetto
        if (getArguments() != null) {
            this.objectUid = getArguments().getString("objectUid", "");
        }

        //Chiamata al metodo per ottenere l'oggetto
        objectCall();

        //Riferimenti agli elementi del layout
        idOggettoTextView = rootView.findViewById(R.id.idOggetto);
        nomeOggettoTextView = rootView.findViewById(R.id.nomeOggetto);
        tipoOggettoTextView = rootView.findViewById(R.id.tipoOggetto);
        livelloOggettoTextView = rootView.findViewById(R.id.livelloOggetto);
        immagineOggettoImageView = rootView.findViewById(R.id.immagineOggetto);
        rangeVitaPersaTextView = rootView.findViewById(R.id.rangeVitaPersa);

        //Toolbar
        Toolbar toolbar = rootView.findViewById(R.id.toolbar_object);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar); //Imposto la toolbar
            Objects.requireNonNull(activity.getSupportActionBar()).setDisplayShowTitleEnabled(false); //Nascondo il titolo
        }

        ImageView backArrow = rootView.findViewById(R.id.backArrow_object);
        backArrow.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        //Bottone per attivare l'oggetto
        Button activateButton = rootView.findViewById(R.id.activateButton);
        activateButton.setOnClickListener(v -> {
            activateObject();
        });

        Log.d(TAG, "Entrato in ObjectFragment");

        return rootView;
    }

    //Funzione per attivare l'oggetto
    private void activateObject() {
        CommunicationController.activateObject(objectUid, SessionDataRepository.getSid(),
                activate -> {
            Log.d(TAG, "Oggetto attivato" + "\n" +
                    "Died: " + activate.isDied() + "\n" +
                    "Life: " + activate.getLife() + "\n" +
                    "Experience: " + activate.getExperience() + "\n" +
                    "Weapon: " + activate.getWeapon() + "\n" +
                    "Armor: " + activate.getArmor() + "\n" +
                    "Amulet: " + activate.getAmulet() + "\n");
            showInfoPopup(requireContext(), activate);
        }, error -> {
            Log.d(TAG, "Oggetto non attivato: " + error.getMessage());
        });
    }

    //Funzione per ottenere l'oggetto
    private void objectCall(){
        CommunicationController.getObjectById(objectUid, SessionDataRepository.getSid(),
                object -> {
            //Setto i dati dell'oggetto
            idOggettoTextView.setText("ID: " + objectUid);
            nomeOggettoTextView.setText("Nome: " + object.getName());
            tipoOggettoTextView.setText("Tipo: " + object.getType());
            livelloOggettoTextView.setText("Livello: " + object.getLevel());

            if (Objects.equals(object.getType(), "monster")){
                rangeVitaPersaTextView.setVisibility(View.VISIBLE);
                rangeVitaPersaTextView.setText("Potresti perdere tra " + object.getLevel() + " e " + (object.getLevel()*2) + " punti");
                rangeVitaPersaTextView.setTextColor(getResources().getColor(R.color.red));
            } else{
                rangeVitaPersaTextView.setVisibility(View.GONE);
            }

            //Setto l'immagine dell'oggetto
            String base64Image = object.getImage();
            Bitmap bitmap = convertBase64ToBitmap(base64Image);
            immagineOggettoImageView.setImageBitmap(bitmap);

        }, error -> {
            Log.d(TAG, "Oggetto non trovato: " + error.getMessage());
        });
    }

    //Funzione per convertire un'immagine da Base64 a Bitmap
    private Bitmap convertBase64ToBitmap(String base64Image) {
        if (base64Image == null || base64Image.isEmpty()) {
            return BitmapFactory.decodeResource(getResources(), R.mipmap.nophotoperson_round); //Immagine di default
        }

        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT); //Decodifico l'immagine
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); //Ritorno l'immagine
    }

    //Funzione per mostrare un popup con le informazioni sull'oggetto attivato
    private void showInfoPopup(Context context, ObjectActivationResponse activate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Informazioni sull'oggetto attivato");
        builder.setMessage("Died: " + activate.isDied() + "\n" +
                "Life: " + activate.getLife() + "\n" +
                "Experience: " + activate.getExperience() + "\n" +
                "Weapon: " + activate.getWeapon() + "\n" +
                "Armor: " + activate.getArmor() + "\n" +
                "Amulet: " + activate.getAmulet() + "\n");

        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        });

        builder.show();
    }
}

