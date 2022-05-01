package br.edu.ifsp.scl.sdm.forca.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ForcaApi {

    @GET("identificadores/{id}")
    fun retrieveIdentificadores(@Path("id") id: Int): Call<Dificuldade>

    @GET("palavra/{pid}")
    fun retrievePalavra(@Path("pid") pid: Int): Call<ArrayList<Palavra>>
}