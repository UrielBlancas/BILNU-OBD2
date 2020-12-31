package com.example.bilnu.fragments;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.bilnu.ObdSelectionCommands;
import com.example.bilnu.R;

public class settingsFragment extends Fragment {

    View v;  //Creacion de una vista global para implementación de métodos.
    Button mconfig_botonbluetooth, mconfig_botonbuscar, mconfig_botonconectar;
    TextView mhowToConnectTv; //Creación de los objetos para enlanzar con los objetos gráficos

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public settingsFragment() {
        // Required empty public constructor
    }

    public static settingsFragment newInstance(String param1, String param2) {
        settingsFragment fragment = new settingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_settings, container, false);

        mconfig_botonbluetooth = v.findViewById(R.id.config_botonbluetooth);
        mconfig_botonbuscar = v.findViewById(R.id.config_botonbuscar);
        mconfig_botonconectar = v.findViewById(R.id.config_botonconectar);
        mhowToConnectTv = v.findViewById(R.id.howToConnectTv);

        mconfig_botonbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ObdSelectionCommands.class);
                startActivity(intent);
            }
        });

        mconfig_botonbluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BluetoothActivity.class);
                startActivity(intent);
            }
        });

        mconfig_botonconectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mhowToConnectTv.setVisibility(View.VISIBLE);
                mhowToConnectTv.setText("1.- Estando apagado el vehículo conecte el adaptador al conector para el diagnóstico. \n" +
                        "2. El dispositovo Bluetooth deberá estar prendido y vinculado con su telefono \n" +
                        "3. Gire la llave de encendido \n" +
                        "4. Seleccione el dispositov OBD2 de la lista Dispositivos emparejados\n" +
                        "5. Elija los comandos que desea visualizar en pantalla, la Velocidad, RPM, Fallas e ID del vehículo estan por defecto asignados \n" +
                        "6. Espere a que se muestren los datos de analisis, si requiere mas comandos, puede agregarlos");
            }
        });

       return v;
    }
}