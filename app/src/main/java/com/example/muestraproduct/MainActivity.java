package com.example.muestraproduct;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnBuscar, btnGrabar, btnEliminar;
    private EditText edtCodigo, edtDescripcion, edtStock, edtUbicacion;
    String codigo, descripcion, stock, ubicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        referencias();
        eventos();

        //Muestra los datos en mainActivity1
        if(getIntent().getStringExtra("codigoProducto") != null){

            datos();
        }

    }

    private void eventos(){
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar();
            }
        });

        btnGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grabar();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar();
            }
        });
    }

    private void  referencias() {

        btnGrabar = findViewById(R.id.btnGrabar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnBuscar = findViewById(R.id.btnBuscar);
        edtCodigo = findViewById(R.id.edtCodigo);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        edtStock = findViewById(R.id.edtStock);
        edtUbicacion = findViewById(R.id.edtUbicacion);
    }

    private void buscar(){
        Intent m = new Intent(this, MainActivity2.class);
        startActivity(m);

    }

    private  void limpiar(){
        edtUbicacion.setText("");
        edtCodigo.setText("");
        edtStock.setText("");
        edtDescripcion.setText("");
    }

    public void datos(){
        codigo = getIntent().getStringExtra("codigoProducto");
        String[] codigoProducto = {codigo};

        Db db = new Db(this, "Prueba", null,1);
        SQLiteDatabase miDB = db.getWritableDatabase();

        if(miDB != null){
            Cursor c = miDB.rawQuery("Select * from productos Where codigo =?", codigoProducto);

            if(c.moveToFirst()){
                do{
                    codigo = c.getString(0);
                    descripcion = c.getString(1);
                    stock = c.getString(2);
                    ubicacion = c.getString(3);

                    edtCodigo.setText(codigo);
                    edtDescripcion.setText(descripcion);
                    edtStock.setText(stock);
                    edtUbicacion.setText(ubicacion);
                }while (c.moveToNext());
            }
        }
        miDB.close();
    }

    public void eliminar(){
        if(edtCodigo.length() == 0){
            edtCodigo.setError("Ingresa el codigo del producto");
            return;
        }
        codigo = edtCodigo.getText().toString();
        String[] codigoProducto = {codigo};
        // ejecutamos la base de datos
        Db db = new Db(this, "Prueba",null,1);/*...*/
        SQLiteDatabase miDB = db.getWritableDatabase();

        if(miDB != null){
            Cursor c = miDB.rawQuery("select * from productos where codigo =?",codigoProducto);
            // producto no existe
            if(c.getCount()==0){
                Toast.makeText(getApplicationContext(),"No existe este producto",Toast.LENGTH_SHORT).show();
                limpiar();
                // producto se elimina
            }if (c.getCount() != 0){
                miDB.execSQL("Delete from productos where codigo =?", codigoProducto);

                Toast.makeText(getApplicationContext(),"Producto eliminado",Toast.LENGTH_SHORT).show();
                limpiar();
            }
        }
        miDB.close();
    }

    public void grabar(){
        if( edtCodigo.length() == 0){
            edtCodigo.setError("Ingrese el código del producto");
            return;
        }
        if (edtDescripcion.length() == 0){
            edtDescripcion.setError("Ingrese la descripción del producto");
            return;
        }
        if (edtStock.length() == 0){
            edtStock.setError("Ingrese el stock del producto");
            return;
        }
        if (edtUbicacion.length() == 0){
            edtUbicacion.setError("ingrese la unbicación del producto");
            return;
        }

        codigo = edtCodigo.getText().toString();
        descripcion = edtDescripcion.getText().toString();
        stock = edtStock.getText().toString();
        ubicacion = edtUbicacion.getText().toString();

        // preparamos String para las Query
        String[] cod = {codigo};
        String[] prod = {codigo, descripcion, stock, ubicacion};
        String[] mod = {descripcion, stock, ubicacion, codigo};

        //Ingresamos a la base de datos
        Db db = new Db(this, "Prueba", null,1);
        SQLiteDatabase miDB = db.getWritableDatabase();

        if(miDB != null){

            Cursor c = miDB.rawQuery("Select * from productos where codigo = ?",cod);

            // si cursor es cero ingresamos los datos
            if(c.getCount()==0){
                miDB.execSQL("insert into productos (codigo,descripcion,stock,ubicacion) values(?,?,?,?)", prod);

                Toast.makeText(getApplicationContext(), "Se ha grabado el producto", Toast.LENGTH_SHORT).show();
                limpiar();
                // Si cursor es distinto de cero actualizamos los datos
            }if (c.getCount() != 0){
                miDB.execSQL("update productos set descripcion = ?, stock = ?, ubicacion = ? where codigo = ?", mod);

                Toast.makeText(getApplicationContext(), "Se ha modificado correctamente", Toast.LENGTH_SHORT).show();
                limpiar();
            }
        }
        miDB.close();
    }
}