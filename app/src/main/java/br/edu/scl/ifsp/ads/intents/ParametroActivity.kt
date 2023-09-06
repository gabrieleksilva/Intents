package br.edu.scl.ifsp.ads.intents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.scl.ifsp.ads.intents.databinding.ActivityMainBinding
import br.edu.scl.ifsp.ads.intents.databinding.ActivityParametroBinding

class ParametroActivity : AppCompatActivity() {
    private val apb by lazy {
        ActivityParametroBinding.inflate(layoutInflater) // tem que ser executado em algum momento para que meu obj seja executado
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(apb.root)
        supportActionBar?.subtitle = "ParametroActivity"


        intent.getStringExtra(MainActivity.PARAMETRO_EXTRA)?.let {parametro ->
            apb.parametroEt.setText(parametro)
        }

        apb.enviarParametroBt.setOnClickListener {
            val intentRetorno = Intent()
            val parametroRetorno = apb.parametroEt.text.toString()
            intentRetorno.putExtra(MainActivity.PARAMETRO_EXTRA, parametroRetorno)
            setResult(RESULT_OK, intentRetorno)
            finish()//chama os 3 ciclos de vida, start finish
        }

    }
}