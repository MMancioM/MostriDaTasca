package com.example.mostri;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mostri.api.CommunicationController;
import com.example.mostri.model.ObjectsResponse;
import com.example.mostri.model.SessionDataRepository;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListaOggettiViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView picture;
    public TextView name, number;
    public ImageView portataMano;
    public final static String TAG = "AppMostri - ListaOggettiViewHolder";

    //Costruttore
    public ListaOggettiViewHolder(@NonNull View itemView) {
        super(itemView);
        number = itemView.findViewById(R.id.numeroRanking); //Numero dell'oggetto
        picture = itemView.findViewById(R.id.pictureOggetto); //Immagine dell'oggetto
        name = itemView.findViewById(R.id.nameOggetto); //Nome dell'oggetto
        portataMano = itemView.findViewById(R.id.portataMano); //Mano che indica che l'oggetto è cliccabile
    }

    //Funzione che imposta i dati nella view
    public void updateContent(ObjectsResponse objectUser, int posizione) {
        name.setText(objectUser.getName()); //Imposto il nome dell'oggetto
        if (objectUser.getPicture() != null) {
            try {
                String pictureUser = objectUser.getPicture();
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
