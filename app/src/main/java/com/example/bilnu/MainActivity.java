package com.example.bilnu;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bilnu.fragments.aboutFragment;
import com.example.bilnu.fragments.homeFragment;
import com.example.bilnu.fragments.settingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    aboutFragment fragmentAbout = new aboutFragment();
    homeFragment fragmentHome = new homeFragment();
    settingsFragment fragmentSettings = new settingsFragment();
    BottomNavigationView navigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.botton_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(fragmentHome);

    }

//Método que permite visualizar el fragment seleccionado por el usuario
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
        = new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) { //Estructura de control
        switch (item.getItemId()){ //Espera a recibir el ID del elemento seleccionado por el usuario
            case R.id.menu_about: //Compara el dato recibido con el esperado
                loadFragment(fragmentAbout); //Carga el fragmento seleccionado
                return true;
            case R.id.menu_inicio: //Compara el dato recibido con el esperado
                loadFragment(fragmentHome); //Carga el fragmento seleccionado
                return true;
            case R.id.menu_settings: //Compara el dato recibido con el esperado
                loadFragment(fragmentSettings); //Carga el fragmento seleccionado
                return true;
        }
        return false;
    }
};

    //Método para cargar el fragmento seleccionado en pantalla
    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment); //Inicia el proceso para cambiar el fragmento seleccionado en el menu
        transaction.commit(); //Cambia el fragmento, lo asigna en la vista del usuario
    }
}


