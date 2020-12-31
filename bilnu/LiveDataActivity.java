package com.example.bilnu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sohrab.obd.reader.obdCommand.ObdCommand;
import com.sohrab.obd.reader.obdCommand.ObdConfiguration;
import com.sohrab.obd.reader.obdCommand.SpeedCommand;
import com.sohrab.obd.reader.obdCommand.control.DistanceMILOnCommand;
import com.sohrab.obd.reader.obdCommand.control.DistanceSinceCCCommand;
import com.sohrab.obd.reader.obdCommand.control.DtcNumberCommand;
import com.sohrab.obd.reader.obdCommand.control.IgnitionMonitorCommand;
import com.sohrab.obd.reader.obdCommand.control.ModuleVoltageCommand;
import com.sohrab.obd.reader.obdCommand.control.PendingTroubleCodesCommand;
import com.sohrab.obd.reader.obdCommand.control.PermanentTroubleCodesCommand;
import com.sohrab.obd.reader.obdCommand.control.TroubleCodesCommand;
import com.sohrab.obd.reader.obdCommand.control.VinCommand;
import com.sohrab.obd.reader.obdCommand.engine.AbsoluteLoadCommand;
import com.sohrab.obd.reader.obdCommand.engine.OilTempCommand;
import com.sohrab.obd.reader.obdCommand.engine.RPMCommand;
import com.sohrab.obd.reader.obdCommand.engine.RuntimeCommand;
import com.sohrab.obd.reader.obdCommand.engine.ThrottlePositionCommand;
import com.sohrab.obd.reader.obdCommand.fuel.AirFuelRatioCommand;
import com.sohrab.obd.reader.obdCommand.fuel.ConsumptionRateCommand;
import com.sohrab.obd.reader.obdCommand.fuel.FuelLevelCommand;
import com.sohrab.obd.reader.obdCommand.fuel.WidebandAirFuelRatioCommand;
import com.sohrab.obd.reader.obdCommand.pressure.BarometricPressureCommand;
import com.sohrab.obd.reader.obdCommand.pressure.FuelPressureCommand;
import com.sohrab.obd.reader.obdCommand.pressure.FuelRailPressureCommand;
import com.sohrab.obd.reader.obdCommand.temperature.AirIntakeTemperatureCommand;
import com.sohrab.obd.reader.obdCommand.temperature.AmbientAirTemperatureCommand;
import com.sohrab.obd.reader.obdCommand.temperature.EngineCoolantTemperatureCommand;
import com.sohrab.obd.reader.service.ObdReaderService;
import com.sohrab.obd.reader.trip.TripRecord;

import java.util.ArrayList;

import static com.sohrab.obd.reader.constants.DefineObdReader.ACTION_READ_OBD_REAL_TIME_DATA;

public class LiveDataActivity extends AppCompatActivity {

    ListView mObdSelectedtoLiveDataLv, mobdLv;
    private TextView mObdInfoTextView, mObdInfoTextView3, mObdInfoTextView2, mObdProtocolNumberTv, mObdProtocolImplementTv, mautoNumberTv;
    String ACTION_OBD_CONNECTION_STATUS = "ACTION_OBD_CONNECTION_STATUS";

    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_data);

        Bundle bundle = getIntent().getExtras();
        ArrayList<String> listObdCommands = bundle.getStringArrayList("obd_commands");

        mObdInfoTextView = findViewById(R.id.tv_obd_info);
        mObdInfoTextView2 = findViewById(R.id.tv_obd_info2);
        mObdInfoTextView3 = findViewById(R.id.tv_obd_info3);
        mObdProtocolNumberTv = findViewById(R.id.obdProtocoloNumberTxt);
        mObdProtocolImplementTv = findViewById(R.id.obdProtocoloImplementTxt);
        mautoNumberTv = findViewById(R.id.autoNumberTv);

        mObdSelectedtoLiveDataLv = findViewById(R.id.ObdSelectedtoLiveDataLv);
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listObdCommands);

        mobdLv = findViewById(R.id.obdLv);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        mobdLv.setAdapter(listAdapter);
        mObdSelectedtoLiveDataLv.setAdapter(arrayAdapter);

        if(mObdSelectedtoLiveDataLv.getAdapter().getCount()==0){
            //mObdInfoTextView.setText("Velocidad: No disponible");
            //mObdInfoTextView2.setText("RPM: No disponible");
            //mObdInfoTextView3.setText("Códigos con problema: No disponible");
            findViewById(R.id.progress_bar).setVisibility(View.GONE);
            ArrayList<ObdCommand> obdCommands = new ArrayList<>();
            obdCommands.add(new SpeedCommand());
            obdCommands.add(new RPMCommand());
            obdCommands.add(new TroubleCodesCommand());
            ObdConfiguration.setmObdCommands(this, obdCommands);

        }

        /*else if(mObdSelectedtoLiveDataLv.getAdapter().getCount()==25){
            findViewById(R.id.progress_bar).setVisibility(View.GONE);
            ArrayList<ObdCommand> obdCommands = new ArrayList<>();
            obdCommands.add(new SpeedCommand());
            obdCommands.add(new RPMCommand());
            obdCommands.add(new TroubleCodesCommand());
            //mObdInfoTextView.setText("Velocidad: ");
            //mObdInfoTextView2.setText("RPM: ");
            //mObdInfoTextView3.setText("Códigos con problema:");
            ObdConfiguration.setmObdCommands(this, obdCommands);
        } */

        else {
            findViewById(R.id.progress_bar).setVisibility(View.GONE);
            ArrayList<ObdCommand> obdCommands = new ArrayList<>();
            obdCommands.add(new SpeedCommand());
            obdCommands.add(new RPMCommand());
            obdCommands.add(new TroubleCodesCommand());
            mobdLv.setVisibility(View.VISIBLE);
            for (int i = 0; i < mObdSelectedtoLiveDataLv.getAdapter().getCount(); i++) {
                /*if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Presión barométrica")) {
                    listAdapter.add("Opcion 1");
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Presion de combustible")) {
                    listAdapter.add("Opcion 2");
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Presión del riel de combustible")) {
                    listAdapter.add("Opcion 3");
                }
                listAdapter.notifyDataSetChanged();

            } */
                if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Presión barométrica")){
                    obdCommands.add(new BarometricPressureCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Presion de combustible")){
                    obdCommands.add(new FuelPressureCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Presión del riel de combustible")) {
                    obdCommands.add(new FuelRailPressureCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Temperatura de entrada de aire")) {
                    obdCommands.add(new AirIntakeTemperatureCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Temperatura ambiente")) {
                    obdCommands.add(new AmbientAirTemperatureCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Temperatura de anticongelante")) {
                    obdCommands.add(new EngineCoolantTemperatureCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Temperatura del aceite")) {
                    obdCommands.add(new OilTempCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Tasa de consumo")) {
                    obdCommands.add(new ConsumptionRateCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Nivel de combustible")) {
                    obdCommands.add(new FuelLevelCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Relación de aire y combustible")) {
                    obdCommands.add(new AirFuelRatioCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Relación aire-combustible de banda ancha")) {
                    obdCommands.add(new WidebandAirFuelRatioCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Distancia con MIL activada")) {
                    obdCommands.add(new DistanceMILOnCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Distancia desde CC")) {
                    obdCommands.add(new DistanceSinceCCCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Número DTC")) {
                    obdCommands.add(new DtcNumberCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Distancia viajada antes de limpiar los codigos")) {
                    obdCommands.add(new DistanceSinceCCCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Encendido del motor")) {
                    obdCommands.add(new IgnitionMonitorCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Voltaje del módulo")) {
                    obdCommands.add(new ModuleVoltageCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Avance de tiempo")) {
                    obdCommands.add(new RuntimeCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Comando VIN")) {
                    obdCommands.add(new VinCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Códigos de problemas permanentes")) {
                    obdCommands.add(new PermanentTroubleCodesCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Códigos de problemas")) {
                    obdCommands.add(new TroubleCodesCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Códigos de problemas pendientes")) {
                    obdCommands.add(new PendingTroubleCodesCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Comando de carga absoluta")) {
                    obdCommands.add(new AbsoluteLoadCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Temperatura del aceite")) {
                    obdCommands.add(new OilTempCommand());
                } else if (mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Posición del acelerador")) {
                    obdCommands.add(new ThrottlePositionCommand());
                }
            }
            ObdConfiguration.setmObdCommands(this, obdCommands);
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_READ_OBD_REAL_TIME_DATA);
        intentFilter.addAction(ACTION_OBD_CONNECTION_STATUS);
        registerReceiver(mObdReaderReceiver, intentFilter);

        // set gas price per litre so that gas cost can calculated. Default is 7 $/l
        float gasPrice = 18.48f; // per litre, you should initialize according to your requirement.
        ObdPreferences.get(this).setGasPrice(gasPrice);

        //start service which will execute in background for connecting and execute command until you stop
        startService(new Intent(this, ObdReaderService.class));
    }


    BroadcastReceiver mObdReaderReceiver = new BroadcastReceiver() {
        //@SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //findViewById(R.id.progress_bar).setVisibility(View.GONE);
            if (action.equals(ACTION_OBD_CONNECTION_STATUS)) {
                String connectionStatusMsg = intent.getStringExtra(ObdReaderService.INTENT_EXTRA_DATA);
                if (connectionStatusMsg.equals(getString(R.string.obd_connected))) {
                    Toast.makeText(LiveDataActivity.this, "Conectado exitosamente", Toast.LENGTH_SHORT).show();
                    //OBD connected  do what want after OBD connection
                }
                else if (connectionStatusMsg.equals(getString(R.string.connect_lost))) {
                    Toast.makeText(LiveDataActivity.this, "Se perdio la conexión", Toast.LENGTH_SHORT).show();
                    //OBD disconnected  do what want after OBD disconnection
                }
                else {
                    Toast.makeText(LiveDataActivity.this, "Revisa la vinculación con BILNU OBD2", Toast.LENGTH_SHORT).show();
                    // here you could check OBD connection and pairing status
                }
            }
            else if (action.equals(ACTION_READ_OBD_REAL_TIME_DATA)) {
                // here you can fetch real time data from TripRecord using getter methods like
                if(mObdSelectedtoLiveDataLv.getAdapter().getCount() == 0  ){
                    findViewById(R.id.progress_bar).setVisibility(View.GONE);
                    mObdInfoTextView.setVisibility(View.VISIBLE);
                    mObdInfoTextView2.setVisibility(View.VISIBLE);
                    mObdInfoTextView3.setVisibility(View.VISIBLE);
                    mObdProtocolNumberTv.setVisibility(View.VISIBLE);
                    mObdProtocolImplementTv.setVisibility(View.VISIBLE);
                    mautoNumberTv.setVisibility(View.VISIBLE);
                    TripRecord tripRecord = TripRecord.getTripRecode(LiveDataActivity.this);
                    tripRecord.getmDescribeProtocol();
                    tripRecord.getmDescribeProtocolNumber();
                    tripRecord.getSpeed();
                    tripRecord.getEngineRpm();
                    tripRecord.getmFaultCodes();
                    if(tripRecord.getSpeed() == 0){
                        mObdInfoTextView.setText("Velocidad: No disponible");
                    }
                    if(tripRecord.getSpeed() != 0){
                        mObdInfoTextView.setText("Velocidad: " + tripRecord.getSpeed().toString() + "km/h");
                    }
                    if(tripRecord.getEngineRpm() == null){
                        mObdInfoTextView2.setText("RPM: No disponible");
                    }
                    if(tripRecord.getEngineRpm() != null){
                        mObdInfoTextView2.setText("RPM: " + tripRecord.getEngineRpm());
                    }
                    if(tripRecord.getmFaultCodes() == null){
                        mObdInfoTextView3.setText("Códigos de error: No hay ningun error");
                    }
                    if(tripRecord.getmFaultCodes() != null){
                        mObdInfoTextView3.setText("Códigos de error:" + tripRecord.getmFaultCodes());
                    }
                    if(tripRecord.getmDescribeProtocol() == null){
                        mObdProtocolImplementTv.setText("Protocolo: No hay información" );
                    }
                    if(tripRecord.getmDescribeProtocol() != null){
                        mObdProtocolImplementTv.setText("Protocolo: " + tripRecord.getmDescribeProtocol());
                    }
                    if(tripRecord.getmDescribeProtocolNumber() == null){
                        mObdProtocolNumberTv.setText("Número de Protocolo: No hay iformación");

                    }
                    if(tripRecord.getmDescribeProtocolNumber() != null){
                        mObdProtocolNumberTv.setText("Número de Protocolo: " + tripRecord.getmDescribeProtocolNumber());
                    }
                    if(tripRecord.getmVehicleIdentificationNumber() == null){
                        mObdProtocolNumberTv.setText("Número de Identificación: No existe información");
                    }
                    if(tripRecord.getmVehicleIdentificationNumber() != null){
                        mObdProtocolNumberTv.setText("Número de Identificación: " + tripRecord.getmVehicleIdentificationNumber());
                    }
                    //tripRecord.getSpeed();
                    //tripRecord.getEngineRpm();
                }
                else {
                    listAdapter = new ArrayAdapter<String>(LiveDataActivity.this, android.R.layout.simple_list_item_1);
                    findViewById(R.id.progress_bar).setVisibility(View.GONE);
                    mObdInfoTextView.setVisibility(View.VISIBLE);
                    mObdInfoTextView2.setVisibility(View.VISIBLE);
                    mObdInfoTextView3.setVisibility(View.VISIBLE);
                    mObdProtocolNumberTv.setVisibility(View.VISIBLE);
                    mObdProtocolImplementTv.setVisibility(View.VISIBLE);
                    mautoNumberTv.setVisibility(View.VISIBLE);
                    mobdLv.setVisibility(View.VISIBLE);
                    mobdLv.setAdapter(listAdapter);
                    TripRecord tripRecord = TripRecord.getTripRecode(LiveDataActivity.this);
                    tripRecord.getmDescribeProtocol();
                    tripRecord.getmDescribeProtocolNumber();
                    tripRecord.getmVehicleIdentificationNumber();
                    tripRecord.getSpeed();
                    tripRecord.getEngineRpm();
                    tripRecord.getmFaultCodes();
                    if(tripRecord.getSpeed() == 0){
                        mObdInfoTextView.setText("Velocidad: No disponible");
                    }
                    if (tripRecord.getSpeed() != 0){
                        mObdInfoTextView.setText("Velocidad: " + tripRecord.getSpeed().toString());
                    }
                    if (tripRecord.getEngineRpm() == null){
                        mObdInfoTextView2.setText("RPM: No disponible");
                    }
                    if (tripRecord.getEngineRpm() != null){
                        mObdInfoTextView2.setText("RPM: " + tripRecord.getEngineRpm());
                    }
                    if (tripRecord.getmFaultCodes() == null){
                        mObdInfoTextView3.setText("Códigos error: No hay ningun error");
                    }
                    if (tripRecord.getmFaultCodes() != null){
                        mObdInfoTextView3.setText("Códigos de error:" + tripRecord.getmFaultCodes());
                    }
                    if(tripRecord.getmDescribeProtocol() == null){
                        mObdProtocolImplementTv.setText("Protocolo: No hay información" );
                    }
                    if(tripRecord.getmDescribeProtocol() != null){
                        mObdProtocolImplementTv.setText("Protocolo: " + tripRecord.getmDescribeProtocol());
                    }
                    if(tripRecord.getmDescribeProtocolNumber() == null){
                        mObdProtocolNumberTv.setText("Número de Protocolo: No hay iformación");

                    }
                    if(tripRecord.getmDescribeProtocolNumber() != null){
                        mObdProtocolNumberTv.setText("Número de Protocolo: " + tripRecord.getmDescribeProtocolNumber());
                    }
                    if(tripRecord.getmVehicleIdentificationNumber() == null){
                        mObdProtocolNumberTv.setText("Número de Identificación: No existe información");
                    }
                    if(tripRecord.getmVehicleIdentificationNumber() != null){
                        mObdProtocolNumberTv.setText("Número de Identificación: " + tripRecord.getmVehicleIdentificationNumber());
                    }
                    for(int i = 0; i<mObdSelectedtoLiveDataLv.getAdapter().getCount(); i++){
                        if((mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Presión barométrica"))){
                            //TripRecord tripRecord1 = TripRecord.getTripRecode(LiveDataActivity.this);
                            if(tripRecord.getmBarometricPressure() == null){
                                listAdapter.add("Presión barométrica: No disponible");
                            }
                            else{
                                listAdapter.add("Presión barométrica: " + tripRecord.getmBarometricPressure());
                            }
                            //mObdInfoTextView0.setText(tripRecord1.getmBarometricPressure().toString());
                        }
                        else if((mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Presion de combustible"))){
                            if(tripRecord.getmFuelPressure() == null){
                                listAdapter.add("Presion de combustible: No disponible");
                            }
                            else {
                                listAdapter.add("Presion de combustible: " + tripRecord.getmFuelPressure());
                                //mObdInfoTextView1.setText(tripRecord2.getmFuelPressure().toString());
                            }
                        }
                        else if(mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Presión del riel de combustible")){
                            //TripRecord tripRecord3 = TripRecord.getTripRecode(LiveDataActivity.this);
                            if(tripRecord.getmFuelRailPressure() == null){
                                listAdapter.add("Presión del riel de combustible: No disponible ");
                            }
                            else {
                                listAdapter.add("Presión del riel de combustible: " + tripRecord.getmFuelRailPressure());

                            }
                            //mObdInfoTextView2.setText(tripRecord3.getmFuelRailPressure().toString);
                        }
                        else if(mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Temperatura de entrada de aire")){
                            if(tripRecord.getmAirFuelRatio() == null){
                                listAdapter.add("Temperatura de entrada de aire:  No disponible");
                            }
                            else{
                                listAdapter.add("Temperatura de entrada de aire: " + tripRecord.getmAirFuelRatio());
                            }
                            //TripRecord tripRecord4 = TripRecord.getTripRecode(LiveDataActivity.this);
                            //mObdInfoTextView3.setText(tripRecord4.getmAirFuelRatio().toString);
                        }
                        else if(mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Temperatura ambiente")){
                            if(tripRecord.getmAmbientAirTemp() == null){
                                listAdapter.add("Temperatura ambiente: No disponible");
                            }
                            else{
                                listAdapter.add("Temperatura ambiente: " + tripRecord.getmAmbientAirTemp());
                            }
                            //TripRecord tripRecord5 = TripRecord.getTripRecode(LiveDataActivity.this);
                            //tripRecord5.getmAmbientAirTemp().toString();
                        }
                        else if(mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Temperatura de anticongelante")){
                            if(tripRecord.getmFuelPressure() == null){
                                listAdapter.add("Temperatura de anticongelante: No disponible");
                            }
                            else {
                                listAdapter.add("Temperatura de anticongelante: " + tripRecord.getmFuelPressure());
                            }
                            //TripRecord tripRecord6= TripRecord.getTripRecode(LiveDataActivity.this);
                            //tripRecord6.getmEngineCoolantTemp();
                        }
                        else if(mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Temperatura del aceite")){
                            //tripRecord.getmEngineOilTemp().toString();
                            if(tripRecord.getmEngineOilTemp() == null){
                                listAdapter.add("Temperatura del aceite: Aun no hay información");
                            }
                            else{
                                listAdapter.add("Temperatura del aceite: " + tripRecord.getmEngineOilTemp());
                            }
                            //TripRecord tripRecord7 = TripRecord.getTripRecode(LiveDataActivity.this);
                            //tripRecord7.getmEngineOilTemp();
                        }
                        else if(mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Tasa de consumo")){
                            //TripRecord tripRecord8 = TripRecord.getTripRecode(LiveDataActivity.this);
                            if(tripRecord.getmFuelConsumptionRate() == null){
                                listAdapter.add("Tasa de consumo: No disponible");
                            }
                            else{
                                listAdapter.add("Tasa de consumo: " + tripRecord.getmFuelConsumptionRate());
                            }
                            //tripRecord8.getmFuelConsumptionRate();
                        }
                        else if(mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Nivel de combustible")){
                            //TripRecord tripRecord9 = TripRecord.getTripRecode(LiveDataActivity.this);
                            if(tripRecord.getmEngineFuelRate() == null){
                                listAdapter.add("Nivel de combustible: No disponible" );
                            }
                            else {
                                listAdapter.add("Nivel de combustible: " + tripRecord.getmEngineFuelRate());
                            }
                            //tripRecord9.getmEngineFuelRate();
                        }
                        else if(mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Relación de aire y combustible")){
                            //TripRecord tripRecord10 = TripRecord.getTripRecode(LiveDataActivity.this);
                            if(tripRecord.getmWideBandAirFuelRatio() == null){
                                listAdapter.add("Relación de aire y combustible: No disponible");
                            }
                            else{
                                listAdapter.add("Relación de aire y combustible: " + tripRecord.getmWideBandAirFuelRatio().toString());
                            }
                            //tripRecord10.getmWideBandAirFuelRatio();
                        }
                        else if(mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Relación aire-combustible de banda ancha")){
                            //TripRecord tripRecord11 = TripRecord.getTripRecode(LiveDataActivity.this);
                            if(tripRecord.getmWideBandAirFuelRatio() == null){
                                listAdapter.add("Relación aire-combustible de banda ancha: No disponible");
                            }
                            else{
                                listAdapter.add("Relación aire-combustible de banda ancha: " + tripRecord.getmWideBandAirFuelRatio().toString());
                            }
                            //tripRecord11.getmWideBandAirFuelRatio();
                        }
                        else if(mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Distancia con MIL activada")){
                            //TripRecord tripRecord12 = TripRecord.getTripRecode(LiveDataActivity.this);
                            if(tripRecord.getmDistanceTraveledMilOn() == null){
                                listAdapter.add("Distancia con MIL activada: No disponible");
                            }
                            else{
                                listAdapter.add("Distancia con MIL activada: " + tripRecord.getmDistanceTraveledMilOn());
                            }
                            //tripRecord12.getmDistanceTraveledMilOn();
                        }
                        else if(mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Distancia desde CC")){
                            // TripRecord tripRecord13 = TripRecord.getTripRecode(LiveDataActivity.this);
                            if(tripRecord.getmDistanceTraveledAfterCodesCleared() == null){
                                listAdapter.add("Distancia desde CC: No disponible");
                            }
                            else{
                                listAdapter.add("Distancia desde CC: " + tripRecord.getmDistanceTraveledAfterCodesCleared());
                            }
                            //tripRecord13.getmDistanceTraveledAfterCodesCleared();
                        }
                        else if(mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Número DTC")){
                            //TripRecord tripRecord14 = TripRecord.getTripRecode(LiveDataActivity.this);
                            if(tripRecord.getmDtcNumber() == null){
                                listAdapter.add("Número DTC: No disponible");
                            }
                            else {
                                listAdapter.add("Número DTC: " + tripRecord.getmDtcNumber());
                            }
                            //tripRecord14.getmDtcNumber();
                        }
                        else if(mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Distancia viajada antes de limpiar los codigos")){
                            //TripRecord tripRecord15 = TripRecord.getTripRecode(LiveDataActivity.this);
                            if(tripRecord.getmDistanceTraveledAfterCodesCleared() == null){
                                listAdapter.add("Distancia viajada antes de limpiar los codigos: No hay información");
                            }
                            else{
                                listAdapter.add("Distancia viajada antes de limpiar los codigos: " + tripRecord.getmDistanceTraveledAfterCodesCleared());
                            }
                            //tripRecord15.getmDistanceTraveledAfterCodesCleared();
                        }
                        else if(mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Encendido del motor")){
                            //TripRecord tripRecord16 = TripRecord.getTripRecode(LiveDataActivity.this);
                            if(tripRecord.getmEngineLoad() == null){
                                listAdapter.add("Encendido del motor: No disponible");
                            }
                            else{
                                listAdapter.add("Encendido del motor: " + tripRecord.getmEngineLoad());
                            }
                            //tripRecord16.getmEngineLoad();
                        }
                        else if(mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Voltaje del módulo")){
                            //TripRecord tripRecord17 = TripRecord.getTripRecode(LiveDataActivity.this);
                            if(tripRecord.getmControlModuleVoltage() == null){
                                listAdapter.add("Voltaje del módulo: No disponible");
                            }
                            else{
                                listAdapter.add("Voltaje del módulo: " + tripRecord.getmControlModuleVoltage());
                            }
                            //tripRecord17.getmControlModuleVoltage();
                        }
                        else if(mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Avance de tiempo")){
                            //TripRecord tripRecord18 = TripRecord.getTripRecode(LiveDataActivity.this);
                            if(tripRecord.getEngineRuntime() == null){
                                listAdapter.add("Avance de tiempo: No hay datos");
                            }
                            else {
                                listAdapter.add("Avance de tiempo: " + tripRecord.getEngineRuntime());
                            }
                            //tripRecord18.getEngineRuntime();
                        }
                        else if(mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Comando VIN")){
                            //TripRecord tripRecord19 = TripRecord.getTripRecode(LiveDataActivity.this);
                            if(tripRecord.getmVehicleIdentificationNumber() == null){
                                listAdapter.add("Comando VIN: No existe el dato");
                            }
                            else{
                                listAdapter.add("Comando VIN: " + tripRecord.getmVehicleIdentificationNumber());
                            }
                            //tripRecord19.getmVehicleIdentificationNumber();
                        }
                        else if(mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Códigos de problemas permanentes")){
                            //TripRecord tripRecord20 = TripRecord.getTripRecode(LiveDataActivity.this);
                            if(tripRecord.getmPermanentTroubleCode() == null){
                                listAdapter.add("Códigos de problemas permanentes: No hay códigos");
                            }
                            else{
                                listAdapter.add("Códigos de problemas permanentes" + tripRecord.getmPermanentTroubleCode());
                            }
                            //tripRecord20.getmPermanentTroubleCode();
                        }
                        else if(mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Códigos de problemas")){
                            //TripRecord tripRecord21 = TripRecord.getTripRecode(LiveDataActivity.this);
                            if(tripRecord.getmPendingTroubleCode() == null){
                                listAdapter.add("Códigos de problemas: No hay códigos");
                            }
                            else{
                                listAdapter.add("Códigos de problemas: " + tripRecord.getmPendingTroubleCode());
                            }
                            //tripRecord21.getmPendingTroubleCode();
                        }
                        else if(mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Códigos de problemas pendientes")){
                            //TripRecord tripRecord22= TripRecord.getTripRecode(LiveDataActivity.this);
                            if(tripRecord.getmPendingTroubleCode() == null){
                                listAdapter.add("Códigos de problemas pendientes: No hay códigos");
                            }
                            else {
                                listAdapter.add("Códigos de problemas pendientes: " + tripRecord.getmPendingTroubleCode());
                            }
                            //tripRecord22.getmPendingTroubleCode();
                        }
                        else if(mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Comando de carga absoluta")){
                            //TripRecord tripRecord23 = TripRecord.getTripRecode(LiveDataActivity.this);
                            if(tripRecord.getmAbsLoad() == null){
                                listAdapter.add("Comando de carga absoluta: No hay datos");
                            }
                            else{
                                listAdapter.add("Comando de carga absoluta" + tripRecord.getmAbsLoad());
                            }
                            //tripRecord23.getmAbsLoad();
                        }
                        else if(mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Temperatura del aceite")){
                            if(tripRecord.getmEngineOilTemp() == null){
                                listAdapter.add("Temperatura del aceite" + tripRecord.getmEngineOilTemp());
                            }
                            //TripRecord tripRecord24 = TripRecord.getTripRecode(LiveDataActivity.this);
                            else{
                                listAdapter.add("Temperatura del aceite" + tripRecord.getmEngineOilTemp());
                            }
                            //tripRecord24.getmEngineOilTemp();
                        }
                        else if(mObdSelectedtoLiveDataLv.getItemAtPosition(i).toString().equals("Posición del acelerador")){
                            //TripRecord tripRecord25= TripRecord.getTripRecode(LiveDataActivity.this);
                            if(tripRecord.getmThrottlePos() == null){
                                listAdapter.add("Posición del acelerador: No hay información");
                            }
                            else{
                                listAdapter.add("Posición del acelerador" + tripRecord.getmThrottlePos());
                            }
                            //tripRecord25.getmThrottlePos();
                        }
                        listAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregister receiver
        unregisterReceiver(mObdReaderReceiver);
        //stop service
        stopService(new Intent(this, ObdReaderService.class));
        // This will stop background thread if any running immediately.
        ObdPreferences.get(this).setServiceRunningStatus(false);
    }
}
