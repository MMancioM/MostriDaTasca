package com.example.mostri;

import com.example.mostri.api.CommunicationController;
import com.example.mostri.database.StoredUser;
import com.example.mostri.database.StoredUserRepository;
import com.example.mostri.model.RankingResponse;
import com.example.mostri.model.SessionDataRepository;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ClassificaAdapter extends RecyclerView.Adapter<ClassificaViewHolder> {
    private List<RankingResponse> rankingList;
    private final LayoutInflater inflater;
    private final OnUserClickListener onUserClickListener;
    private final Context context;
    public final static String TAG = "AppMostri - ClassificaAdapter";

    //Costruttore
    public ClassificaAdapter(List<RankingResponse> rankingListList, Context context, OnUserClickListener listener) {
        this.context = context;
        this.rankingList = rankingListList;
        this.inflater = LayoutInflater.from(context);
        this.onUserClickListener = listener;
    }

    //Riferimento al ViewHolder
    @NonNull
    @Override
    public ClassificaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.leaderboard_userinfo, parent, false);
        return new ClassificaViewHolder(view);
    }

    //Associa i dati alla view
    @Override
    public void onBindViewHolder(@NonNull ClassificaViewHolder holder, int position) {
        RankingResponse rankingUser = rankingList.get(position); //Ottengo l'utente in posizione position

        //Rendo l'utente cliccabile
        holder.itemView.setOnClickListener(view -> {
            if (onUserClickListener != null) {
                onUserClickListener.onUserClick(rankingUser);
            }
        });

        //Chiamata per ottenere i dati dell'utente
        CommunicationController.getUserById(String.valueOf(rankingUser.getUid()), SessionDataRepository.getSid(),
                response -> {
                    //Recupero i dati dell'utente
                    rankingList.get(position).setName(response.getName());
                    rankingList.get(position).setPicture(response.getPicture());
                    holder.updateContent(rankingUser, position);

                    //Inserisco l'utente nel DB
                    List<StoredUser> storedUsers = new ArrayList<>();
                    StoredUser storedUser = new StoredUser();
                    storedUser.setUid(response.getUid());
                    storedUser.setProfileVersion(response.getProfileVersion());
                    storedUser.setPicture(response.getPicture());
                    storedUser.setName(response.getName());
                    storedUser.setLife(response.getLife());
                    storedUser.setExperience(response.getExperience());
                    storedUser.setAmulet(response.getAmulet());
                    storedUser.setWeapon(response.getWeapon());
                    storedUser.setArmor(response.getArmor());
                    storedUser.setPositionshare(response.isPositionshare());

                    StoredUserRepository storedUserRepository = StoredUserRepository.getInstance(context.getApplicationContext()); //Recupero il database
                    ListenableFuture<StoredUser> futureExistingUser = storedUserRepository.storedUserDao().getUser(Integer.toString(storedUser.getUid())); //Recupero l'utente dal database

                    try {
                        StoredUser existingUser = Futures.getUnchecked(futureExistingUser); //Vedo se l'utente è già presente nel DB

                        if (existingUser == null) {
                            storedUsers.add(storedUser);
                            insertUsersInRoomDatabase(storedUsers);
                            Log.d(TAG, "Utente inserito nel DB: " + storedUser.getUid());
                        } else if (response.getProfileVersion() > existingUser.getProfileVersion()) {
                            storedUsers.add(storedUser);
                            insertUsersInRoomDatabase(storedUsers);
                            Log.d(TAG, "Utente aggiornato nel DB: " + storedUser.getUid());
                        } else {
                            //Log.d(TAG, "Utente già presente nel DB: " + storedUser.getUid());
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "Errore nel recupero dell'utente dal DB: " + e.getMessage());
                    }
                },
                error -> Log.d(TAG, "Errore nella chiamata: " + error.getMessage())
        );
    }

    //Ritorna il numero di elementi nella lista
    @Override
    public int getItemCount() {
        return  rankingList.size();
    }

    //Aggiorna la lista
    public void setUserList(List<RankingResponse> response) {
        this.rankingList = response;
    }

    //Interfaccia per rendere l'utente cliccabile
    public interface OnUserClickListener {
        void onUserClick(RankingResponse rankingUser);
    }

    //Funzione per inserire l'utente nel database
    private void insertUsersInRoomDatabase(List<StoredUser> storedUsers) {
        StoredUserRepository storedUserRepository = StoredUserRepository.getInstance(context.getApplicationContext());

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            storedUserRepository.storedUserDao().insertAll(storedUsers.toArray(new StoredUser[0]));
        });
    }
}
