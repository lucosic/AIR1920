package com.example.database.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourites", primaryKeys = {"korisnicko_ime", "id_znamenitost"})
public class Favourites {

    @ForeignKey(entity = Korisnik.class, parentColumns = "korisnicko_ime", childColumns = "korisnicko_ime")
    @ColumnInfo(index = true)
    @NonNull String korisnicko_ime;

    @ForeignKey(entity = Znamenitost.class, parentColumns = "id_znamenitost", childColumns = "id_znamenitost")
    @ColumnInfo(index = true)
    @NonNull int id_znamenitost;

    public String getKorisnicko_ime() {
        return korisnicko_ime;
    }

    public void setKorisnicko_ime(String korisnicko_ime) {
        this.korisnicko_ime = korisnicko_ime;
    }

    public int getId_znamenitost() {
        return id_znamenitost;
    }

    public void setId_znamenitost(int id_znamenitost) {
        this.id_znamenitost = id_znamenitost;
    }
}
