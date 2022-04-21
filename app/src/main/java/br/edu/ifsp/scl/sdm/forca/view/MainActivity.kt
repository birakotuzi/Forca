package br.edu.ifsp.scl.sdm.forca.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.sdm.forca.R
import br.edu.ifsp.scl.sdm.forca.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    companion object{
        const val  DIFICULDADE = "DIFICULDADE"
        const val  RODADAS = "RODADAS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)
        var dificuldade : Int
        dificuldade = 0
        var rodadas : Int
        rodadas = 0

        activityMainBinding.configurarBtn.setOnClickListener {
            val linearLayout = activityMainBinding.configurarCamposLl
            linearLayout.visibility = View.VISIBLE

            val cfgButton = activityMainBinding.configurarBtn
            cfgButton.visibility = View.INVISIBLE
        }

        activityMainBinding.dificuldadeRg.setOnCheckedChangeListener { radioGroup, _ ->
            when(radioGroup.checkedRadioButtonId){
                R.id.facilRb -> {
                    dificuldade = 1
                }
                R.id.medioRb -> {
                    dificuldade = 2
                }
                R.id.dificilRb -> {
                    dificuldade = 3
                }
            }
        }

        activityMainBinding.jogarBtn.setOnClickListener {
            with(activityMainBinding){
                var rodadasEt = this.rodadaEt.text.toString()
                if (!rodadasEt.isNullOrEmpty()) {
                    rodadas = rodadasEt.toInt()
                }

                if (dificuldade == 0 && rodadas == 0) {
                    Toast.makeText(applicationContext, "Informe a Dificuldade e a Quantidade de Rodadas do Jogo.", Toast.LENGTH_LONG).show()
                }
                else if (dificuldade > 0 && rodadas == 0) {
                    Toast.makeText(applicationContext, "Informe a Quantidade de Rodadas do Jogo.", Toast.LENGTH_LONG).show()
                }
                else if (dificuldade == 0 && rodadas > 0) {
                    Toast.makeText(applicationContext, "Informe a Dificuldade do Jogo.", Toast.LENGTH_LONG).show()
                }
                else if (dificuldade >= 0 && rodadas > 15) {
                    Toast.makeText(applicationContext, "Quantidade de Rodadas deve ser de 1 a 15.", Toast.LENGTH_LONG).show()
                }
                else {
                    val intent = Intent(applicationContext,JogarActivity::class.java)
                    intent.putExtra(DIFICULDADE, dificuldade)
                    intent.putExtra(RODADAS, rodadas)
                    startActivity(intent)
                }
            }
        }

    }

}