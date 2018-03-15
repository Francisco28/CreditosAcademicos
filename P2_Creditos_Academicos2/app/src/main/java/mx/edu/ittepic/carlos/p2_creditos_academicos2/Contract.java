package mx.edu.ittepic.carlos.p2_creditos_academicos2;

import android.provider.BaseColumns;

/**
 * Created by carlos on 05/10/2017.
 */

public final class Contract {

    private Contract() {}

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_ALUMNO = "ALUMNO";
        public static final String COLUMN_NOMBRE = "NOMBRE";
        public static final String COLUMN_NUMERO_CONTROL = "NO_CONTROL";
        public static final String COLUMN_TELEFONO = "TELEFONO";
        public static final String COLUMN_CORREO = "CORREO";
        public static final String COLUMN_CARRERA = "CARRERA";

        public static final String TABLE_ACTIVIDAD = "ACTIVIDAD";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_ID_ALUMNO_FK = "ID_FK";
        public static final String COLUMN_FECHA_INICIO = "INICIO";
        public static final String COLUMN_FECHA_FIN = "FIN";
        public static final String COLUMN_CREDITOS = "CREDITOS";
        public static final String COLUMN_ACTIVIDAD = "ACTIVIDAD";
    }
}
