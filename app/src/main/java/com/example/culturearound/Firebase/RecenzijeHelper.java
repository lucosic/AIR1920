package com.example.culturearound.Firebase;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import com.example.culturearound.Firebase.EntitiesFirebase.Komentar;
import com.google.firebase.auth.FirebaseAuth;
import com.example.culturearound.Firebase.Listeners.RecenzijaListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class RecenzijeHelper extends FirebaseHelper {
    private RecenzijaListener recenzijaListener;
    public RecenzijeHelper (Context context, RecenzijaListener recenzijaListener){
        mAuth = FirebaseAuth.getInstance();
        mContext = context;
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        this.recenzijaListener = recenzijaListener;

    }


    public void dohvatiSveRecenzije(){
        mQuery = mDatabase.child("komentar");
        if (provjeriDostupnostMreze()){
            mQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("Anja", "Dobavljanje..");
                    List<Komentar> listaRecenzija = new ArrayList<>();
                    for (DataSnapshot temp : dataSnapshot.getChildren()){
                        Komentar komentar = new Komentar();
                        komentar = temp.getValue(Komentar.class);
                        komentar.setIdKomentar(Integer.parseInt(temp.getKey()));
                        listaRecenzija.add(komentar);
                    }
                    Log.d("Anja", "Dobavljanje DONE");
                    recenzijaListener.onLoadRecenzijaSucess("Uspješno dohvaćanje - listener", listaRecenzija);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    recenzijaListener.onLoadRecenzijaFail("Neuspješno dohvaćanje - listener");
                }
            });
        }
    }

    public void dohvatiRecenzijePremaId(int idKomentar){
        mQuery = mDatabase.child("komentar").child(Integer.toString(idKomentar));
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Komentar komentar = new Komentar();
                komentar = dataSnapshot.getValue(Komentar.class);
                komentar.setIdKomentar(Integer.parseInt(dataSnapshot.getKey()));
                recenzijaListener.onLoadRecenzijaSucess("Uspješno dohvaćanje - listener", Arrays.asList(komentar));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                recenzijaListener.onLoadRecenzijaFail("Neuspješno dohvaćanje - listener");
            }
        });
    }










}