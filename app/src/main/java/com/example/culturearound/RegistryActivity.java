package com.example.culturearound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.database.Listeners.LoginListener;
import com.example.database.LoginHelper;


public class RegistryActivity extends AppCompatActivity implements LoginListener {
    private EditText Ime;
    private EditText Prezime;
    private EditText Email;
    private EditText Lozinka;
    private EditText PonoviLozinku;
    private Button Registriraj;

    LoginHelper loginHelper;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginHelper = new LoginHelper(this);

        if (loginHelper.checkIfSignedIn()) pokreniPocetnuStranicu();

        setContentView(R.layout.activity_registry);

        Ime = findViewById(R.id.re_ime);
        Prezime = findViewById(R.id.re_prezime);
        Email=findViewById(R.id.re_email);
        Lozinka = findViewById(R.id.re_password);
        PonoviLozinku = findViewById(R.id.re_password2);
        Registriraj = findViewById(R.id.register_button);
        Registriraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInformation(
                        Ime.getText().toString(),
                        Prezime.getText().toString(),
                        Email.getText().toString(),
                        Lozinka.getText().toString(),
                        PonoviLozinku.getText().toString());
            }
        });
    }

    private void validateInformation (String ime, String prezime, String email, String lozinka, String ponLozinku) {

        if(ime.isEmpty() || prezime.isEmpty() || email.isEmpty() || lozinka.isEmpty() || ponLozinku.isEmpty()  ){
            Toast.makeText(this, "Niste unijeli korisničke podatke.", Toast.LENGTH_LONG).show();
        }
        else {
            if(lozinka.equals(ponLozinku)){
                loginHelper.createAccount(ime, prezime, email, lozinka);
            }
            else{
                Toast.makeText(this, "Lozinka i ponovljena lozinka nisu jednake.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void pokreniPocetnuStranicu() {
        Intent intent = new Intent(RegistryActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }


    @Override
    public void onLoginSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        pokreniPocetnuStranicu();
    }

    @Override
    public void onLoginFail(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

