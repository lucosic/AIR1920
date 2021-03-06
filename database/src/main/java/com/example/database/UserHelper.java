package com.example.database;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.database.EntitiesFirebase.Korisnik;
import com.example.database.Listeners.UserListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserHelper extends FirebaseHelper {


    private final UserListener userListener;

    public UserHelper(Context context, UserListener userListener) {
        mAuth = FirebaseAuth.getInstance();
        mContext = context;
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        this.userListener = userListener;
    }

    public String returnUserId () {
        String userId = mAuth.getCurrentUser().getUid();
        if (userId == null) userId = "";
        return userId;
    }


    public void findUserById (final String userId) {
        if(provjeriDostupnostMreze()) {
            mQuery = mDatabase.child("Korisnik").child(userId);
            mQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Korisnik currentUser = new Korisnik();
                        currentUser = dataSnapshot.getValue(Korisnik.class);
                        currentUser.setUid(userId);
                        userListener.onLoadUserSuccess("Uspjesno dohvaćanje- listener", currentUser);
                }}

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    userListener.onLoadUserFail("Neuspješno dohvaćanje usera- listener");
                }
            });
        }
    }

    public void updateData(String firstName, String lastName, String email, String pictureUrl, final String userId) {
        if (!provjeriDostupnostMreze()) return;

        if (firstName != "") {
            mDatabase.child("Korisnik").child(userId).child("ime").setValue(firstName);
        }

        if (lastName != "") {
            mDatabase.child("Korisnik").child(userId).child("prezime").setValue(lastName);
        }

        if (email != "") {
            mDatabase.child("Korisnik").child(userId).child("email").setValue(email);
            mAuth.getCurrentUser().updateEmail(email);
        }

        if (pictureUrl != "") {
            mDatabase.child("Korisnik").child(userId).child("lokacijaSlike").setValue(pictureUrl);
        }
    }

    public void removeItemFromFavorites(final String userId, final Integer selectedItemId){

        if(provjeriDostupnostMreze()) {
            Query query = mDatabase
                    .child("Korisnik")
                    .child(userId)
                    .child("listaSpremljenihZnamenitosti");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Map<String, Long>> updatedListZnamenitosti = new ArrayList<Map<String, Long>>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        Map<String,Long> value = (Map<String,Long>) ds.getValue();
                        Long idZnamenitosti = value.get("idZnamenitosti");
                        Log.d("haha-d-idZnamenitosti",idZnamenitosti.toString());
                        Log.d("haha-d-selectedItemId", selectedItemId.toString());
                        if (idZnamenitosti.intValue() != selectedItemId){
                            updatedListZnamenitosti.add(value);
                        }
                    }
                    Map<String,Object> lista = new HashMap<>();
                    lista.put("listaSpremljenihZnamenitosti", updatedListZnamenitosti);
                    mDatabase.child("Korisnik")
                            .child(userId)
                            .updateChildren(lista);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void addItemToFavourites(final String userId, final Integer selectedItemId){

        if(provjeriDostupnostMreze()) {
            Query query = mDatabase
                    .child("Korisnik")
                    .child(userId)
                    .child("listaSpremljenihZnamenitosti");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Map<String, Long>> updatedListZnamenitosti = (List<Map<String, Long>>) dataSnapshot.getValue();
                    if (updatedListZnamenitosti == null) {
                        updatedListZnamenitosti = new ArrayList<Map<String, Long>>();
                    }
                    Map map = new HashMap<String, Integer>();
                    Boolean alreadyExists = false;
                    for (Map keyValueId : updatedListZnamenitosti) {
                        if (keyValueId.get("idZnamenitosti") == selectedItemId) {
                            alreadyExists = true;
                            break;
                        }
                    }
                    if(!alreadyExists) {
                        map.put("idZnamenitosti", selectedItemId);
                        updatedListZnamenitosti.add(map);
                        Map<String,Object> lista = new HashMap<>();
                        lista.put("listaSpremljenihZnamenitosti", updatedListZnamenitosti);
                        mDatabase.child("Korisnik")
                                .child(userId)
                                .updateChildren(lista);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }





}

