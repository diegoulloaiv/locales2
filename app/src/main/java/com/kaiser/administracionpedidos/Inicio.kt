package com.kaiser.administracionpedidos

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.kaiser.administracionpedidos.adaptadores.RecyclerAdapterPedidos
import com.kaiser.administracionpedidos.adaptadores.RecyclerAdapterSolicitudes
import com.kaiser.administracionpedidos.logica.*
import kotlinx.android.synthetic.main.activity_main.*


class Inicio : AppCompatActivity() {

    lateinit var database: FirebaseFirestore
    val db = FirebaseFirestore.getInstance()
    var db2 = FirebaseFirestore.getInstance()
    var db3 = FirebaseFirestore.getInstance()
    var lista_pedidos : MutableList<pedidos>? = ArrayList<pedidos>()
    private lateinit var adapter: RecyclerAdapterPedidos
    private lateinit var adapter2: RecyclerAdapterSolicitudes
    var filtro_seleccionado : String = "nuevo"
    var listado_solicitudes : MutableList<solicitudes>? = ArrayList<solicitudes>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ocultar_ui()

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build())

        grupo_filtro_pedidos.check(filtro_todos.id)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_pedidos .layoutManager = layoutManager
        adapter = RecyclerAdapterPedidos(lista_pedidos as ArrayList<pedidos>)
        rv_pedidos.adapter = adapter
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        rv_pedidos.addItemDecoration(decoration)

        val layoutManager2 = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_solicitudes.layoutManager = layoutManager2
        adapter2 = RecyclerAdapterSolicitudes(listado_solicitudes as ArrayList<solicitudes>)
        rv_solicitudes.adapter = adapter2
        val decoration2 = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        rv_solicitudes.addItemDecoration(decoration2)

        buscar_solicitudes()

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            1)

        grupo_filtro_pedidos.setOnCheckedChangeListener(ChipGroup.OnCheckedChangeListener { chipGroup, i ->
            ocultar_ui()
            val chip: Chip = chipGroup.findViewById(i)
            if (chip != null) {
                lista_pedidos?.clear()
                adapter.notifyDataSetChanged()
                filtro_seleccionado = chip.text.toString().toLowerCase()
                buscar_pedidos()
            }
        })

    }

    fun buscar_solicitudes()
    {
        db2 = FirebaseFirestore.getInstance()
        db2.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        db2
            .collection("solicitud_categoria")
            .whereEqualTo("estado","nuevo")
            .get()
            .addOnSuccessListener { items ->
                for (item in items) {
                    var aux = item.toObject(certificado::class.java)
                    //Toast.makeText(this,aux.estado,Toast.LENGTH_SHORT).show()
                    db3 = FirebaseFirestore.getInstance()
                    db3.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
                    db3
                        .collection("usuarios")
                        .document(aux.usuario_id)
                        .get()
                        .addOnSuccessListener {i ->
                            var aux2 = i.toObject(usuario::class.java)
                            var solicitud_aux : solicitudes = solicitudes()
                            solicitud_aux.categoria = aux.categoria
                            solicitud_aux.certificado_url = aux.certificado_url
                            solicitud_aux.estado = aux.estado
                            solicitud_aux.fecha_hora = aux.fecha_hora
                            solicitud_aux.id = item.id
                            if (aux2 != null) {
                                solicitud_aux.nombre = aux2.nombre
                            }
                            solicitud_aux.token = aux.token
                            solicitud_aux.usuario_id = aux.usuario_id
                            listado_solicitudes?.add(solicitud_aux)
                            adapter2.notifyDataSetChanged()
                        }
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var bandera = false
        if (requestCode == 1) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    db.collection("operadores")
                        .whereEqualTo("nombre_usuario",user.email)
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                               bandera = true
                                var aux = document.toObject(operadores::class.java)
                                MyApplication.local  = aux.local
                                MyApplication.nombre_usuario = aux.nombre_usuario
                                break
                            }
                        }
                        .addOnFailureListener { exception ->

                        }
                        .addOnCompleteListener {
                            if (bandera) {

                                continuar_create()
                            }
                     //       else
                       //         Toast.makeText(this,"Usuario invalido",Toast.LENGTH_LONG).show()
                        }
                }

            } else {

            }
        }
        if(requestCode == 911)
            adapter.onActivityResult(requestCode,1)
    }

    fun continuar_create()
    {

        buscar_pedidos()
    }

    fun buscar_pedidos()
    {
       // Toast.makeText(this,filtro_seleccionado,Toast.LENGTH_SHORT).show()
        database = FirebaseFirestore.getInstance()
        database.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        if (filtro_seleccionado == "todos")
        {
            database
                .collection("pedidos")
                .whereEqualTo("local",MyApplication.local)
                //.orderBy("dateTime")
                .get()
                .addOnSuccessListener { items ->
                    for (item in items)
                    {
                        var aux = item.toObject(pedidos::class.java)
                        aux.id = item.id
                        lista_pedidos?.add(aux)
                        adapter.notifyItemInserted(lista_pedidos?.size!! -1)
                    }
                    //Toast.makeText(this,"a",Toast.LENGTH_SHORT)
                    mostrar_ui()
                }
        }
            else {
            database
                .collection("pedidos")
                .whereEqualTo("local", MyApplication.local)
                .whereEqualTo("estado", filtro_seleccionado)
                //.orderBy("dateTime")
                .get()
                .addOnSuccessListener { items ->
                    for (item in items) {
                        var aux = item.toObject(pedidos::class.java)
                        aux.id = item.id
                        lista_pedidos?.add(aux)
                        adapter.notifyItemInserted(lista_pedidos?.size!! - 1)
                    }
                    mostrar_ui()
                }
        }
    }

    fun ocultar_ui(){
        cargado.visibility = View.VISIBLE
       // grupo_filtro_pedidos.visibility = View.INVISIBLE
        rv_pedidos.visibility = View.INVISIBLE
       // textView2.visibility = View.INVISIBLE
    }

    fun mostrar_ui()
    {
        cargado.visibility = View.INVISIBLE
        //grupo_filtro_pedidos.visibility = View.VISIBLE
        rv_pedidos.visibility = View.VISIBLE
    //    textView2.visibility = View.VISIBLE
    }
}
