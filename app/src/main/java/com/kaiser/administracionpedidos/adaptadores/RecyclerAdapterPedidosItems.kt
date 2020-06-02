package com.kaiser.administracionpedidos.adaptadores



import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.kaiser.administracionpedidos.R
import com.kaiser.administracionpedidos.actividad_pedido
import com.kaiser.administracionpedidos.logica.pedidos
import com.kaiser.administracionpedidos.logica.pedidos_Items
import kotlinx.android.synthetic.main.recyclerview_item_detalle_pedido.view.*
import kotlinx.android.synthetic.main.recyclerview_item_pedidos.view.*
import java.text.SimpleDateFormat
import java.util.*

class RecyclerAdapterPedidosItems(private val items: ArrayList<pedidos_Items>) : RecyclerView.Adapter<RecyclerAdapterPedidosItems.PhotoHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_detalle_pedido, parent, false)
        return PhotoHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val itemPhoto = items[position]
        holder.bindPhoto(itemPhoto)
    }

    //1
    class PhotoHolder(v: View) : RecyclerView.ViewHolder(v){
        //2
        private var view: View = v
        private var pronos: pedidos_Items? = null

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("RestrictedApi")
        fun bindPhoto(i: pedidos_Items) {
                view.txt_rv_detalle_pedido_precio.text = "$ ${i.importe.toString()}"
                view.txt_rv_detalle_pedido_cant.text = i.cantidad.toString()
                view.txt_rv_detalle_pedido_nombre.text = i.producto
        }

    }



}
