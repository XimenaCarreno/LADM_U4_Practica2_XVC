package mx.tecnm.tepic.ladm_u4_practica2_xvc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.widget.Toast

class smsReceiver : BroadcastReceiver(){
    var mensajeEnviar = ""
    override fun onReceive(context: Context?, intent: Intent?) {
        val extras = intent!!.extras

        if(extras!=null){
            var sms = extras!!.get("pdus") as Array<Any>

            for(indice in sms.indices)
            {
                val formato = extras!!.getString("format")

                var smsMensaje = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    SmsMessage.createFromPdu(sms[indice] as ByteArray,formato)
                }
                else{
                    SmsMessage.createFromPdu(sms[indice] as ByteArray)
                }

                var celularOrigen = smsMensaje.originatingAddress
                var contenidoSMS = smsMensaje.messageBody.toString()
                var mensaje = contenidoSMS.split(" ")

                Toast.makeText(context,"Entro contenido ${contenidoSMS}",Toast.LENGTH_LONG).show()
                if(mensaje.size != 3){
                    SmsManager.getDefault().sendTextMessage(celularOrigen,null,"Por favor envie: DISNEY seguido de un espacio, seguido de la pelicula, otro espacio y el tipo de frase. Ejemplo: DISNEY ELREYLEON AMOR",null,null)
                    return
                }
                else {
                    var opcion = mensaje[2]
                    if (!mensaje[0].equals("DISNEY")) {
                        SmsManager.getDefault().sendTextMessage(
                            celularOrigen,
                            null,
                            "Por favor, asegurese de enviar el mensaje de la manera correcta. Ejemplo: DISNEY ELREYLEON VILLANOS",
                            null,
                            null
                        )
                    }
                    else if(opcion.equals("AMOR") || opcion.equals("GRACIA") || opcion.equals("HEROES") || opcion.equals("LECCION") || opcion.equals("VILLANOS")){
                        try{
                            val cursor = BaseDatos(context, "BasePeliculas", null, 1)
                                .readableDatabase
                                .rawQuery("SELECT FRASE FROM FRASESDISNEY WHERE PELICULA = '${mensaje[1]}' AND TIPO ='${mensaje[2]}'",null)

                            if(cursor.moveToFirst()){
                                do {
                                    mensajeEnviar="Tu frase es: "+cursor.getString(0)
                                    SmsManager.getDefault().sendTextMessage(celularOrigen,null,mensajeEnviar,null,null)
                                }while(cursor.moveToNext())
                            }else{
                                SmsManager.getDefault().sendTextMessage(
                                    celularOrigen, null, "Lo sentimos. No hay una frase para ti esta vez.", null, null
                                )
                            }
                        }catch(e:SQLiteException){
                            Toast.makeText(context, e.message,Toast.LENGTH_LONG).show()
                        }
                        /*----*/
                    }
                    else{
                        SmsManager.getDefault().sendTextMessage(celularOrigen,null,
                            "El tipo de frase ingresado no existe. Intenta con alguno de estos: AMOR, GRACIA, HEROES, LECCION, VILLANOS",null,null)
                    }
                }
            }
        }
    }
}