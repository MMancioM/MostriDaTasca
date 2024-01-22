package com.example.mostri;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mostri.api.CommunicationController;
import com.example.mostri.model.ObjectsResponse;
import com.example.mostri.model.SessionDataRepository;
import java.util.List;

public class ListaOggettiAdapter extends RecyclerView.Adapter<ListaOggettiViewHolder> {
    private List<ObjectsResponse> objectList;
    private final LayoutInflater inflater;
    private String myLat, myLon;
    private int distance;
    private final OnItemClickListener onItemClickListener;
    public final static String TAG = "AppMostri - ListaOggettiAdapter";

    //Costruttore
    public ListaOggettiAdapter(List<ObjectsResponse> objectList, Context context, OnItemClickListener listener, String myLat, String myLon) {
        this.objectList = objectList;
        this.inflater = LayoutInflater.from(context);
        this.onItemClickListener = listener;
        this.myLat = myLat;
        this.myLon = myLon;
        maxDistance(); //Calcolo la distanza tra l'utente e l'oggetto
    }

    //Metodo che crea la view
    @NonNull
    @Override
    public ListaOggettiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.lista_oggetti_info, parent, false); //Creo la view
        return new ListaOggettiViewHolder(view); //Ritorno la view
    }

    //Metodo che imposta i dati nella view
    @Override
    public void onBindViewHolder(@NonNull ListaOggettiViewHolder holder, int position) {
        ObjectsResponse objectsUser = objectList.get(position); //Oggetto nella posizione position
            //Posizione dell'oggetto
            double objectLat = Double.parseDouble(objectsUser.getLat());
            double objectLon = Double.parseDouble(objectsUser.getLon());

            //Calcolo la distanza tra l'utente e l'oggetto
            float[] distanceResults = new float[1];
            Location.distanceBetween(Double.parseDouble(myLat), Double.parseDouble(myLon), objectLat, objectLon, distanceResults);

            //Se la distanza è minore o uguale alla distanza massima impostata dall'utente
            //allora fa comparire la mano e rende cliccabile l'oggeto
            if (distanceResults[0] <= distance) {
                holder.portataMano.setVisibility(View.VISIBLE);
                holder.itemView.setOnClickListener(view -> {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(String.valueOf(objectsUser.getId()));
                    }
                });
            } else {
                holder.portataMano.setVisibility(View.GONE);
            }

        //Recupero il nome e l'immagine dell'oggetto
        CommunicationController.getObjectById(String.valueOf(objectsUser.getId()), SessionDataRepository.getSid(),
                response -> {
                    objectList.get(position).setName(response.getName()); //Imposto il nome
                    objectList.get(position).setPicture(response.getImage()); //Imposto l'immagine
                    holder.updateContent(objectsUser, position); //Aggiorno la view
                },
                error -> Log.d(TAG, "Errore nella chiamata: " + error.getMessage())
        );
    }

    //Metodo che ritorna la dimensione della lista
    @Override
    public int getItemCount() {
        return  objectList.size();
    }
    public void setObjectList(List<ObjectsResponse> response) {
        this.objectList = response;
    }

    //Interfaccia che gestisce il click su un oggetto
    public interface OnItemClickListener {
        void onItemClick(String objectUid);
    }

    //Funzione che calcola la distanza tra l'utente e l'oggetto
    private void maxDistance(){
        CommunicationController.getUserById(SessionDataRepository.getUid(), SessionDataRepository.getSid(),
                myProfile -> {
                    CommunicationController.getObjectById(String.valueOf(myProfile.getAmulet()), SessionDataRepository.getSid(),
                            amulet -> {
                                distance = 100 + amulet.getLevel();
                                notifyDataSetChanged();
                            }, t -> {
                                Log.d(TAG, "Errore nel recupero del livello dell'amuleto: " + t.getMessage());
                            });
        },
        t -> {
            Log.d(TAG, "Errore nel recupero della distanza: " + t.getMessage());
        });
    }
}


