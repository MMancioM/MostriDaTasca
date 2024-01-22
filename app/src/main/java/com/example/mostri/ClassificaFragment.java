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
import android.widget.ProgressBar;

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

public class ClassificaFragment extends Fragment {
    private static final String TAG = "AppMostri - ClassificaFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Convertire un file XML di layout in un oggetto View
        View root = inflater.inflate(R.layout.fragment_classifica, container, false);

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
            activity.setSupportActionBar(toolbar);
            Objects.requireNonNull(activity.getSupportActionBar()).setDisplayShowTitleEnabled(false);
        }

        ImageView backArrow = root.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            startActivity(intent);
        });

        //Riferimento alla RecyclerView
        RecyclerView userRecyclerView = root.findViewById(R.id.userRecyclerView);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext())); //Imposto il LayoutManager

        //Riferimento all'Adapter
        ClassificaAdapter classificaAdapter = new ClassificaAdapter(new ArrayList<>(), getContext(),
                rankingUser -> {
            SharedUserFragment sharedUserFragment = new SharedUserFragment(); //Creo un nuovo fragment

            //Passo i dati dell'utente all'adapter
            Bundle bundle = new Bundle();
            bundle.putString("userUid", String.valueOf(rankingUser.getUid()));
            sharedUserFragment.setArguments(bundle);

            //Sostituisco il fragment corrente con quello appena creato
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, sharedUserFragment)
                    .addToBackStack(null)
                    .commit();
        });

        //Setto l'adapter alla RecyclerView
        userRecyclerView.setAdapter(classificaAdapter);

        //Chiamata per ottenere la classifica
        CommunicationController.getRanking(SessionDataRepository.getSid(),
                rankingResponses -> {
                classificaAdapter.setUserList(rankingResponses);
                classificaAdapter.notifyDataSetChanged();
            },
            error -> Log.d(TAG,"Errore nella chiamata getRanking")
        );

        Log.d(TAG, "Entrato in ProfileFragment");

        return root;
    }
}