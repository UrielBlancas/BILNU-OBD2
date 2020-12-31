package com.example.bilnu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    public static final int REQUEST_ACCESS_COARSE_LOCATION = 1;
    public static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private BluetoothAdapter mBlueAdapter;
    private ArrayAdapter <String> listAdapter;
    private ArrayAdapter <String> listAdapter2;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String EXTRA_DEVICE_NAME = "device_name";
    String infoBluetoothDevice = null;

    TextView  mPairedTv, mnewDevicesTv;
    Button mOnBtn, mPairingBtn;
    ListView  mnewDevicesLv, mPairedLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mPairedTv     = findViewById(R.id.pairedTv);
        mOnBtn        = findViewById(R.id.onBtn);
        mnewDevicesLv = findViewById(R.id.scanDevicesLv);
        mPairingBtn   = findViewById(R.id.pairingBtn);
        mPairedLv     = findViewById(R.id.pairedDevicesLv);
        mnewDevicesTv = findViewById(R.id.newDevicesTv);

        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mnewDevicesLv.setAdapter(listAdapter);

        listAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mPairedLv.setAdapter(listAdapter2);

        //Adaptador
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();

        /*//Checar si el bluetooth esta disponible o no
        if(mBlueAdapter == null){
            mStatusBlueTv.setText("El Bluetooth no esta disponible");
        }
        else{
            mStatusBlueTv.setText("El Bluetooth esta disponible");
        } */

        //On btn click
        mOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBlueAdapter == null){ //No existe un adaptador Bluetooth en el dispositivo
                    showToast("Lo siento, tu dispositivo no es compatabile");
                }
                else {
                    if (!mBlueAdapter.isEnabled()) { //El Bluetooth aun no esta encendido
                        showToast("Activando Bluetooth");
                        //Intent activar bluetooth
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(intent, REQUEST_ENABLE_BT); //Pide permiso  al usuario para activar el bluetooth
                    } else {
                        showToast("El Bluetooth ya se encuentra activado"); //El bluetooth ya se encuentra encendido
                    }
                }
            }
        });
        mPairingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBluetoothState(); //Metodo de verificación del estado actual de Bluetooth
                checkCoarseLocationPermission(); //Permisos necesarios para empezar la busqueda de dispositivos
                if(mBlueAdapter != null && mBlueAdapter.isEnabled()){ //Hay un adaptador Bluetooth en el dispositivo y se encuentra activado el Bluetooth
                    if(!mBlueAdapter.isDiscovering()){  // No se encuentra en modo visible
                        showToast("Haciendo Bluetooth visible"); //Mensaje del sistema
                        Intent intentDiscovering = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                        startActivityForResult(intentDiscovering, REQUEST_DISCOVER_BT); //Pide permiso al usuario de hacer visible su Bluetooth
                    }
                    if(checkCoarseLocationPermission()){
                        listAdapter.clear(); //Limpia la lista
                        listAdapter2.clear(); //Limpia la lista,
                        mBlueAdapter.startDiscovery();
                    }
                    else{
                        checkBluetoothState(); //Verifica el estado actual de Bluetooth por si este cambio y de ser así
                                                //se reinicia el preceso
                    }
                }
            }
        });

        mPairedLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                infoBluetoothDevice = ((TextView)view).getText().toString();
                String deviceAddress = infoBluetoothDevice.substring(infoBluetoothDevice.length() - 17);
                String deviceName = infoBluetoothDevice.substring(0, infoBluetoothDevice.length() - 18);
                Intent intent = new Intent(BluetoothActivity.this, ObdSelectionCommands.class);
                intent.putExtra(EXTRA_DEVICE_ADDRESS, deviceAddress);
                intent.putExtra(EXTRA_DEVICE_NAME, deviceName);
                startActivity(intent);
            }
        });

        mnewDevicesLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showToast("Dispositivo no vinculado, ingresa a ajustes de tu telefono para vincularlo");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Registro dedicado para algunas acciones de Bluetooth

        registerReceiver(devicesFoundReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        registerReceiver(devicesFoundReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
        registerReceiver(devicesFoundReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        registerReceiver(pairedDevices, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    @Override
    protected void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(devicesFoundReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(pairedDevices);
        //unregisterReceiver(devicesFoundReceiver);
        //unregisterReceiver(pairedDevices);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBlueAdapter != null) {
            mBlueAdapter.cancelDiscovery();
        }
        // Unregister broadcast listeners
        this.unregisterReceiver(devicesFoundReceiver);
        this.unregisterReceiver(pairedDevices);
    }

    private boolean checkCoarseLocationPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_COARSE_LOCATION);
            return false;
        }
        else{

            return true;
        }
    }

    private void checkBluetoothState(){
        if(mBlueAdapter == null){
            Toast.makeText(this, "Bluetooth no es soportado en tu dispositivo", Toast.LENGTH_SHORT).show();
        }
        else{
            if(mBlueAdapter.isEnabled()){
                if(mBlueAdapter.isDiscovering()){
                    Toast.makeText(this, "Buscando dispositivos... ", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Listo para buscar dispositivos", Toast.LENGTH_SHORT).show();
                    mPairingBtn.setEnabled(true);
                }
            }
            else{
                Toast.makeText(this, "Necesitas activar el Bluetooth", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK){
                    //Bluetooth esta activado
                    showToast("Bluetooth Activado");
                }
                else{
                    //El usuario denego el acceso a activar Bluetooth
                    showToast("No se pudo activar el Bluetooth");
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_ACCESS_COARSE_LOCATION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Acceso concedido, buscando dispositivos Bluetooth", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Acceso denegado, no se pueden buscar dispositivos Bluetooth", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    //Implementacion del Receiver para obtener los nuevos dispositovos al alcance
    private final  BroadcastReceiver devicesFoundReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                mnewDevicesTv.setText("Dispositivos encontrados:");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                listAdapter.add(device.getName() + "\n" + device.getAddress());
                listAdapter.notifyDataSetChanged();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mPairingBtn.setText("Buscar nuevamente");
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                mPairingBtn.setText("Buscando...");
            }
        }
    };
    //Implementacion del Receiver para obtener los dispositovos conectados
    private final BroadcastReceiver pairedDevices = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                mPairedTv.setText("Dispositvos emparejados:");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                listAdapter2.add(device.getName() + "\n" + device.getAddress());
                listAdapter2.notifyDataSetChanged();
            }
            else{
                //El bluetooth esta apagado, no se pueden obtener los dispositivos emparejados
                showToast("Activa el Bluetooth para ver los dispositivos");
            }
        }
    };

    //Funcion mensaje Toast
    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}

