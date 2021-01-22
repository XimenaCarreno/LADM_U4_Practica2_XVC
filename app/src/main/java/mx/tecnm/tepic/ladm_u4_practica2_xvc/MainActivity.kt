package mx.tecnm.tepic.ladm_u4_practica2_xvc

import android.content.pm.PackageManager
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val siPermiso = 1
    val siPermisoReceiver = 2
    val siPermisoLectura = 3

    var baseDatos=BaseDatos(this,"BasePeliculas", null, 1)
    var listaID=ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //--------------------------Permisos-----------------------------------------

        //Recibir mensajes
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.RECEIVE_SMS),siPermisoReceiver)
        }
        //Leer mensajes
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_SMS),siPermisoLectura)
        }
        //Enviar mensajes
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.SEND_SMS), siPermiso
            )
        }

        button.setOnClickListener {
            insertar()
        }

        button2.setOnClickListener {
            mostrarFrases()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==siPermiso){
        }

        if(requestCode==siPermisoReceiver){
        }

        if(requestCode==siPermisoLectura){
        }
    }

    private fun mostrarFrases() {
        try{
            var trans=baseDatos.readableDatabase
            var frasesR=ArrayList<String>()
            var respuesta=trans.query("FRASESDISNEY", arrayOf("*"),null,null,null,null,null)
            listaID.clear()
            if (respuesta.moveToFirst()){
                do{
                    var concatenacion="PELICULA: ${respuesta.getString(0)}\nTIPO DE FRASE: ${respuesta.getString(1)}\nFRASE: ${respuesta.getString(2)}"
                    frasesR.add(concatenacion)
                    //listaID.add(respuesta.getInt(0).toString())
                }while (respuesta.moveToNext())

            }else{
                frasesR.add("Las frases volaron a la segunda estrella directo al amanecer, y ya no están aquí")
                //2 POSIBLES RESULTADOS
                //  1=TODAS LAS TUPLAS RESULTADO
                //  2=NO HAY PERSONAS INSERTADAS
            }
            Frases.adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,frasesR)
            trans.close()
        }catch (e:SQLiteException){mensaje("ERROR: "+e.message!!)}
    }

    private fun insertar() {
        var pelicula = txtPelicula.text.toString().toUpperCase()
        var tipo = txtTipo.text.toString().toUpperCase()
        var frase = txtFrase.text.toString().toUpperCase()

        if(tipo!="AMOR"||tipo!="GRACIA"||tipo!="HEROES"||tipo!="LECCION"||tipo!="VILLANOS"){

        }

        try{
            var baseDatos = BaseDatos (this, "BasePeliculas", null, 1)

            var insertar = baseDatos.writableDatabase
            var SQL = "INSERT INTO FRASESDISNEY VALUES('${pelicula}','${tipo}','${frase}')"

            insertar.execSQL(SQL)
            baseDatos.close()

            mensaje("Frase añadida con exito")
            txtPelicula.setText("")
            txtTipo.setText("")
            txtFrase.setText("")


        }catch (error: SQLiteException){
            mensaje(error.message.toString())
        }
    }

    fun mensaje(S:String){
        AlertDialog.Builder(this)
            .setMessage(S)
            .show()
    }
}