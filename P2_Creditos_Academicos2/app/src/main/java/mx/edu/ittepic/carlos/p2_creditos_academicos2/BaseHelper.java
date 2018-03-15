package mx.edu.ittepic.carlos.p2_creditos_academicos2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by carlos on 05/10/2017.
 */

public class BaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "BDCreditosAcademicos.db";
    private static final String TEXT_TYPE = " TEXT ";
    private static final String INT_TYPE = " INTEGER ";
    private static final String DATE_TYPE = " DATE ";
    private static final String COMMA_SEP = " , ";
    private static final String SQL_CREATE_TABLA_ALUMNOS =
            "CREATE TABLE " + Contract.FeedEntry.TABLE_ALUMNO + " (" +
                    Contract.FeedEntry.COLUMN_NUMERO_CONTROL + TEXT_TYPE + " PRIMARY KEY "+ COMMA_SEP +
                    Contract.FeedEntry.COLUMN_NOMBRE + TEXT_TYPE + COMMA_SEP +
                    Contract.FeedEntry.COLUMN_CORREO + TEXT_TYPE + COMMA_SEP+
                    Contract.FeedEntry.COLUMN_CARRERA + TEXT_TYPE + COMMA_SEP+
                    Contract.FeedEntry.COLUMN_TELEFONO + TEXT_TYPE+" );";

    private static final String SQL_CREATE_TABLA_ACTIVIDAD =
            " CREATE TABLE " + Contract.FeedEntry.TABLE_ACTIVIDAD + " (" +
                    Contract.FeedEntry.COLUMN_ACTIVIDAD + TEXT_TYPE + COMMA_SEP+
                    Contract.FeedEntry.COLUMN_ID + INT_TYPE+ " PRIMARY KEY AUTOINCREMENT " + COMMA_SEP+
                    Contract.FeedEntry.COLUMN_ID_ALUMNO_FK + TEXT_TYPE + COMMA_SEP+
                    Contract.FeedEntry.COLUMN_FECHA_INICIO + DATE_TYPE + COMMA_SEP+
                    Contract.FeedEntry.COLUMN_FECHA_FIN + DATE_TYPE + COMMA_SEP+
                    Contract.FeedEntry.COLUMN_CREDITOS + INT_TYPE +COMMA_SEP+
                    "FOREIGN KEY("+Contract.FeedEntry.COLUMN_ID_ALUMNO_FK+") REFERENCES "+Contract.FeedEntry.TABLE_ALUMNO+"("+Contract.FeedEntry.COLUMN_NUMERO_CONTROL+"));";

    private static final String SQL_DELETE_ALUMNOS = "DROP TABLE IF EXISTS " + Contract.FeedEntry.TABLE_ALUMNO+";";
    private static final String SQL_DELETE_ACTIVIDADES ="DROP TABLE IF EXISTS " + Contract.FeedEntry.TABLE_ACTIVIDAD+";";
    public BaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLA_ALUMNOS);
        db.execSQL(SQL_CREATE_TABLA_ACTIVIDAD);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ACTIVIDADES);
        db.execSQL(SQL_DELETE_ALUMNOS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}