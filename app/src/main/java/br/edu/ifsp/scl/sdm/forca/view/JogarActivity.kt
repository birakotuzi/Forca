package br.edu.ifsp.scl.sdm.forca.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import br.edu.ifsp.scl.sdm.forca.R
import br.edu.ifsp.scl.sdm.forca.databinding.ActivityJogarBinding
import br.edu.ifsp.scl.sdm.forca.viewmodel.ForcaViewModel
import java.text.Normalizer
import java.util.regex.Pattern

class JogarActivity : AppCompatActivity() {

    private val activityJogarBinding: ActivityJogarBinding by lazy {
        ActivityJogarBinding.inflate(layoutInflater)
    }

    companion object{
        const val  DIFICULDADE = "DIFICULDADE"
        const val  RODADAS = "RODADAS"
    }
    private var dificuldade : Int = 0
    private var rodadas : Int = 0

    private var letrasSelecionadas : String = "Letras: "
    private var letrasCertas : String = "Acertou: "
    private var letrasErradas : String = "Errou: "
    private var rodada : Int = 0
    private var rodadaRelatorio : Int = 0
    private var palavraAtual : String = ""

    private var acertosRodada : Int = 0
    private var errosRodada : Int = 0

    private var totalAcertos : Int = 0
    private var totalErros : Int = 0

    private var palavraSemTratamento : String = ""

    private var mascaraMap = mutableMapOf<Int, String>()

    private lateinit var forcaViewModel: ForcaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityJogarBinding.root)

        esconderParteCorpo()

        forcaViewModel = ViewModelProvider
            .AndroidViewModelFactory(this.application)
            .create(ForcaViewModel::class.java)

        dificuldade = this.intent.getSerializableExtra(DIFICULDADE) as Int
        rodadas = this.intent.getSerializableExtra(RODADAS) as Int

        //android.widget.Toast.makeText(applicationContext, "Rodada "+ rodadas + ", Dificuldade " +  dificuldade , android.widget.Toast.LENGTH_LONG).show()

        forcaViewModel = ViewModelProvider
            .AndroidViewModelFactory(this.application)
            .create(ForcaViewModel::class.java)
        resetTeclado(false)
        selecionarLetraTeclado()

        if (rodada == 0) {
            forcaViewModel.getIdentificadoresDificuldade(dificuldade)

            forcaViewModel.identificadoresPalavrasDificuldade.observe(this){
                forcaViewModel.getIdentificadorPalavra(rodadas)
            }
            carregarDadosForca()
        }


        activityJogarBinding.jogarNovamenteBtn.setOnClickListener {
            with(activityJogarBinding){
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
            }
        }

    }

    fun carregarDadosForca(){

        forcaViewModel.idPalavraLista.observe(this){
            if (palavraAtual == ""){
                forcaViewModel.getPalavra(forcaViewModel.palavrasLista[rodada])
            }
        }

        forcaViewModel.palavraMld.observe(this){ lista ->
            lista.forEach { palavra ->
                palavra.palavra.also { palavra ->
                    palavraSemTratamento = palavra
                    palavraAtual = tratarCaracteres(palavra).toString()
                    activityJogarBinding.palavraTv.text = palavraAtual
                    mascaraPalavra(palavraAtual)
                }
            }
        }
        resetTeclado(true)
        rodadaRelatorio = rodada + 1
        activityJogarBinding.totalRodadasTv.text = "Rodada " + rodadaRelatorio + " de " + rodadas + " rodadas"
    }

    fun mascaraPalavra(palavra: String){
        var mascara = ""
        for(i in 0 until palavra.length) {
            mascara = mascara + "-"
            mascaraMap[i] = "-"
        }
        activityJogarBinding.palavraTv.text = mascara
    }

    fun selecionarLetraTeclado() {
        with(activityJogarBinding) {
            letraABtn.setOnClickListener { cliqueLetra("A") }
            letraBBtn.setOnClickListener { cliqueLetra("B") }
            letraCBtn.setOnClickListener { cliqueLetra("C") }
            letraDBtn.setOnClickListener { cliqueLetra("D") }
            letraEBtn.setOnClickListener { cliqueLetra("E") }
            letraFBtn.setOnClickListener { cliqueLetra("F") }
            letraGBtn.setOnClickListener { cliqueLetra("G") }
            letraHBtn.setOnClickListener { cliqueLetra("H") }
            letraIBtn.setOnClickListener { cliqueLetra("I") }
            letraJBtn.setOnClickListener { cliqueLetra("J") }
            letraKBtn.setOnClickListener { cliqueLetra("K") }
            letraLBtn.setOnClickListener { cliqueLetra("L") }
            letraMBtn.setOnClickListener { cliqueLetra("M") }
            letraNBtn.setOnClickListener { cliqueLetra("N") }
            letraOBtn.setOnClickListener { cliqueLetra("O") }
            letraPBtn.setOnClickListener { cliqueLetra("P") }
            letraQBtn.setOnClickListener { cliqueLetra("Q") }
            letraRBtn.setOnClickListener { cliqueLetra("R") }
            letraSBtn.setOnClickListener { cliqueLetra("S") }
            letraTBtn.setOnClickListener { cliqueLetra("T") }
            letraUBtn.setOnClickListener { cliqueLetra("U") }
            letraVBtn.setOnClickListener { cliqueLetra("V") }
            letraWBtn.setOnClickListener { cliqueLetra("W") }
            letraXBtn.setOnClickListener { cliqueLetra("X") }
            letraYBtn.setOnClickListener { cliqueLetra("Y") }
            letraZBtn.setOnClickListener { cliqueLetra("Z") }
        }
    }

    fun cliqueLetra(letra: String) {
        if (palavraAtual != "") {
            letrasSelecionadas = letrasSelecionadas + " " + letra
            activityJogarBinding.letrasTv.text = letrasSelecionadas
            desabilitarLetra(letra)
            comparaLetraPalavra(letra)
        }
    }

    fun comparaLetraPalavra(letra: String){
        if(palavraAtual.contains(letra.uppercase())) {
            letrasCertas = letrasCertas + " " + letra
            activityJogarBinding.letrasCertasTv.text = letrasCertas
        } else {
            letrasErradas = letrasErradas + " " + letra
            activityJogarBinding.letrasErradasTv.text = letrasErradas
            errosRodada++
            mostrarParteCorpo(errosRodada)
        }

        for(i in 0 until palavraAtual.length) {
            if(palavraAtual[i].toString().uppercase() == letra) {
                acertosRodada++
                retiraMascaraPalavra(i, letra)
            }
        }

        if (acertosRodada == palavraAtual.length){
            totalAcertos++
            val sb = StringBuilder()
            sb.append(activityJogarBinding.palavrasCertasTv.text).append(" " + palavraSemTratamento)
            activityJogarBinding.palavrasCertasTv.text = sb.toString()
            Toast.makeText(this, "VOCÊ GANHOU! ", Toast.LENGTH_SHORT).show()
            reiniciarRodada()
        }
        if (errosRodada == 6){
            val sb = StringBuilder()
            sb.append(activityJogarBinding.palavrasErradasTv.text).append(" " + palavraSemTratamento)
            activityJogarBinding.palavrasErradasTv.text = sb.toString()
            totalErros++
            Toast.makeText(this, "VOCÊ PERDEU! ", Toast.LENGTH_SHORT).show()
            reiniciarRodada()
        }
    }

    fun retiraMascaraPalavra(posicao: Int, letra: String){
        mascaraMap[posicao] = letra
        var palavraMascara = ""
        for ((k, v) in mascaraMap) {
            palavraMascara = palavraMascara + "$v"
        }
        activityJogarBinding.palavraTv.text = palavraMascara
    }

    fun mostrarParteCorpo(erro: Int){
        if (erro == 1 ) {
            activityJogarBinding.cabecaTv.visibility = View.VISIBLE
        } else if (erro == 2 ) {
            activityJogarBinding.troncoTv.visibility = View.VISIBLE
        } else if (erro == 3 ) {
            activityJogarBinding.bracoEsquerdoTv.visibility = View.VISIBLE
        } else if (erro == 4 ) {
            activityJogarBinding.bracoDireitoTv.visibility = View.VISIBLE
        } else if (erro == 5 ) {
            activityJogarBinding.pernaEsquerdaTv.visibility = View.VISIBLE
        } else if (erro == 6 ) {
            activityJogarBinding.pernaDireitaTv.visibility = View.VISIBLE
        }
    }

    fun esconderParteCorpo(){
        with(activityJogarBinding) {
            cabecaTv.visibility = View.GONE
            troncoTv.visibility = View.GONE
            bracoEsquerdoTv.visibility = View.GONE
            bracoDireitoTv.visibility = View.GONE
            pernaEsquerdaTv.visibility = View.GONE
            pernaDireitaTv.visibility = View.GONE
        }
    }


    fun reiniciarRodada(){
        letrasSelecionadas = "Letras: "
        letrasCertas = "Acertou: "
        letrasErradas = "Errou: "
        palavraAtual = ""
        palavraSemTratamento = ""
        activityJogarBinding.palavraTv.text = palavraAtual
        activityJogarBinding.letrasTv.text = letrasSelecionadas
        activityJogarBinding.letrasCertasTv.text = letrasCertas
        activityJogarBinding.letrasErradasTv.text = letrasErradas
        esconderParteCorpo()
        mascaraMap.clear()
        if (rodada < rodadas - 1) {
            acertosRodada = 0
            errosRodada = 0
            resetTeclado(true)
            rodada++
            carregarDadosForca()
        } else {
            with(activityJogarBinding) {
                totalRodadasTv.text = ""
                jogoForcaTv.text = "RELATÓRIO FINAL"
                letrasTv.visibility = View.GONE
                letrasCertasTv.visibility = View.GONE
                letrasErradasTv.visibility = View.GONE
                tecladoLayout.visibility = View.GONE
                relatorioLayout.visibility = View.VISIBLE
                totalPalavrasTv.text = "Total de Palavras: " + rodadas
                totalAcertosTv.text = "Total de Acertos: " + totalAcertos
                totalErrosTv.text = "Total de Erros: " + totalErros
            }
        }
    }

    fun tratarCaracteres(str: String?): String? {
        val nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD)
        val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(nfdNormalizedString).replaceAll("").uppercase()
    }

    fun desabilitarLetra(letra: String) {
        with(activityJogarBinding) {
            when(letra) {
                "A" -> letraABtn.isEnabled = false
                "B" -> letraBBtn.isEnabled = false
                "C" -> letraCBtn.isEnabled = false
                "D" -> letraDBtn.isEnabled = false
                "E" -> letraEBtn.isEnabled = false
                "F" -> letraFBtn.isEnabled = false
                "G" -> letraGBtn.isEnabled = false
                "H" -> letraHBtn.isEnabled = false
                "I" -> letraIBtn.isEnabled = false
                "J" -> letraJBtn.isEnabled = false
                "K" -> letraKBtn.isEnabled = false
                "L" -> letraLBtn.isEnabled = false
                "M" -> letraMBtn.isEnabled = false
                "N" -> letraNBtn.isEnabled = false
                "O" -> letraOBtn.isEnabled = false
                "P" -> letraPBtn.isEnabled = false
                "Q" -> letraQBtn.isEnabled = false
                "R" -> letraRBtn.isEnabled = false
                "S" -> letraSBtn.isEnabled = false
                "T" -> letraTBtn.isEnabled = false
                "U" -> letraUBtn.isEnabled = false
                "V" -> letraVBtn.isEnabled = false
                "W" -> letraWBtn.isEnabled = false
                "X" -> letraXBtn.isEnabled = false
                "Y" -> letraYBtn.isEnabled = false
                "Z" -> letraZBtn.isEnabled = false
            }
        }
    }

    fun resetTeclado(parametro: Boolean) {
        with(activityJogarBinding) {
            letraABtn.isEnabled = parametro
            letraBBtn.isEnabled = parametro
            letraCBtn.isEnabled = parametro
            letraDBtn.isEnabled = parametro
            letraEBtn.isEnabled = parametro
            letraFBtn.isEnabled = parametro
            letraGBtn.isEnabled = parametro
            letraHBtn.isEnabled = parametro
            letraIBtn.isEnabled = parametro
            letraJBtn.isEnabled = parametro
            letraKBtn.isEnabled = parametro
            letraLBtn.isEnabled = parametro
            letraMBtn.isEnabled = parametro
            letraNBtn.isEnabled = parametro
            letraOBtn.isEnabled = parametro
            letraPBtn.isEnabled = parametro
            letraQBtn.isEnabled = parametro
            letraRBtn.isEnabled = parametro
            letraSBtn.isEnabled = parametro
            letraTBtn.isEnabled = parametro
            letraUBtn.isEnabled = parametro
            letraVBtn.isEnabled = parametro
            letraWBtn.isEnabled = parametro
            letraXBtn.isEnabled = parametro
            letraYBtn.isEnabled = parametro
            letraZBtn.isEnabled = parametro
        }
    }
}