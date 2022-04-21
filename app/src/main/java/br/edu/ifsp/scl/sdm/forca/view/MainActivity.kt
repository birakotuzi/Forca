package br.edu.ifsp.scl.sdm.forca.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.sdm.forca.R
import br.edu.ifsp.scl.sdm.forca.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var configurarActivityResultLauncher: ActivityResultLauncher<Intent>

    companion object{
        const val  DIFICULDADE = "DIFICULDADE"
        const val  RODADAS = "RODADAS"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)
        var dificuldade : Int = 0
        var rodadas : Int = 0

        configurarActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ resultado ->
            if (resultado?.resultCode == RESULT_OK){
                with(resultado){
                    data?.getIntExtra(DIFICULDADE, 0).takeIf { it!=null }.let {
                        dificuldade = it.toString().toInt()
                    }
                    data?.getIntExtra(RODADAS, 0).takeIf { it!=null }.let {
                        rodadas = it.toString().toInt()
                    }
                }
                Toast.makeText(applicationContext, dificuldade.toString(), Toast.LENGTH_LONG).show()
                Toast.makeText(applicationContext, rodadas.toString(), Toast.LENGTH_LONG).show()
                if ((dificuldade > 0) && (rodadas > 0)) {
                    activityMainBinding.titleTv.text = dificuldade.toString()
                }
            }
        }

        activityMainBinding.configurarBtn.setOnClickListener {
            val linearLayout = activityMainBinding.configurarCamposLl
            linearLayout.visibility = View.VISIBLE
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
                rodadas = this.rodadaEt.text.toString().toInt()

                val intent = Intent(applicationContext,JogarActivity::class.java)
                intent.putExtra(DIFICULDADE, dificuldade)
                intent.putExtra(RODADAS, rodadas)
                startActivity(intent)
                /*if ((rodadas == 0) && (dificuldade == 0)) {
                    android.widget.Toast.makeText(applicationContext, "Informe a Dificuldade e a Quantidade de Rodadas do Jogo.", android.widget.Toast.LENGTH_SHORT).show()
                } else if ((rodadas == 0) && (dificuldade > 0)) {
                    android.widget.Toast.makeText(applicationContext, "Informe a Quantidade de Rodadas do Jogo.", android.widget.Toast.LENGTH_SHORT).show()
                } else if ((rodadas > 0) && (dificuldade == 0)) {
                    android.widget.Toast.makeText(applicationContext, "Informe a Dificuldade do Jogo.", android.widget.Toast.LENGTH_SHORT).show()
                } else { // rodadas > 0 && dificuldade > 0

                }*/
            }
        }

    }

}