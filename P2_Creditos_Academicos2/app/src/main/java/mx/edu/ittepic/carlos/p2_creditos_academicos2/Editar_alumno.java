package mx.edu.ittepic.carlos.p2_creditos_academicos2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by carlos on 05/10/2017.
 */

public class Editar_alumno extends AppCompatActivity {
    EditText edtnombre,edtcarrera, edtcelular,edtemail,edtnocontrol;
    ImageButton foto,btn_guardar;
    final int REQUEST_CODE_GALLERY=999;
    BaseHelper mDbHelper;
    final Editar_alumno puntero = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar__alumno);

        mDbHelper = new BaseHelper(this.getApplicationContext());

        edtnombre = (EditText) findViewById(R.id.edit_nombre);
        edtcelular = (EditText) findViewById(R.id.edit_celular);
        edtemail = (EditText) findViewById(R.id.edit_email);
        btn_guardar = (ImageButton) findViewById(R.id.btn_guardar);
        foto = (ImageButton) findViewById(R.id.foto);
        edtcarrera = (EditText) findViewById(R.id.edit_carrera);
        edtnocontrol = (EditText) findViewById(R.id.edit_nocontrol);

        edtnocontrol.setText(getIntent().getExtras().getString("control"));
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtnombre.getText().toString().equals("") || edtemail.getText().toString().equals("") ||
                        edtcelular.getText().toString().equals("") || edtcarrera.getText().toString().equals("") ||
                        edtnocontrol.getText().toString().equals("")){
                    return;
                }

                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(Contract.FeedEntry.COLUMN_NOMBRE, edtnombre.getText().toString());
                values.put(Contract.FeedEntry.COLUMN_CORREO, edtemail.getText().toString());
                values.put(Contract.FeedEntry.COLUMN_TELEFONO, edtcelular.getText().toString());
                values.put(Contract.FeedEntry.COLUMN_CARRERA, edtcarrera.getText().toString());
                values.put(Contract.FeedEntry.COLUMN_NUMERO_CONTROL, edtnocontrol.getText().toString());
                db.insert(Contract.FeedEntry.TABLE_ALUMNO,null,values);


                long count = db.update(Contract.FeedEntry.TABLE_ALUMNO, values,Contract.FeedEntry.COLUMN_NUMERO_CONTROL + " like ?",new String[]{edtnocontrol.getText().toString()});
                puntero.finish();
            }
        });
        obtenerDatosAlumno();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        obtenerDatosAlumno();
    }

    private void obtenerDatosAlumno() {
        SQLiteDatabase dbb = mDbHelper.getReadableDatabase();
        String[] projection = {
                Contract.FeedEntry.COLUMN_NOMBRE,
                Contract.FeedEntry.COLUMN_CARRERA,
                Contract.FeedEntry.COLUMN_CORREO,
                Contract.FeedEntry.COLUMN_TELEFONO
        };
        Cursor c = dbb.query(
                Contract.FeedEntry.TABLE_ALUMNO,                     // The table to query
                projection,                               // The columns to return
                Contract.FeedEntry.COLUMN_NUMERO_CONTROL+ " = "+edtnocontrol.getText().toString(),                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null
        );
        c.moveToFirst();
        do{
            edtnombre.setText(c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_NOMBRE)));
            edtemail.setText(c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_CORREO)));
            edtcelular.setText(c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_TELEFONO)));
            edtcarrera.setText(c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_CARRERA)));
        }while(c.moveToNext());
    }
}
