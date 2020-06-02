package com.kaiser.administracionpedidos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.kaiser.administracionpedidos.adaptadores.RecyclerAdapterPedidosItems
import com.kaiser.administracionpedidos.logica.pedidos
import com.kaiser.administracionpedidos.logica.pedidos_Items
import com.kaiser.administracionpedidos.logica.usuario
import kotlinx.android.synthetic.main.activity_pedido.*
import java.text.SimpleDateFormat
import java.util.*

class actividad_pedido : AppCompatActivity() {

    lateinit var p1 : pedidos
    lateinit var id : String
    lateinit var database: FirebaseFirestore
    var lista_items : MutableList<pedidos_Items>? = ArrayList<pedidos_Items>()
    private lateinit var adapter: RecyclerAdapterPedidosItems


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedido)
        ocultar_ui()
        id = intent.getStringExtra("id")
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_detalle_pedido.layoutManager = layoutManager
        adapter = RecyclerAdapterPedidosItems(lista_items as ArrayList<pedidos_Items>)
        rv_detalle_pedido.adapter = adapter
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        rv_detalle_pedido.addItemDecoration(decoration)

        buscar_datos_pedido()
        buscar_items_pedido()
        fab_avanzar.setOnClickListener()
        {
            val builder = AlertDialog.Builder(this,R.style.AlertDialogTheme)
            builder.setMessage("Seguro que desea avanzar el pedido al siguiente estado?")
                .setCancelable(false)
                .setPositiveButton("Si") { dialog, id ->
                    when (txt_estado_pedido.text) {
                        "nuevo" -> p1.estado = "aceptado"
                        "aceptado" -> if (txt_metodo_envio_pedido.text == "local")
                                        p1.estado = "listo"
                                        else
                                        p1.estado = "en camino"
                        "listo" -> p1.estado = "retirado"
                        "en camino" -> p1.estado = "entregado"
                    }

                    var db1 : FirebaseFirestore
                    db1 = FirebaseFirestore.getInstance()
                    db1.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
                    db1
                        .collection("pedidos")
                        .document(intent.getStringExtra("id"))
                        .set(p1)
                        .addOnSuccessListener { setResult(RESULT_OK);
                            finish();
                        }


                }
                    .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
                    val alert = builder.create()
                    alert.show()
        }
    }

    fun buscar_items_pedido()
    {
        var db1 : FirebaseFirestore
        db1 = FirebaseFirestore.getInstance()
        db1.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        db1
            .collection("pedidos_items")
            .whereEqualTo("pedido_id",id)
            .get()
            .addOnSuccessListener { items ->
                for (item in items)
                {
                    lista_items?.add(item.toObject(pedidos_Items::class.java))
                    adapter.notifyItemInserted(lista_items?.size!! -1)
                }
            }
    }

    fun buscar_datos_pedido()
    {
        database = FirebaseFirestore.getInstance()
        database.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        database
            .collection("pedidos")
            .document(id)
            .get()
            .addOnSuccessListener { item ->
                var resultado = item.toObject(pedidos::class.java)
                if (resultado != null) {
                    p1 = resultado
                }
                if (resultado != null) {
                    val date = Date(resultado!!.fecha_hora.seconds * 1000L)
                    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                    sdf.timeZone = TimeZone.getTimeZone("GMT-4")
                    val formattedDate = sdf.format(date)
                    txt_fecha_hora_pedido.text = formattedDate
                    txt_estado_pedido.text = resultado.estado
                    if (resultado.estado == "entregado" || resultado.estado == "retirado")
                        fab_avanzar.isEnabled = false
                    txt_total_pedido.text = "$ " + resultado.total.toString()
                    txt_metodo_envio_pedido.text = "Metodo de Envio: " + resultado.metodo_envio
                    txt_metodo_pago.text = "Metodo de Pago: " + resultado.metodo_pago
                    txt_provincia_ciudad_pedido.text = resultado.ciudad + ", " + resultado.provincia
                            txt_direccion.text = resultado.direccion
                    txt_observaciones_pedido.text = resultado.observaciones
                    buscar_info_cliente(resultado.usuario)
                }
            }
    }

    fun ocultar_ui()
    {
        pg_pedido.visibility = View.VISIBLE
        txt_metodo_pago.visibility = View.INVISIBLE
        txt_fecha_hora_pedido.visibility = View.INVISIBLE
        txt_estado_pedido.visibility = View.INVISIBLE
        txt_metodo_envio_pedido.visibility = View.INVISIBLE
        txt_total_pedido.visibility = View.INVISIBLE
        txt_nombre_pedido.visibility = View.INVISIBLE
        txt_telefono_pedido.visibility = View.INVISIBLE
        rv_detalle_pedido.visibility = View.INVISIBLE
        txt_provincia_ciudad_pedido.visibility = View.INVISIBLE
        txt_direccion.visibility = View.INVISIBLE
        txt_observaciones_pedido.visibility = View.INVISIBLE
    }

    fun mostrar_ui()
    {
        pg_pedido.visibility = View.INVISIBLE
        txt_metodo_pago.visibility = View.VISIBLE
        txt_fecha_hora_pedido.visibility = View.VISIBLE
        txt_estado_pedido.visibility = View.VISIBLE
        txt_metodo_envio_pedido.visibility = View.VISIBLE
        txt_total_pedido.visibility = View.VISIBLE
        txt_nombre_pedido.visibility = View.VISIBLE
        txt_telefono_pedido.visibility = View.VISIBLE
        rv_detalle_pedido.visibility = View.VISIBLE
        txt_provincia_ciudad_pedido.visibility = View.VISIBLE
        txt_direccion.visibility = View.VISIBLE
        txt_observaciones_pedido.visibility = View.VISIBLE
    }

    fun buscar_info_cliente(id : String)
    {
        var db: FirebaseFirestore =FirebaseFirestore.getInstance()
        db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        db
            .collection("usuarios")
            .whereEqualTo("usuario_id",id)
            .get()
            .addOnSuccessListener { resultados ->
                for (resultado in resultados) {
                    var aux = resultado.toObject(usuario::class.java)
                    if (aux != null) {
                        txt_nombre_pedido.text = aux.nombre
                        txt_telefono_pedido.text = aux.telefono.toString()
                        mostrar_ui()
                    }
                }
            }

    }
}
