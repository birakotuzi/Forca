package br.edu.ifsp.scl.sdm.forca.model

import com.google.gson.annotations.SerializedName

data class Palavra(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Palavra")
    val palavra: String,
    @SerializedName("Letras")
    val letras: Int,
    @SerializedName("Nivel")
    val nivel: Int
)
