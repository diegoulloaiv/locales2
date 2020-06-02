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
import com.kaiser.administracionpedidos.actividad_solicitudes
import com.kaiser.administracionpedidos.logica.certificado
import com.kaiser.administracionpedidos.logica.pedidos
import com.kaiser.administracionpedidos.logica.solicitudes
import kotlinx.android.synthetic.main.recyclerview_item_pedidos.view.*
import kotlinx.android.synthetic.main.recyclerview_item_solicitudes.view.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class RecyclerAdapterSolicitudes(private val solicitudes: ArrayList<solicitudes>) : RecyclerView.Adapter<RecyclerAdapterSolicitudes.PhotoHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_solicitudes, parent, false)
        return PhotoHolder(v)
    }

    override fun getItemCount(): Int {
        return solicitudes.size
    }

    fun onActivityResult(requestCode: Int, resultCode: Int) {
        this.notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val itemPhoto = solicitudes[position]
        holder.bindPhoto(itemPhoto)
    }

    //1
    class PhotoHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        //2
        private var view: View = v
        private var soli: solicitudes? = null

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("RestrictedApi")
        fun bindPhoto(i: solicitudes) {
            this.soli = i
            view.txt_nombre_solicitud.text = i.nombre
            view.txt_categoria_solicitud.text = i.categoria
        }


        //3
        init {
            v.setOnClickListener(this)

        }

        override fun onClick(p0: View?) {
            val context: Context = itemView.context
            val intent = Intent(context, actividad_solicitudes::class.java)
            intent.putExtra("categoria",soli?.categoria)
            intent.putExtra("url",soli?.certificado_url)
            intent.putExtra("estado",soli?.estado)
            var long : Long = 0
            long = soli?.fecha_hora?.seconds!!
            val date = Date(long * 1000L)
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            sdf.timeZone = TimeZone.getTimeZone("GMT-4")
            val formattedDate = sdf.format(date)


            intent.putExtra("fecha", formattedDate.toString())
            intent.putExtra("nombre",soli?.nombre)
            intent.putExtra("usuario_id",soli?.usuario_id)
            intent.putExtra("id", soli?.id)
            intent.putExtra("token",soli?.token)
            context.startActivity(intent)
        }


    }



}
