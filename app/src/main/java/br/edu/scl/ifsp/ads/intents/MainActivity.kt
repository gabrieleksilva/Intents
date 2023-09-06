package br.edu.scl.ifsp.ads.intents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.scl.ifsp.ads.intents.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // o obj só é instanciado quando ele é chamado
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater) // tem que ser executado em algum momento para que meu obj seja executado
    }

    companion object {
        const val PARAMETRO_EXTRA = "PARAMETRO_EXTRA"
        const val PARAMETRO_REQUEST_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        supportActionBar?.subtitle = "MainActivity"

        amb.entrarParametroBt.setOnClickListener {
            val parametroIntent: Intent = Intent(this, ParametroActivity::class.java)
            parametroIntent.putExtra(PARAMETRO_EXTRA, amb.parametroTv.text.toString())

            startActivityForResult(parametroIntent, PARAMETRO_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PARAMETRO_REQUEST_CODE && resultCode == RESULT_OK){
            data?.getStringExtra(PARAMETRO_EXTRA)?.let { parametro ->
                amb.parametroTv.text = parametro
            }
        }
    }
}