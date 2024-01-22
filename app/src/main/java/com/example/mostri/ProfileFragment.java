package com.example.mostri;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

public class ProfileFragment extends Fragment {
    private String uid, sid;
    private String picture;
    private TextView nameTextView, lifeTextView, weaponTextView, armorTextView, amuletTextView, experienceTextView;
    private boolean positionShareStatus;
    private CircleImageView immagineProfiloImageView;
    private SwitchMaterial positionShareSwitch;
    private View itemView;
    private Context context;
    String TAG = "AppMostri - ProfileFragment";

    //Metodo onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Metodo onCreateView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Convertire un file XML di layout in un oggetto View
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        //Recupero il contesto
        context = rootView.getContext();

        //Nascondo i bottoni
        AtomicReference<LinearLayout> bottomButtonsLayout = new AtomicReference<>(requireActivity().findViewById(R.id.bottom_buttons_layout));
        bottomButtonsLayout.get().setVisibility(View.GONE);

        AtomicReference<LinearLayout> buttonsZoom = new AtomicReference<>(requireActivity().findViewById(R.id.buttons_zoom));
        buttonsZoom.get().setVisibility(View.GONE);

        AtomicReference<LinearLayout> buttonsPagine = new AtomicReference<>(requireActivity().findViewById(R.id.nuovePagine));
        buttonsPagine.get().setVisibility(View.GONE);

        //Chiamata al metodo per inizializzare le view
        initViews(rootView);

        //Chiamata al metodo per visualizzare i dati
        visualizzaDati();

        //Toolbar
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            Objects.requireNonNull(activity.getSupportActionBar()).setDisplayShowTitleEnabled(false);
        }

        ImageView backArrow = rootView.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            startActivity(intent);
        });

        Log.d(TAG, "Entrato in profileFragment");

        return rootView;
    }

    //Funzione per inizializzare le view
    private void initViews(View rootView) {
        this.sid = SessionDataRepository.getSid();
        this.uid = SessionDataRepository.getUid();
        this.nameTextView = rootView.findViewById(R.id.profileName);
        this.lifeTextView = rootView.findViewById(R.id.profileLife);
        this.weaponTextView = rootView.findViewById(R.id.profileArma);
        this.armorTextView = rootView.findViewById(R.id.profileArmatura);
        this.amuletTextView = rootView.findViewById(R.id.profileAmuleto);
        this.experienceTextView = rootView.findViewById(R.id.profileExp);
        this.positionShareSwitch = rootView.findViewById(R.id.switch1);
        this.immagineProfiloImageView = rootView.findViewById(R.id.immagineProfilo);
        this.itemView = rootView;

        //Switch per condividere la posizione
        positionShareSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                updateUserWithPositionShare(isChecked)
        );

        //Bottone per modificare il nome
        Button editNameButton = rootView.findViewById(R.id.editNameButton);
        editNameButton.setOnClickListener(v -> showNameInputDialog());

        //Bottone per modificare l'immagine di profilo
        Button editProfilePictureButton = rootView.findViewById(R.id.editProfilePictureButton);
        editProfilePictureButton.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder() .build());
        });
    }

    //Funzione per visualizzare i dati
    private void visualizzaDati() {
        CommunicationController.getUserById(uid, sid, profile -> {
            nameTextView.setText("NOME: " + profile.getName());
            lifeTextView.setText("PUNTI VITA: " + profile.getLife());
            experienceTextView.setText("ESPERIENZA: " + profile.getExperience());

            String weaponText = (profile.getWeapon() != null) ? profile.getWeapon() : "non equipaggiata";
            weaponTextView.setText("ARMA: " + weaponText);

            String armorText = (profile.getArmor() != null) ? profile.getArmor() : "non equipaggiata";
            armorTextView.setText("ARMATURA: " + armorText);

            String amuletText = (profile.getAmulet() != 0) ? String.valueOf(profile.getAmulet()) : "non equipaggiato";
            amuletTextView.setText("AMULETO: " + amuletText);

            //Setto lo stato del positionShareSwitch
            positionShareSwitch.setChecked(profile.isPositionshare());

            //Setto l'immagine di profilo
            String base64Image = profile.getPicture();
            Bitmap bitmap = convertBase64ToBitmap(base64Image);
            immagineProfiloImageView.setImageBitmap(bitmap);

            //Inserisco l'utente nel database
            List<StoredUser> storedUsers = new ArrayList<>();
            StoredUser storedUser = new StoredUser();
            storedUser.setUid(profile.getUid());
            storedUser.setProfileVersion(profile.getProfileVersion());
            storedUser.setPicture(profile.getPicture());
            storedUser.setName(profile.getName());
            storedUser.setLife(profile.getLife());
            storedUser.setExperience(profile.getExperience());
            storedUser.setAmulet(profile.getAmulet());
            storedUser.setWeapon(profile.getWeapon());
            storedUser.setArmor(profile.getArmor());
            storedUser.setPositionshare(profile.isPositionshare());

            StoredUserRepository storedUserRepository = StoredUserRepository.getInstance(context.getApplicationContext()); //Recupero l'istanza del repository
            ListenableFuture<StoredUser> futureExistingUser = storedUserRepository.storedUserDao().getUser(Integer.toString(storedUser.getUid())); //Recupero l'utente dal database

            try {
                StoredUser existingUser = Futures.getUnchecked(futureExistingUser); //Capisco se l'utente esiste già nel database

                if (existingUser == null) {
                    storedUsers.add(storedUser);
                    insertUsersInRoomDatabase(storedUsers);
                    Log.d(TAG, "Utente inserito nel DB: " + storedUser.getUid());
                } else if (profile.getProfileVersion() > existingUser.getProfileVersion()) {
                    storedUsers.add(storedUser);
                    insertUsersInRoomDatabase(storedUsers);
                    Log.d(TAG, "Utente aggiornato nel DB: " + storedUser.getUid());
                } else {
                    //Log.d(TAG, "Utente già presente nel DB: " + storedUser.getUid());
                }
            } catch (Exception e) {
                Log.d(TAG, "Errore nel recupero dell'utente dal database: " + e.getMessage());
            }
        }, error -> {
            Log.d(TAG, "Profilo non ricevuto");
        });
    }

    //Funzione per aggiornare l'utente con la condivisione della posizione
    private void updateUserWithPositionShare(boolean positionShare) {
        CommunicationController.updateUser(uid, new UpdateUser(sid, null, null, positionShare),
                userUpdate -> {
                    CommunicationController.getUserById(uid, sid, profile -> {
                                positionShareStatus = profile.isPositionshare();
                                Log.d(TAG, "User updated: " + profile.isPositionshare());
                                visualizzaDati();
                            },
                            error -> {
                                Log.d(TAG, "Error getting user");
                            }
                    );
                },
                throwable -> {
                    Log.d(TAG, "Error updating user");
                }
        );
    }

    //Funzione per mostrare il popup per inserire il nuovo nome
    private void showNameInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
        builder.setTitle("Modifica Nome");

        final EditText input = new EditText(itemView.getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT); //Permette di inserire solo testo
        builder.setView(input); //Setto l'editText

        builder.setPositiveButton("OK", (dialog, which) -> {
            String newName = input.getText().toString(); //Recupero il nuovo nome

            //Controllo che il nome non sia maggiore di 15 caratteri
            if (newName.length() > 15) {
                Toast.makeText(itemView.getContext(), "Il nome deve essere lungo al massimo 15 caratteri", Toast.LENGTH_SHORT).show();
            } else {
                updateUserName(newName);
            }
        });
        builder.setNegativeButton("Annulla", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    //Funzione per ottenere lo stato della condivisione della posizione
    public boolean getPositionShareStatus() {
        return positionShareStatus;
    }

    //Funzione per aggiornare il nome dell'utente
    private void updateUserName(String newName) {
        CommunicationController.updateUser(uid, new UpdateUser(sid, newName, null, getPositionShareStatus()),
                userUpdate -> {
                    CommunicationController.getUserById(uid, sid,
                            profile -> {
                                Log.d(TAG, "Utenet aggiornato con nuovo nome: " + profile.getName());
                                visualizzaDati();
                            },
                            error -> {
                                Log.d(TAG, "Errore nel recupero dell'utente da getUserById" + error.getMessage());
                            }
                    );
                },
                throwable -> {
                    Log.d(TAG, "Errore nell'aggiornamento dell'utente" + throwable.getMessage());
                }
        );
    }

    //Funzione per convertire un'immagine da Base64 a Bitmap
    private Bitmap convertBase64ToBitmap(String base64Image) {
        if (base64Image == null || base64Image.isEmpty()) {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.nophotoperson_round); //Immagine di default
        }

        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT); //Decodifico l'immagine
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); //Ritorno l'immagine
    }

    //Funzione per imagepicker
    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(
            new ActivityResultContracts.PickVisualMedia(),
            uri -> {
                //Se l'immagine non è nulla
                if (uri != null) {
                    try {
                        //Da URI a Bitmap
                        final InputStream imageStream = requireActivity().getContentResolver().openInputStream(uri); //Recupero l'immagine
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream); //Decodifico l'immagine

                        //Da Bitmap a Base64
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //Comprimo l'immagine
                        byte[] byteArray = baos.toByteArray(); //Converto l'immagine in un array di byte
                        String imageToBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT); //Converto l'array di byte in una stringa Base64

                        //Controllo che l'immagine non sia troppo grande
                        if (imageToBase64.length() > 137000) {
                            AlertDialog.Builder imageDialog = new AlertDialog.Builder(getContext());

                            imageDialog.setTitle("Modifica il tuo profilo");
                            imageDialog.setMessage("L'immagine che hai selezionato è troppo grande");
                            imageDialog.setPositiveButton("Scegli", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    pickMedia.launch(new PickVisualMediaRequest.Builder().build()); //Rilancio l'immagine
                                }
                            });
                            imageDialog.setNegativeButton("Annulla", (dialog, id) -> dialog.cancel());
                            AlertDialog alert = imageDialog.create();
                            alert.show();
                        } else {
                            setPicture(imageToBase64);
                            CircleImageView picture = requireView().findViewById(R.id.immagineProfilo); //Recupero l'immagine
                            picture.setImageBitmap(selectedImage); //Setto l'immagine
                            CommunicationController.updateUser(uid, new UpdateUser(sid, null, getPicture(), getPositionShareStatus()),
                                    userUpdate -> {
                                        Log.d(TAG,"Nuova immagine di profilo inserita");
                                    },
                                    e -> {
                                        Log.d(TAG, "Errore nell'aggiornamento dell'immagine di profilo" + e.getMessage());
                                    }
                            );

                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace(); //Stampo l'errore
                    }
                }
            }
    );

    //Getter e setter per l'immagine di profilo
    public void setPicture(String picture) { this.picture = picture; }
    public String getPicture() { return picture; }

    //Funzione per inserire l'utente nel database
    private void insertUsersInRoomDatabase(List<StoredUser> storedUsers) {
        StoredUserRepository storedUserRepository = StoredUserRepository.getInstance(context.getApplicationContext()); //Recupero l'istanza del repository

        Executor executor = Executors.newSingleThreadExecutor(); //Creo un executor
        executor.execute(() -> {
            storedUserRepository.storedUserDao().insertAll(storedUsers.toArray(new StoredUser[0])); //Inserisco l'utente nel database
        });
    }
}