package com.kaiser.administracionpedidos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.R
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.kaiser.administracionpedidos.logica.certificado
import com.kaiser.administracionpedidos.logica.solicitudes
import kotlinx.android.synthetic.main.activity_actividad_solicitudes2.*
import java.text.SimpleDateFormat
import java.util.*

class actividad_solicitudes : AppCompatActivity() {

    var nombre : String = ""
    var fecha : String = ""
    var usuario_id : String = ""
    var categoria : String = ""
    var url : String = ""
    var id : String = ""
    var token : String = ""
    lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.kaiser.administracionpedidos.R.layout.activity_actividad_solicitudes2)
        nombre = intent.getStringExtra("nombre")
        val aux_fecha = intent.getStringExtra("fecha")
        fecha = aux_fecha
        id = intent.getStringExtra("id")
        usuario_id = intent.getStringExtra("usuario_id")
        categoria = intent.getStringExtra("categoria")
        url = intent.getStringExtra("url")
        token = intent.getStringExtra("token")

        txt_solicitud_nombre.text = nombre
        txt_solicitud_fecha.text = fecha
        txt_solicitud_categoria.text = categoria
        Glide.with(this)
            .load(url)
            .into(iv_certificados)

        btn_aceptar_solicitud.setOnClickListener()
        {
            var soli_aux : certificado = certificado()
            soli_aux.estado = "aceptado"
            soli_aux.categoria = categoria
            soli_aux.certificado_url = url
            soli_aux.fecha_hora = com.google.firebase.Timestamp.now()
            soli_aux.token = token
            soli_aux.usuario_id = usuario_id
            database = FirebaseFirestore.getInstance()
            database.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

            database.collection("solicitud_categoria")
                .document(id)
                .set(soli_aux)
                .addOnSuccessListener { documentReference ->
                    run {
                        this.finish()
                    }

                }
        }

        btn_cancelar_solicitud.setOnClickListener()
        {
            var soli_aux : certificado = certificado()
            soli_aux.estado = "rechazado"
            soli_aux.categoria = categoria
            soli_aux.certificado_url = url
            soli_aux.fecha_hora = com.google.firebase.Timestamp.now()
            soli_aux.token = token
            soli_aux.usuario_id = usuario_id
            database = FirebaseFirestore.getInstance()
            database.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

            database.collection("solicitud_categoria")
                .document(id)
                .set(soli_aux)
                .addOnSuccessListener { documentReference ->
                    run {
                        this.finish()
                    }

                }
        }
    }
}
