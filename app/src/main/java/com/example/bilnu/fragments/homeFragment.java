package com.example.bilnu.fragments;

import android.content.Intent;
import android.graphics.BlendMode;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bilnu.BluetoothActivity;
import com.example.bilnu.LiveDataActivity;
import com.example.bilnu.MainActivity;
import com.example.bilnu.ObdSelectionCommands;
import com.example.bilnu.R;

public class homeFragment extends Fragment {

    Button mConfigBluetoothBtn; //Creacion de los objetos para enlazar
    ImageButton mConectImgBtn;  //con los objetos gráficos
    View v;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public homeFragment() {
        // Es requerido un construtor vacío
    }
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Para poder usar los metodos
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        //Crea los enlances a los objetos gráficos
        mConfigBluetoothBtn = v.findViewById(R.id.botonConectar);
        mConectImgBtn = v.findViewById(R.id.conectarImgBtn);

        //Creamos un Listener sobre el botón Bluetooth para detectar si es presionado
        mConfigBluetoothBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BluetoothActivity.class);
                startActivity(intent); //Nos redireccionamos a la Activity BluetoothActivity
            }
        });
        //Creamos un Listener sobre el botón Conectar para detectar si es presionado
        mConectImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ObdSelectionCommands.class);
                startActivity(intent); //Nos redireccionamos a la Activity ObdSelectionCommands
            }
        });
        return v;
    }
}