package mx.edu.ittepic.carlos.p2_creditos_academicos2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by francisco on 05/10/2017.
 */

public class Actividades extends AppCompatActivity {
    TextView nombre;
    TextView correo;
    TextView telefono;
    TextView carrera;
    TextView control;
    TextView creditos;
    ArrayList conceptosArray;
    Vector ID_Vector;
    BaseHelper mDbHelper;
    ListView lista;
    final Actividades puntero = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades);

        mDbHelper = new BaseHelper(this.getApplicationContext());
        conceptosArray = new ArrayList();
        ID_Vector = new Vector();
        nombre = (TextView)findViewById(R.id.Nombre);
        correo = (TextView)findViewById(R.id.correo);
        telefono = (TextView)findViewById(R.id.telefono);
        carrera = (TextView)findViewById(R.id.carrera);
        control = (TextView)findViewById(R.id.nocontrol);
        creditos = (TextView)findViewById(R.id.creditos);

        lista = (ListView)findViewById(R.id.Lista);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(puntero, Editar_actividad.class);
                intent.putExtra("id",(ID_Vector.get(i)).toString());
                startActivityForResult(intent,2);
            }
        });

        FloatingActionButton nuevo = (FloatingActionButton)findViewById(R.id.nuevo);
        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(puntero, Nueva_Actividad.class);
                intent.putExtra("control",control.getText().toString());
                startActivityForResult(intent,2);
            }
        });
/*
        FloatingActionButton eliminar = (FloatingActionButton)findViewById(R.id.eliminar);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                long count1 = db.delete(FeedReaderContract.FeedEntry.TABLE_ACTIVIDAD, FeedReaderContract.FeedEntry.COLUMN_ID_ALUMNO_FK + " like ?",new String[]{control.getText().toString()});
                long count2 = db.delete(FeedReaderContract.FeedEntry.TABLE_ALUMNO, FeedReaderContract.FeedEntry.COLUMN_NUMERO_CONTROL + " like ?",new String[]{control.getText().toString()});
                puntero.finish();
            }
        }); */

        FloatingActionButton editar = (FloatingActionButton)findViewById(R.id.editar);
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(puntero, Editar_alumno.class);
                intent.putExtra("control",control.getText().toString());
                startActivityForResult(intent,2);
            }
        });

        Intent intent = getIntent();
        try {
            control.setText(intent.getExtras().getString("control"));
        }catch (NullPointerException ex){

        }
        obtenerDatosAlumno();
        obtenerDatos();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        obtenerDatosAlumno();
        obtenerDatos();
    }

    private void obtenerDatosAlumno() {
        SQLiteDatabase dbb = mDbHelper.getReadableDatabase();
        String[] projection = {
                Contract.FeedEntry.COLUMN_NOMBRE,
                Contract.FeedEntry.COLUMN_CARRERA,
                Contract.FeedEntry.COLUMN_CORREO,
                Contract.FeedEntry.COLUMN_TELEFONO,
                "SUM("+Contract.FeedEntry.COLUMN_CREDITOS+") as CREDITOS"
        };
        Cursor c = dbb.query(
                Contract.FeedEntry.TABLE_ALUMNO+","+Contract.FeedEntry.TABLE_ACTIVIDAD,                     // The table to query
                projection,                               // The columns to return
                Contract.FeedEntry.COLUMN_NUMERO_CONTROL + " = "+Contract.FeedEntry.COLUMN_ID_ALUMNO_FK + " AND "+Contract.FeedEntry.COLUMN_NUMERO_CONTROL+ " = " +control.getText().toString(),                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                Contract.FeedEntry.COLUMN_NUMERO_CONTROL,                                     // don't group the rows
                null,                                     // don't filter by row groups
                Contract.FeedEntry.COLUMN_NOMBRE + " ASC"
        );
        c.moveToFirst();
        do{
            nombre.setText(c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_NOMBRE)));
            correo.setText(c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_CORREO)));
            telefono.setText(c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_TELEFONO)));
            carrera.setText(c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_CARRERA)));
            creditos.setText(c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_CREDITOS)));
        }while(c.moveToNext());
    }

    private void obtenerDatos() {
        SQLiteDatabase dbb = mDbHelper.getReadableDatabase();
        String[] projection = {
                Contract.FeedEntry.COLUMN_ID,
                Contract.FeedEntry.COLUMN_ACTIVIDAD,
                Contract.FeedEntry.COLUMN_FECHA_INICIO,
                Contract.FeedEntry.COLUMN_FECHA_FIN,
                Contract.FeedEntry.COLUMN_CREDITOS,
        };
        Cursor c = dbb.query(
                Contract.FeedEntry.TABLE_ACTIVIDAD,                     // The table to query
                projection,                               // The columns to return
                Contract.FeedEntry.COLUMN_ID_ALUMNO_FK + " like ?",                                // The columns for the WHERE clause
                new String[]{control.getText().toString()},                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,
                Contract.FeedEntry.COLUMN_FECHA_INICIO+ " ASC"
        );
        ID_Vector.removeAllElements();
        conceptosArray.removeAll(conceptosArray);

        if(c.getCount()<1){
            lista.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,new String [] {}));
            return;
        }
        c.moveToFirst();
        do{
            String actividad = c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_ACTIVIDAD));
            String ini = c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_FECHA_INICIO));
            String fin = c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_FECHA_FIN));
            String creditos = c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_CREDITOS));
            String id = c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_ID));

            if(actividad==null){

            }else {
                conceptosArray.add(actividad + "\nFecha de Inicio: " + ini + "\nFecha de Fin: " + fin + "\nCreditos: " + creditos);
                ID_Vector.add(id);
            }
        }while(c.moveToNext());
        lista.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,conceptosArray));
    }
}
