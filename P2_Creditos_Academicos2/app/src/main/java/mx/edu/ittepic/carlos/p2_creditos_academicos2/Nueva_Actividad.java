package mx.edu.ittepic.carlos.p2_creditos_academicos2;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
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

import java.util.Calendar;

/**
 * Created by carlos on 05/10/2017.
 */

public class Nueva_Actividad extends AppCompatActivity {

    Button btn_fechainicio,btn_fechafinal;
    ImageButton btn_guardar;
    private int mYear, mMonth, mDay;
    EditText edit_actividad,edit_creditos;
    BaseHelper mDbHelper;
    final Nueva_Actividad puntero = this;
    String control;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_actividad);

        btn_guardar = (ImageButton) findViewById(R.id.btn_guardar);
        btn_fechainicio = (Button) findViewById(R.id.btn_fechainicio);
        btn_fechafinal = (Button) findViewById(R.id.btn_fechafinal);
        edit_actividad = (EditText) findViewById(R.id.edit_actividad);
        edit_creditos = (EditText) findViewById(R.id.edit_creditos);

        control = getIntent().getExtras().getString("control");
        mDbHelper = new BaseHelper(this.getApplicationContext());


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
                values.put(Contract.FeedEntry.COLUMN_ID_ALUMNO_FK, control);
                db.insert(Contract.FeedEntry.TABLE_ACTIVIDAD,null,values);
                puntero.finish();
            }
        });

        btn_fechainicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(Nueva_Actividad.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                btn_fechainicio.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btn_fechafinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(Nueva_Actividad.this,
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
    }
}
