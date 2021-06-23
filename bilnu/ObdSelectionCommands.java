package com.example.bilnu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ObdSelectionCommands extends AppCompatActivity {
    ListView mOdbCommandsLv, mObdSelectedLv;
    private ArrayAdapter<String> listAdapter;
    private ArrayAdapter <String> listAdapter2;
    Button mstartObdBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obd_selection_commands);

        ArrayList<String> list = new ArrayList();
        list.add("Presión barométrica");
        list.add("Presión de combustible");
        list.add("Presión del riel de combustible");
        list.add("Temperatura de entrada de aire");
        list.add("Temperatura ambiente");
        list.add("Temperatura de anticongelante");
        list.add("Temperatura del aceite");
        list.add("Buscar tipo de combustible");
        list.add("Tasa de consumo");
        list.add("Nivel de combustible");
        list.add("Relación de aire y combustible");
        list.add("Relación aire-combustible de banda ancha");
        list.add("Distancia con MIL activada");
        list.add("Distancia desde CC");
        list.add("Número DTC");
        list.add("Distancia viajada antes de limpiar los codigos");
        list.add("Encendido del motor");
        list.add("Voltaje del módulo");
        list.add("Avance de tiempo");
        list.add("Códigos de problemas permanentes");
        list.add("Comando de carga absoluta");
        list.add("Posición del acelerador");


        mOdbCommandsLv = findViewById(R.id.ObdCommandsLv);
        mstartObdBtn = findViewById(R.id.startObdBtn);
        mObdSelectedLv = findViewById(R.id.ObdSelectedLv);
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        mOdbCommandsLv.setAdapter(listAdapter);
        listAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mObdSelectedLv.setAdapter(listAdapter2);

        mOdbCommandsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ObdCommandString = listAdapter.getItem(position);
                listAdapter2.add(ObdCommandString);
                listAdapter2.notifyDataSetChanged();
            }
        });

        mstartObdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObdListToSend();
            }
        });
    }

    private void ObdListToSend(){
        int sizeOfLv = mObdSelectedLv.getAdapter().getCount();
        ArrayList <String> listToSend = new ArrayList<>();
        for(int i = 0; i < sizeOfLv; i++){
            String textItemList = mObdSelectedLv.getItemAtPosition(i).toString();
            listToSend.add(textItemList);
        }

        Intent intent = new Intent(ObdSelectionCommands.this, LiveDataActivity.class);
        intent.putExtra("obd_commands", listToSend);
        startActivity(intent);
    }
    /*private void init() {
        this.mOdbCommandsLv = findViewById(R.id.ObdCommandsLv);
    }

    private void mostrarListViewBasico(){
        List<String> lista = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.array_odbCommands)));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);

        this.mOdbCommandsLv.setAdapter(arrayAdapter);

        this.mOdbCommandsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object = parent.getAdapter().getItem(position);
                Toast.makeText(getBaseContext(), object.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    // Lista en color Rojo
    private ArrayAdapter<String> getAdaptermObdCommandsBasic(List<String> lista){
        return new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista){
          @Override
            public View getView(int position, View convertView, ViewGroup parent){
              View view = super.getView(position, convertView, parent);

              TextView txt = view.findViewById(android.R.id.text1);
              txt.setTextColor(Color.RED);
              return view;
          }
        };

    }

    private  mostrarListViewNombreCorrecto(){
        String[] nombre = {}
    } */

}