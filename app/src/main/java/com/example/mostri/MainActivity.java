package com.example.mostri;

import android.Manifest;
import com.example.mostri.api.CommunicationController;
import com.example.mostri.database.StoredUser;
import com.example.mostri.database.StoredUserRepository;
import com.example.mostri.model.ObjectsResponse;
import com.example.mostri.model.SessionDataRepository;
import com.example.mostri.model.UsersShareResponse;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import java.util.concurrent.Executors;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

public class MainActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener {
    public final static String TAG = "AppMostri - MainActivity";
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;
    public int distance;
    public double myLat, myLon;
    public String sid, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Chiamate alle funzioni
        hideSystemUI();
        setContentView(R.layout.activity_main);
        SidUid();
        maxDistance();

        //Rendere i bottoni visibili
        LinearLayout bottomButtonsLayout = findViewById(R.id.bottom_buttons_layout);
        bottomButtonsLayout.setVisibility(View.VISIBLE);

        LinearLayout buttonsZoom = findViewById(R.id.buttons_zoom);
        buttonsZoom.setVisibility(View.VISIBLE);

        //Bottone per profilo
        ImageButton btnProfile = findViewById(R.id.profilo);
        CommunicationController.getUserById(uid, sid, profile -> {
            //Se l'immagine di profilo c'è esegui questo
            if (profile.getPicture() != null) {
                try {
                    String immagine = profile.getPicture(); //Prendo l'immagine di profilo
                    // Converte la stringa Base64 in un Bitmap
                    byte[] decodedImage = Base64.decode(immagine, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);

                    // Utilizza Glide per impostare l'immagine circolare nella ImageButton
                    Glide.with(this)
                            .load(bitmap)
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .apply(new RequestOptions()
                                    .override(210, 210)  // Imposta le dimensioni desiderate
                                    .transform(new CircleCrop()))
                            .into(btnProfile);
                } catch (Exception e) {
                    Log.d(TAG, "Errore immagine dell'oggetto" + e);
                    Glide.with(this)
                            .load(R.mipmap.nophotoperson_round) //Se non c'è l'immagine la sostituisco con quella di default
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .apply(new RequestOptions()
                                    .override(210, 210)  // Imposta le dimensioni desiderate
                                    .transform(new CircleCrop()))
                            .into(btnProfile);
                }
                //Altrimenti esegui questo
            } else {
                Glide.with(this)
                        .load(R.mipmap.nophotoperson_round) //Se non c'è l'immagine la sostituisco con quella di default
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .apply(new RequestOptions()
                                .override(210, 210)  // Imposta le dimensioni desiderate
                                .transform(new CircleCrop()))
                        .into(btnProfile);
            }

            //ProgressBar per punti vita
            ProgressBar progressBar = findViewById(R.id.progressBar);
            int puntiVita = profile.getLife();
            int coloreProgressBar;

            //Setto i colori in base ai punti vita
            if (puntiVita <= 25) {
                coloreProgressBar = Color.RED;
            } else if (puntiVita <= 50) {
                coloreProgressBar = Color.parseColor("#FFA500"); //Arancione
            } else if (puntiVita <= 75) {
                coloreProgressBar = Color.YELLOW;
            } else {
                coloreProgressBar = Color.GREEN;
            }

            //Setto il background della barra
            progressBar.setBackgroundColor(Color.GRAY);
            ClipDrawable clipDrawable = new ClipDrawable(new ColorDrawable(coloreProgressBar), Gravity.START, ClipDrawable.HORIZONTAL);
            progressBar.setProgressDrawable(clipDrawable);
            progressBar.setProgress(puntiVita);
        }, error -> {
            Log.d(TAG, "Profilo non ricevuto");
        });
        btnProfile.setOnClickListener(v -> {
            ProfileFragment profileFragment = new ProfileFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.google_map, profileFragment)
                    .addToBackStack(null)
                    .commit();
        });

        //Bottone per la classifica
        ImageButton btnClassifica = findViewById(R.id.classifica);
        btnClassifica.setOnClickListener(v -> {
            ClassificaFragment classificaFragment = new ClassificaFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.google_map, classificaFragment)
                    .addToBackStack(null)
                    .commit();
        });

        //Bottone per la lista degli oggetti vicini
        ImageButton btnOggetti = findViewById(R.id.listObject);
        btnOggetti.setOnClickListener(v -> {
            ListaOggettiFragment listaOggettiFragment = new ListaOggettiFragment();

            // Creazione di un bundle per passare i dati
            Bundle bundle = new Bundle();
            bundle.putString("lat", String.valueOf(this.myLat));
            bundle.putString("lon", String.valueOf(this.myLon));
            bundle.putInt("chiave_intero", 42);

            // Impostare il bundle come argomento per il fragment
            listaOggettiFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.google_map, listaOggettiFragment)
                    .addToBackStack(null)
                    .commit();
        });

        //Bottone per il refresh della posizione
        Button refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(v -> {
            getCurrentLocation();
            Log.d(TAG, "Posizione aggiornata");
        });

        //Bottone per il refresh della posizione
        Button newPageButton = findViewById(R.id.nuovaPagina);
        newPageButton.setOnClickListener(v -> {
            navigateToNewPage();
            Log.d(TAG, "Passato a nuova pagina");
        });

        //Creazione della mappa
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        requestLocationPermission();

        supportMapFragment.getMapAsync(googleMap -> {
            googleMap.setOnMarkerClickListener(this);
        });

        Log.d(TAG,"Entrato in MainActivity");
    }

    //Funzione per ottenere SID e UID
    public void SidUid() {
        SessionDataRepository.initSidAndUid(this, (sid, uid) -> {
                    this.sid = sid;
                    this.uid = uid;
                },
                t -> {
                    Log.d(TAG, "Sid non ottenuto");
                }
        );
    }

    //Funzione per la richiesta dei permessi di localizzazione
    private void requestLocationPermission() {
        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getCurrentLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    //Funzione per ottenere la posizione corrente
    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> supportMapFragment.getMapAsync(googleMap -> {
            if (location != null) {
                //Ottenimento delle coordinate dell'utente
                myLat = location.getLatitude();
                myLon = location.getLongitude();
                LatLng latLng = new LatLng(myLat, myLon);

                //Aggiunta del marker per la posizione utente
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title("Posizione corrente: " + myLat + ", " + myLon)
                        .zIndex(999);
                googleMap.addMarker(markerOptions);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

                //Aggiunta dei marker personalizzati
                addMarkersForObjects();
                addMarkersForSharedUsers();

                Log.d(TAG, "Coordinate - Latitudine: " + myLat + ", longitudine: " + myLon);

                // Ottenere i riferimenti ai pulsanti di zoom
                Button btnZoomIn = findViewById(R.id.btnZoomIn);
                btnZoomIn.setOnClickListener(v -> {
                    googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                });

                Button btnZoomOut = findViewById(R.id.btnZoomOut);
                btnZoomOut.setOnClickListener(v -> {
                    googleMap.animateCamera(CameraUpdateFactory.zoomOut());
                });
            } else {
                Toast.makeText(MainActivity.this, "Accendi la localizzazione", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    //Funzione per aggiungere i marker degli oggetti nella mappa
    private void addMarkersForObjects() {
        CommunicationController.getObjects(sid, String.valueOf(myLat), String.valueOf(myLon),
                objectsResponseList -> {
                    for (ObjectsResponse object : objectsResponseList) {
                        String objectUid = object.getId();
                        double objectLat = Double.parseDouble(object.getLat());
                        double objectLon = Double.parseDouble(object.getLon());

                        float[] distanceResults = new float[1];
                        Location.distanceBetween(myLat, myLon, objectLat, objectLon, distanceResults);

                        //Se l'oggetto è un mostro allora aggiungo il marker del mostro
                        if (Objects.equals(object.getType(), "monster")){
                            int vectorDrawableResourceId = R.drawable.marker_monster; //cambia solo questo
                            BitmapDescriptor icon = getBitmapDescriptorFromVector(vectorDrawableResourceId);

                            if (distanceResults[0] <= distance) {
                                LatLng objectLatLng = new LatLng(objectLat, objectLon);
                                MarkerOptions objectMarkerOptions = new MarkerOptions()
                                        .position(objectLatLng)
                                        .title("Object ID: " + objectUid)
                                        .icon(icon)
                                        .zIndex(1);

                                supportMapFragment.getMapAsync(googleMap -> {
                                    googleMap.addMarker(objectMarkerOptions);
                                });
                            }

                            //Se l'oggetto è un amuleto allora aggiungo il marker dell'amuleto
                        } else if (Objects.equals(object.getType(), "candy")) {
                            int vectorDrawableResourceId = R.drawable.marker_candy; //cambia solo questo
                            BitmapDescriptor icon = getBitmapDescriptorFromVector(vectorDrawableResourceId);

                            if (distanceResults[0] <= distance) {
                                LatLng objectLatLng = new LatLng(objectLat, objectLon);
                                MarkerOptions objectMarkerOptions = new MarkerOptions()
                                        .position(objectLatLng)
                                        .title("Object ID: " + objectUid)
                                        .icon(icon)
                                        .zIndex(1);

                                supportMapFragment.getMapAsync(googleMap -> {
                                    googleMap.addMarker(objectMarkerOptions);
                                });
                            }
                        //Se l'oggetto è altro allora aggiungo il marker dell'oggetto
                        }else {
                            int vectorDrawableResourceId = R.drawable.marker_sword; //cambia solo questo
                            BitmapDescriptor icon = getBitmapDescriptorFromVector(vectorDrawableResourceId);

                            if (distanceResults[0] <= distance) {
                                LatLng objectLatLng = new LatLng(objectLat, objectLon);
                                MarkerOptions objectMarkerOptions = new MarkerOptions()
                                        .position(objectLatLng)
                                        .title("Object ID: " + objectUid)
                                        .icon(icon)
                                        .zIndex(1);

                                supportMapFragment.getMapAsync(googleMap -> {
                                    googleMap.addMarker(objectMarkerOptions);
                                });
                            }
                        }
                    }
                },
                t -> {
                    Log.d(TAG, "Errore nel recupero degli oggetti: " + t.getMessage());
                });
    }

    //Funzione per aggiungere i marker degli utenti condivisi nella mappa
    private void addMarkersForSharedUsers() {
        CommunicationController.getSharedUsers(sid, String.valueOf(myLat), String.valueOf(myLon),
                usersResponseList -> {
                    for (UsersShareResponse user : usersResponseList) {
                        if (!user.getUid().equals(uid)) {
                            String userUid = user.getUid();
                            double userLat = Double.parseDouble(user.getLat());
                            double userLon = Double.parseDouble(user.getLon());

                            float[] distanceResults = new float[1];
                            Location.distanceBetween(myLat, myLon, userLat, userLon, distanceResults);

                            if (distanceResults[0] <= distance) {
                                LatLng userLatLng = new LatLng(userLat, userLon);

                                int vectorDrawableResourceId = R.drawable.marker_user;

                                BitmapDescriptor icon = getBitmapDescriptorFromVector(vectorDrawableResourceId);

                                MarkerOptions userMarkerOptions = new MarkerOptions()
                                        .position(userLatLng)
                                        .title("User ID: " + userUid)
                                        .icon(icon);

                                supportMapFragment.getMapAsync(googleMap -> {
                                    googleMap.addMarker(userMarkerOptions);
                                });
                            }
                        }

                        //Aggiungo gli utenti al database
                        List<StoredUser> storedUsers = new ArrayList<>();
                        CommunicationController.getUserById(user.getUid(), sid,
                                userResponse -> {
                            StoredUser storedUser = new StoredUser();
                            storedUser.setUid(userResponse.getUid());
                            storedUser.setProfileVersion(userResponse.getProfileVersion());
                            storedUser.setPicture(userResponse.getPicture());
                            storedUser.setName(userResponse.getName());
                            storedUser.setLife(userResponse.getLife());
                            storedUser.setExperience(userResponse.getExperience());
                            storedUser.setAmulet(userResponse.getAmulet());
                            storedUser.setWeapon(userResponse.getWeapon());
                            storedUser.setArmor(userResponse.getArmor());
                            storedUser.setPositionshare(userResponse.isPositionshare());

                            StoredUserRepository storedUserRepository = StoredUserRepository.getInstance(getApplicationContext());
                            ListenableFuture<StoredUser> futureExistingUser = storedUserRepository.storedUserDao().getUser(Integer.toString(storedUser.getUid()));

                            try {
                                StoredUser existingUser = Futures.getUnchecked(futureExistingUser);
                                if (existingUser == null) {
                                    storedUsers.add(storedUser);
                                    insertUsersInRoomDatabase(storedUsers);
                                    Log.d(TAG, "Utente inserito nel DB: " + storedUser.getUid());
                                } else if (userResponse.getProfileVersion() > existingUser.getProfileVersion()) {
                                    storedUsers.add(storedUser);
                                    insertUsersInRoomDatabase(storedUsers);
                                    Log.d(TAG, "Utente aggiornato nel DB: " + storedUser.getUid());
                                } else {
                                    //Log.d(TAG, "Utente già presente nel DB: " + storedUser.getUid());
                                }
                            } catch (Exception e) {
                                Log.d(TAG, "Errore nel recupero dell'utente dal DB: " + e.getMessage());
                            }

                        }, t -> {
                            Log.d(TAG, "Errore nel recupero dell'utente condiviso: " + t.getMessage());
                        });
                    }
                },
                t -> {
                    Log.d(TAG, "Errore nel recupero degli utenti condivisi: " + t.getMessage());
                });
    }

    //Funzione per la navigazione tra i fragment cliccando il marker sulla mappa
    @Override
    public boolean onMarkerClick(Marker marker) {
        String title = marker.getTitle();

        //Se il titolo del marker inizia così allora naviga al fragment corrispondente
        if (title != null) {
            if (title.startsWith("Object ID:")) {
                String objectUid = extractIdFromTitle(title); //Estraggo l'ID dall'oggetto usando la funzione apposita
                navigateToObjectFragment(objectUid);
                return true;
            } else if (title.startsWith("User ID:")) {
                String userUid = extractIdFromTitle(title); //Estraggo l'ID dall'oggetto usando la funzione apposita
                navigateToSharedUserFragment(userUid);
                return true;
            } else if (title.startsWith("Posizione corrente:")) {
                navigateToProfileFragment();
                return true;
            }
        }
        return false;
    }

    //Funzione per estrarre l'ID dall'oggetto/utente
    private String extractIdFromTitle(String title) {
        return title.substring(title.indexOf(":") + 2).trim(); //Estraggo l'ID dall'oggetto facendo il substring
    }

    //Funzione per navigare al fragment dell'oggetto
    private void navigateToObjectFragment(String objectUid) {
        ObjectFragmentMap objectFragmentMap = new ObjectFragmentMap();
        Bundle args = new Bundle();
        args.putString("objectUid", objectUid);
        objectFragmentMap.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.google_map, objectFragmentMap)
                .addToBackStack(null)
                .commit();
    }

    //Funzione per navigare al fragment dell'utente condiviso
    private void navigateToSharedUserFragment(String userUid) {
        SharedUserFragmentMap sharedUserFragmentMap = new SharedUserFragmentMap();
        Bundle args = new Bundle();
        args.putString("userUid", userUid);
        sharedUserFragmentMap.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.google_map, sharedUserFragmentMap)
                .addToBackStack(null)
                .commit();
    }

    //Funzione per navigare al fragment del profilo
    private void navigateToProfileFragment() {
        ProfileFragment profileFragment = new ProfileFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.google_map, profileFragment)
                .addToBackStack(null)
                .commit();
    }

    //Funzione per navigare al fragment del profilo
    private void navigateToNewPage() {
        NewPageFragment newPageFragment = new NewPageFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.google_map, newPageFragment)
                .addToBackStack(null)
                .commit();
    }

    //Funzione per ottenere l'icona del marker
    private BitmapDescriptor getBitmapDescriptorFromVector(int vectorDrawableResourceId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(this, vectorDrawableResourceId);
        if (vectorDrawable == null) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(
                vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    //Funzione per inserire gli utenti nel database
    private void insertUsersInRoomDatabase(List<StoredUser> storedUsers) {
        StoredUserRepository storedUserRepository = StoredUserRepository.getInstance(getApplicationContext());

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            storedUserRepository.storedUserDao().insertAll(storedUsers.toArray(new StoredUser[0]));
        });
    }

    //Funzione per ottenere la distanza tra la posizione dell'utente e quella degli oggetti
    private void maxDistance() {
        CommunicationController.getUserById(uid, sid, myProfile -> {
                CommunicationController.getObjectById(String.valueOf(myProfile.getAmulet()), sid,
                        amulet -> {
                    distance = 100 + amulet.getLevel();
                }, t -> {
                    Log.d(TAG, "Errore nel recupero del livello dell'amuleto: " + t.getMessage());
                });
            },
            t -> {
                Log.d(TAG, "Errore nel recupero della distanza: " + t.getMessage());
            });
    }

    //Funzione per rendere l'applicazione a schermo intero
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }
}