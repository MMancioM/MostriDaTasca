package com.example.mostri;

import com.example.mostri.api.CommunicationController;
import com.example.mostri.model.SessionDataRepository;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ListaOggettiFragment extends Fragment {
    private static final String TAG = "AppMostri - ListaOggettiFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Convertire un file XML di layout in un oggetto View
        View root = inflater.inflate(R.layout.fragment_listaoggetti, container, false);

        //Nascondo i bottoni
        AtomicReference<LinearLayout> bottomButtonsLayout = new AtomicReference<>(requireActivity().findViewById(R.id.bottom_buttons_layout));
        bottomButtonsLayout.get().setVisibility(View.GONE);

        AtomicReference<LinearLayout> buttonsZoom = new AtomicReference<>(requireActivity().findViewById(R.id.buttons_zoom));
        buttonsZoom.get().setVisibility(View.GONE);

        AtomicReference<LinearLayout> buttonsPagine = new AtomicReference<>(requireActivity().findViewById(R.id.nuovePagine));
        buttonsPagine.get().setVisibility(View.GONE);

        //Toolbar
        Toolbar toolbar = root.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar); //Imposto la toolbar
            Objects.requireNonNull(activity.getSupportActionBar()).setDisplayShowTitleEnabled(false); //Nascondo il titolo
        }

        ImageView backArrow = root.findViewById(R.id.backArrowOggetti);
        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), MainActivity.class); //Torno alla MainActivity
            startActivity(intent); //Avvio l'activity
        });

        //Riferimento alla RecyclerView
        RecyclerView userRecyclerView = root.findViewById(R.id.userRecyclerView);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext())); //Imposto il LayoutManager

        //Textview se non ci sono oggetti nelle vicinanze
        TextView noObjectsTextView = root.findViewById(R.id.noObjectsTextView);

        //Bundle per ottenere latitudine e longitudine degli oggetti
        Bundle bundle = getArguments();
        if (bundle != null) {
            String lat = bundle.getString("lat");
            String lon = bundle.getString("lon");

            //Riferimento all'Adapter
            ListaOggettiAdapter listaOggettiAdapter = new ListaOggettiAdapter(new ArrayList<>(), getContext(),
                    objectUid -> {
                ObjectFragment objectFragment = new ObjectFragment(); //Creo un nuovo fragment

                //Passo i dati dell'oggetto al fragment
                Bundle fragmentBundle = new Bundle();
                fragmentBundle.putString("objectUid", objectUid);
                objectFragment.setArguments(fragmentBundle);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, objectFragment)
                        .addToBackStack(null)
                        .commit();
            }, lat, lon);

            //Setto l'adapter alla RecyclerView
            userRecyclerView.setAdapter(listaOggettiAdapter);

            //Chiamata per ottenere la lista degli oggetti vicini
            CommunicationController.getObjects(SessionDataRepository.getSid(), lat, lon,
                    objectsResponses -> {
                        // Verifica se ci sono oggetti nella lista
                        if (objectsResponses.isEmpty()) {
                            // Se la lista è vuota, mostra il TextView
                            noObjectsTextView.setVisibility(View.VISIBLE);
                        } else {
                            // Se la lista non è vuota, nascondi il TextView
                            noObjectsTextView.setVisibility(View.GONE);

                            // Aggiorna la lista degli oggetti
                            listaOggettiAdapter.setObjectList(objectsResponses);
                            listaOggettiAdapter.notifyDataSetChanged();
                        }
                    },
                    error -> {
                        Log.d(TAG, "ERRORE GETRANKING");
                        // In caso di errore, puoi gestire la visibilità del TextView di conseguenza
                        noObjectsTextView.setVisibility(View.VISIBLE);
                    }
            );
        }

        Log.d(TAG, "Entrato in ListaOggettiFragment");

        return root;
    }
}
