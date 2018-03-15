package mx.edu.ittepic.carlos.p2_creditos_academicos2;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;

/**
 * Created by carlos on 05/10/2017.
 */

public class Editar_actividad extends AppCompatActivity {
    Button btn_fechainicio,btn_fechafinal;
    ImageButton btn_guardar;
    //private int mYear, mMonth, mDay;
    EditText edit_actividad,edit_creditos;
    BaseHelper mDbHelper;
    final Editar_actividad puntero = this;
    String control;
    DatePickerDialog datePickerDialog;
    String ID;

    int mYear;
    int mMonth;
    int mDay;
    int amYear;
    int amMonth;
    int amDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar__actividad);

        btn_guardar = (ImageButton) findViewById(R.id.btn_guardar);
        btn_fechainicio = (Button) findViewById(R.id.btn_fechainicio);
        btn_fechafinal = (Button) findViewById(R.id.btn_fechafinal);
        edit_actividad = (EditText) findViewById(R.id.edit_actividad);
        edit_creditos = (EditText) findViewById(R.id.edit_creditos);
        control = getIntent().getExtras().getString("control");
        mDbHelper = new BaseHelper(this.getApplicationContext());
        ID = getIntent().getExtras().getString("id");

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edit_actividad.getText().toString().equals("") || btn_fechainicio.getText().toString().equals("") ||
                        btn_fechafinal.getText().toString().equals("")||edit_creditos.getText().toString().equals("")){
                    return;
                }

                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(Contract.FeedEntry.COLUMN_ACTIVIDAD, edit_actividad.getText().toString());
                values.put(Contract.FeedEntry.COLUMN_FECHA_INICIO, btn_fechainicio.getText().toString());
                values.put(Contract.FeedEntry.COLUMN_FECHA_FIN, btn_fechafinal.getText().toString());
                values.put(Contract.FeedEntry.COLUMN_CREDITOS, edit_creditos.getText().toString());
                db.update(Contract.FeedEntry.TABLE_ACTIVIDAD,values,Contract.FeedEntry.COLUMN_ID + "= ?",new String[]{ID});
                puntero.finish();
            }
        });


        btn_fechainicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // date picker dialog
                datePickerDialog = new DatePickerDialog(Editar_actividad.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                btn_fechainicio.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, amYear, amMonth, amDay);
                datePickerDialog.show();
            }
        });

        btn_fechafinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // date picker dialog
                datePickerDialog = new DatePickerDialog(Editar_actividad.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                btn_fechafinal.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        obtenerDatosActividad();
    }
    private void obtenerDatosActividad() {
        SQLiteDatabase dbb = mDbHelper.getReadableDatabase();
        String[] projection = {
                Contract.FeedEntry.COLUMN_ACTIVIDAD,
                Contract.FeedEntry.COLUMN_FECHA_INICIO,
                Contract.FeedEntry.COLUMN_FECHA_FIN,
                Contract.FeedEntry.COLUMN_CREDITOS
        };
        Cursor c = dbb.query(
                Contract.FeedEntry.TABLE_ACTIVIDAD,
                projection,                               // The columns to return
                Contract.FeedEntry.COLUMN_ID + " = "+ID,
                null,                            // The values for the WHERE clause
                null,
                null,                                     // don't filter by row groups
                null
        );
        c.moveToFirst();
        do{
            edit_actividad.setText(c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_ACTIVIDAD)));
            btn_fechainicio.setText(c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_FECHA_INICIO)));
            amYear = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_FECHA_INICIO)).split("/")[2]);
            amMonth = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_FECHA_INICIO)).split("/")[1]);
            amDay = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_FECHA_INICIO)).split("/")[0]);
            mYear = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_FECHA_FIN)).split("/")[2]);
            mMonth = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_FECHA_FIN)).split("/")[1]);
            mDay = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_FECHA_FIN)).split("/")[0]);
            btn_fechafinal.setText(c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_FECHA_FIN)));
            edit_creditos.setText(c.getString(c.getColumnIndexOrThrow(Contract.FeedEntry.COLUMN_CREDITOS)));

        }while(c.moveToNext());
    }
}

