package br.edu.scl.ifsp.ads.pdm.intents

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.scl.ifsp.ads.pdm.intents.MainActivity.Constantes.PARAMETRO_EXTRA
import br.edu.scl.ifsp.ads.pdm.intents.databinding.ActivityParametroBinding

class ParametroActivity : AppCompatActivity() {
    private val apb: ActivityParametroBinding by lazy {
        ActivityParametroBinding.inflate(layoutInflater)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(apb.root)

        setSupportActionBar(apb.toolbarTb)
        supportActionBar?.apply {
            title = getString(R.string.app_name)
            subtitle = this@ParametroActivity.javaClass.simpleName
        }

        intent.getStringExtra(PARAMETRO_EXTRA)?.also { parametro ->
            apb.parametroEt.setText(parametro)

        }

        apb.enviarParametroBt.setOnClickListener{
            // criando uma intent
            Intent().apply {
                apb.parametroEt.text.toString().let {
                    putExtra(PARAMETRO_EXTRA, it)
                }
                setResult(RESULT_OK, this) //para devolver o valor para a main que sera inserido no campo texto da tela de parametro
            }
            finish()
        }
    }
}