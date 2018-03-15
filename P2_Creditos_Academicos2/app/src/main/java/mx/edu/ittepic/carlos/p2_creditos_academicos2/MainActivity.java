package mx.edu.ittepic.carlos.p2_creditos_academicos2;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    ArrayList conceptosArray;
    Vector ID_Vector;
    BaseHelper mDbHelper;
    ListView lista;
    final MainActivity puntero = this;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new BaseHelper(this.getApplicationContext());
        conceptosArray = new ArrayList();
        ID_Vector = new Vector();

        lista = (ListView)findViewById(R.id.Lista);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(puntero, Actividades.class);
                intent.putExtra("control",(ID_Vector.get(i)).toString());
                startActivityForResult(intent,2);
            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                id = Integer.parseInt(ID_Vector.get(position).toString());

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setMessage("¿Desea eliminar el registro?");
                alertDialog.setTitle("Advertencia");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Sí", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        Eliminar(id);
                        obtenerDatos();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });
                alertDialog.show();

                return false;
            }
        });

        FloatingActionButton boton = (FloatingActionButton)findViewById(R.id.nuevo);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(puntero, Nuevo_Alumno.class);
                startActivityForResult(intent,2);
            }
        });
        this.obtenerDatos();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.obtenerDatos();
    }

    private void obtenerDatos() {
        SQLiteDatabase dbb = mDbHelper.getReadableDatabase();
        String[] projection = {
                Contract.FeedEntry.COLUMN_NUMERO_CONTROL,
                Contract.FeedEntry.COLUMN_NOMBRE,
                "SUM("+Contract.FeedEntry.COLUMN_CREDITOS+") as CREDITOS"
        };
        Cursor c = dbb.query(
                Contract.FeedEntry.TABLE_ALUMNO+","+Contract.FeedEntry.TABLE_ACTIVIDAD,   // The table to query
                projection,                               // The columns to return
                Contract.FeedEntry.COLUMN_NUMERO_CONTROL + " = "+Contract.FeedEntry.COLUMN_ID_ALUMNO_FK,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                Contract.FeedEntry.COLUMN_NUMERO_CONTROL,                                     // don't group the rows
                null,                                     // don't filter by row groups
                Contract.FeedEntry.COLUMN_NOMBRE + " ASC"
        );
        ID_Vector.removeAllElements();
        conceptosArray.removeAll(conceptosArray);

        if(c.getCount()<1){
            lista.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,new String []{}));
            return;
        }
        c.moveToFirst();
        do{
            String id = c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_NUMERO_CONTROL));
            ID_Vector.add(id);
            String autor = c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_NOMBRE));
            String a = c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_CREDITOS));
            conceptosArray.add(autor+"\n"+"Creditos: "+a);
        }while(c.moveToNext());
        lista.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,conceptosArray));
    }

    //eliminar
  private void Eliminar (int Id){
      BaseHelper mDbHelper = new BaseHelper(this.getApplicationContext());
        //BaseHelper helper = new BaseHelper(this,"BDCreditosAcademicos.db",null,1);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
       mDbHelper = new BaseHelper(this.getApplicationContext());
        try{
            String sql = "DELETE FROM ALUMNOS WHERE COLUMN_NUMERO_CONTROL="+Id;
            db.execSQL(sql);
            db.close();
            Toast.makeText(this,"Registro borrado",Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(this,"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
  }
  //fin eliminar
}


