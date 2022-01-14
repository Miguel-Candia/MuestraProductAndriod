package com.example.muestraproduct;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    ArrayList<Product> productos = new ArrayList<>();

    private ArrayAdapter adaptador;
    private Button btnBuscar2;
    private EditText edtBuscar;
    private ListView listView;
    String codigo, descripcion, stock, ubicacion, codigoProducto, buscar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        referencias();
        eventos();
    }

    private void eventos(){
        btnBuscar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar2();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product p = productos.get(position);
                codigoProducto = p.getCodigo();

                enviar();
            }
        });
    }

    public void enviar(){
        Intent b = new Intent(this, MainActivity.class);
        b.putExtra("codigoProducto", codigoProducto);
        startActivity(b);
    }

    public void referencias(){
        btnBuscar2 = findViewById(R.id.btnBuscar2);
        edtBuscar = findViewById(R.id.edtBuscar);
        listView = findViewById(R.id.listView);
    }

    private void buscar2(){
        productos.clear();
        if(edtBuscar.length() == 0) {
            edtBuscar.setError("Ingrese un codigo valido");
            return;
        }
        buscar = edtBuscar.getText().toString();
        String[] bus = {buscar};

        try {
            Db db = new Db(this, "Prueba", null,1);
            SQLiteDatabase miDB = db.getWritableDatabase();
            // Buscar codigo
            Cursor c = miDB.rawQuery("Select * from productos Where codigo =?", bus);
            // buscar texto parecidos
           /* Cursor d = miDB.rawQuery("Select * from productos Where descripcion like '%' || ? ||'%'", bus);*/

            try{
                if(c.moveToFirst()){
                    Log.d("TAG_","Registros encontrados: " + c.getCount());
                    do{
                        codigo = c.getString(0);
                        descripcion = c.getString(1);
                        stock = c.getString(2);
                        ubicacion = c.getString(3);
                        // agregamos producto al array list
                        productos.add(new Product(codigo, descripcion, stock, ubicacion));
                    }while (c.moveToNext());
                }
            }catch (Exception ex){
                Log.e("TAG_", "Error "+ ex.toString());
            }finally {
                miDB.close();
                c.close();
            }
            // muestra los datos por listView
            adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, productos);
            listView.setAdapter(adaptador);
        }catch (Exception ex){
            Toast.makeText(this, "Error" + ex.toString(), Toast.LENGTH_SHORT).show();
        }

    }
}