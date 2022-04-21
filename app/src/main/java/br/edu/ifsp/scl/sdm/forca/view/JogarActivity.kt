package br.edu.ifsp.scl.sdm.forca.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.ifsp.scl.sdm.forca.R

class JogarActivity : AppCompatActivity() {

    companion object{
        const val  DIFICULDADE = "DIFICULDADE"
        const val  RODADAS = "RODADAS"
    }
    var dificuldade : Int = 0
    var rodadas : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jogar)

        dificuldade = this.intent.getSerializableExtra(DIFICULDADE) as Int
        rodadas = this.intent.getSerializableExtra(RODADAS) as Int

        android.widget.Toast.makeText(applicationContext, "Rodada "+ rodadas + ", Dificuldade " +  dificuldade , android.widget.Toast.LENGTH_LONG).show()
    }
}