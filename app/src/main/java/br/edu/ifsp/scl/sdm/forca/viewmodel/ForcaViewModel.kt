package br.edu.ifsp.scl.sdm.forca.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.edu.ifsp.scl.sdm.forca.model.Dificuldade
import br.edu.ifsp.scl.sdm.forca.model.ForcaApi
import br.edu.ifsp.scl.sdm.forca.model.Palavra
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class ForcaViewModel(application: Application): AndroidViewModel(application)  {

    val identificadoresPalavrasDificuldade: MutableLiveData<Dificuldade> = MutableLiveData()
    val palavraMld: MutableLiveData<ArrayList<Palavra>> = MutableLiveData()
    var idPalavraLista: MutableLiveData<Int> = MutableLiveData()
    val palavrasLista : MutableList<Int> = ArrayList()
    private val escopoCorrotinas = CoroutineScope(Dispatchers.IO + Job())

    // Retrofit
    private val retrofit: Retrofit = Retrofit
        .Builder()
        .baseUrl("${URL_BASE}")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val forcaApi: ForcaApi = retrofit.create(ForcaApi::class.java)

    companion object {
        val URL_BASE = "http://nobile.pro.br/forcaws/"
    }

    fun getIdentificadoresDificuldade(id: Int){
        escopoCorrotinas.launch {
            forcaApi.retrieveIdentificadores(id).enqueue(object: Callback<Dificuldade> {
                override fun onResponse(call: Call<Dificuldade>, response: Response<Dificuldade>) {
                    identificadoresPalavrasDificuldade.postValue(response.body())
                }

                //@SuppressLint("LongLogTag")
                override fun onFailure(call: Call<Dificuldade>, t: Throwable) {
                    Log.e("$URL_BASE", t.message.toString())
                }
            })
        }
    }

    fun getIdentificadorPalavra(totalRodadas: Int){
        escopoCorrotinas.launch {
            var rodada: Int = 0
            while (rodada != totalRodadas) {
                val random = Random()
                val identificadorPalavraDif =
                    random.nextInt(identificadoresPalavrasDificuldade.value!!.size - 1)
                val idPalavra =
                    identificadoresPalavrasDificuldade.value!![identificadorPalavraDif].toString()
                if (!palavrasLista.contains(idPalavra.toInt())) {
                    palavrasLista.add(idPalavra.toInt())
                    idPalavraLista.postValue(idPalavra.toInt())
                    rodada++
                }
            }
        }
    }

    fun getPalavra(id: Int){
        escopoCorrotinas.launch {
            forcaApi.retrievePalavra(id).enqueue(object: Callback<ArrayList<Palavra>> {
                override fun onResponse(call: Call<ArrayList<Palavra>>, response: Response<ArrayList<Palavra>>) {
                    palavraMld.postValue(response.body())
                    //Log.e("erroPalavra", response.body()?.toMutableList().toString())
                }

                override fun onFailure(call: Call<ArrayList<Palavra>>, t: Throwable) {
                    Log.e("$URL_BASE", t.message.toString())
                }
            })
        }
    }
}