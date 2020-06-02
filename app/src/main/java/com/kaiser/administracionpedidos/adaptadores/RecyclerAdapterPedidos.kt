package com.kaiser.administracionpedidos.adaptadores

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.kaiser.administracionpedidos.R
import com.kaiser.administracionpedidos.actividad_pedido
import com.kaiser.administracionpedidos.logica.pedidos
import kotlinx.android.synthetic.main.recyclerview_item_pedidos.view.*
import java.text.SimpleDateFormat
import java.util.*


class RecyclerAdapterPedidos(private val pedidos: ArrayList<pedidos>) : RecyclerView.Adapter<RecyclerAdapterPedidos.PhotoHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_pedidos, parent, false)
        return PhotoHolder(v)

        //val inflatedView = parent.inflate(R.layout.recyclerview_item_pedidos, false)
        //return PhotoHolder(inflatedView)

    }

    override fun getItemCount(): Int {
        return pedidos.size
    }

    fun onActivityResult(requestCode: Int, resultCode: Int) {
        this.notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val itemPhoto = pedidos[position]
        holder.bindPhoto(itemPhoto)
    }

    //1
    class PhotoHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        //2
        private var view: View = v
        private var pronos: pedidos? = null

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("RestrictedApi")
        fun bindPhoto(i: pedidos) {
            this.pronos = i
            val date = Date(i!!.fecha_hora.seconds * 1000L)
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            sdf.timeZone = TimeZone.getTimeZone("GMT-4")
            val formattedDate = sdf.format(date)
            view.txt_rv_fecha_hora.text= formattedDate
            view.txt_rv_estado.text = i!!.estado.toString().toUpperCase()
            view.txt_rv_metodo_envio.text = i!!.metodo_envio.toUpperCase()
            view.txt_rv_importe.text = "$ " + i!!.total.toString()
            if (i!!.estado.toString() == "nuevo")
                view.iv_alerta.visibility = View.VISIBLE
            else
                view.iv_alerta.visibility = View.INVISIBLE
        }


        //3
        init {
            v.setOnClickListener(this)

        }


        companion object {
            //5
            private val PHOTO_KEY = "PRODUCTO"
        }

        override fun onClick(p0: View?) {
            val context: Context = itemView.context
            val intent = Intent(context, actividad_pedido::class.java)

            intent.putExtra("id", pronos?.id)
            (context as Activity).startActivityForResult(intent, 911)
//            context.startActivity(intent)
        }


    }



}
