package com.kaiser.administracionpedidos.logica

class pedidos( var fecha_hora: com.google.firebase.Timestamp,
               var estado: String,
               var total: Double,
               var usuario: String,
               var id: String,
               var metodo_envio : String,
               var provincia : String,
               var ciudad : String,
               var direccion  : String,
               var metodo_pago : String,
               var local : String,
               var observaciones : String,
               var token : String)

{
    constructor() : this(
        com.google.firebase.Timestamp.now(),
        "",
        0.0,
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    )
}

