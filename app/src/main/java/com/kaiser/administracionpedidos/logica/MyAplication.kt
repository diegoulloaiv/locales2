package com.kaiser.administracionpedidos.logica

import android.app.Application

class MyApplication: Application() {
    companion object {
        var nombre_usuario = ""
        var local = ""
    }

    override fun onCreate() {
        super.onCreate()
        // initialization code here
    }
}