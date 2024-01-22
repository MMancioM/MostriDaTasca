package com.example.mostri;

import com.example.mostri.model.RankingResponse;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ClassificaViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView picture;
    public TextView name, number, puntiExp;
    public final static String TAG = "AppMostri - ClassificaViewHolder";

    //Costruttore
    public ClassificaViewHolder(@NonNull View itemView) {
        super(itemView);
        number = itemView.findViewById(R.id.numeroRanking);
        picture = itemView.findViewById(R.id.picture);
        name = itemView.findViewById(R.id.name);
        puntiExp = itemView.findViewById((R.id.puntiEsp));
    }

    //Funzione per aggiornare i dati dell'utente
    public void updateContent(RankingResponse rankingUser, int posizione) {
        name.setText(rankingUser.getName());
        number.setText(String.valueOf(posizione+1));
        puntiExp.setText(String.valueOf(rankingUser.getExperience()));

        //Se l'utente ha una foto, la visualizzo
        if (rankingUser.getPicture() != null) {
            try {
                String pictureUser = rankingUser.getPicture();
                byte[] decodedString = Base64.decode(pictureUser, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                picture.setImageBitmap(decodedByte);
            } catch (Exception e) {
                Log.d(TAG, "Errore immagine dell'oggetto" + e);
                picture.setImageResource(R.mipmap.nophotoperson_round); // Imposta un'immagine predefinita in caso di errore
            }
        } else {
            picture.setImageResource(R.mipmap.nophotoperson_round); // Imposta un'immagine predefinita se l'oggetto non ha un'immagine
        }
    }
}
