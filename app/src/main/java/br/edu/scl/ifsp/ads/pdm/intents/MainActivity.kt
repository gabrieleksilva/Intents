package br.edu.scl.ifsp.ads.pdm.intents


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.scl.ifsp.ads.pdm.intents.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    companion object Constantes { // classe internar na main
        const val PARAMETRO_EXTRA = "PARAMETRO_EXTRA"
        const val PARAMETRO_REQUEST_CODE = 0  //id

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.toolbarTb)
        supportActionBar?.apply {
            title = getString(R.string.app_name)
            subtitle = this@MainActivity.javaClass.simpleName
        }

        amb.entrarParametroBt.setOnClickListener {

            Intent(this, ParametroActivity::class.java).apply {
                putExtra(PARAMETRO_EXTRA, amb.parametroTv.text.toString())
                startActivityForResult(this, PARAMETRO_REQUEST_CODE)
            }
// ou
            /*Intent(this, ParametroActivity::class.java).apply {
                amb.parametroTv.text.toString().let {
                    putExtra(PARAMETRO_EXTRA,it)
                }
                startActivityForResult(this, PARAMETRO_REQUEST_CODE)
            }*/


            //jeito java
            /*val parametroIntent = Intent(this, ParametroActivity::class.java)
            parametroIntent.putExtra(PARAMETRO_EXTRA, amb.parametroTv.text.toString())
            startActivityForResult(parametroIntent, PARAMETRO_REQUEST_CODE) */
        }
    }
}