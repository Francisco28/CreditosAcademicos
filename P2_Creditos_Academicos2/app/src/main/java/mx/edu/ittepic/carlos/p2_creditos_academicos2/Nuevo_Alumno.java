package mx.edu.ittepic.carlos.p2_creditos_academicos2;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by carlos on 05/10/2017.
 */

public class Nuevo_Alumno extends AppCompatActivity {

    EditText edtnombre,edtcarrera, edtcelular,edtemail,edtnocontrol;
    ImageButton foto,btn_guardar;
    final int REQUEST_CODE_GALLERY=999;
    BaseHelper mDbHelper;
    final Nuevo_Alumno puntero = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_alumno);

        edtnombre = (EditText) findViewById(R.id.edit_nombre);
        edtcelular = (EditText) findViewById(R.id.edit_celular);
        edtemail = (EditText) findViewById(R.id.edit_email);
        btn_guardar = (ImageButton) findViewById(R.id.btn_guardar);
        foto = (ImageButton) findViewById(R.id.foto);
        edtcarrera = (EditText) findViewById(R.id.edit_carrera);
        edtnocontrol = (EditText) findViewById(R.id.edit_nocontrol);
        mDbHelper = new BaseHelper(this.getApplicationContext());

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

                values.clear();
                values.put(Contract.FeedEntry.COLUMN_ID_ALUMNO_FK,edtnocontrol.getText().toString());
                values.put(Contract.FeedEntry.COLUMN_CREDITOS,0);
                db.insert(Contract.FeedEntry.TABLE_ACTIVIDAD,null,values);
                puntero.finish();

            }
        });

        //Listener para el boton seleccionar
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(Nuevo_Alumno.this, new String []{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_GALLERY);
            }
        });

    }


    //seleccionar imagen
    private byte[] imageViewTobyte(){
        Bitmap bitmap = ((BitmapDrawable) foto.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permission, @NonNull int[] grantResults){
        if (requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent (Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(),"Sin permiso",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode,permission,grantResults);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                foto.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    // fin de selelccionar
}

