package mx.tecnm.tepic.ladm_u4_practica2_xvc

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        try {
            db.execSQL("CREATE TABLE FRASESDISNEY(PELICULA VARCHAR(50),TIPO VARCHAR(50),FRASE VARCHAR(200))")
        } catch (error: SQLiteException) {
            //SE DISPARA SI HAY UN ERROR EN EXECSQL
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}