package com.kaiser.administracionpedidos.logica

class solicitudes(
    var fecha_hora: com.google.firebase.Timestamp,
    var usuario_id : String,
    var certificado_url : String,
    var categoria : String,
    var estado : String,
    var token : String,
    var id : String,
    var nombre : String
) {
    constructor() : this (com.google.firebase.Timestamp.now(),
        "",
        "",
        "",
        "",
        "",
    "","")
}