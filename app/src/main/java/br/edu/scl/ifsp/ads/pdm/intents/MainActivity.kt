package br.edu.scl.ifsp.ads.pdm.intents


import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.content.Intent.ACTION_DIAL
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.edu.scl.ifsp.ads.pdm.intents.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    companion object Constantes { // classe internar na main
        const val PARAMETRO_EXTRA = "PARAMETRO_EXTRA"
        //const val PARAMETRO_REQUEST_CODE = 0  //id

    }

    private lateinit var parl: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.toolbarTb)
        supportActionBar?.apply {
            title = getString(R.string.app_name)
            subtitle = this@MainActivity.javaClass.simpleName
        }

        // quando a tela secundaria abre é isso aqui que executa
        parl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK){
                result.data?.getStringExtra(PARAMETRO_EXTRA)?.let {
                    amb.parametroTv.text = it
                }
            }
        }


        amb.entrarParametroBt.setOnClickListener {

/*            Intent(this, ParametroActivity::class.java).apply {
                putExtra(PARAMETRO_EXTRA, amb.parametroTv.text.toString())
                startActivityForResult(this, PARAMETRO_REQUEST_CODE)
            }*/
// ou
            Intent("MINHA_ACTION_PARA_PROXIMA_TELA").apply {
                amb.parametroTv.text.toString().let {
                    putExtra(PARAMETRO_EXTRA,it)
                }
                parl.launch(this) //startActivityForResult(this, PARAMETRO_REQUEST_CODE)
            }


            //jeito java
            /*val parametroIntent = Intent(this, ParametroActivity::class.java)
            parametroIntent.putExtra(PARAMETRO_EXTRA, amb.parametroTv.text.toString())
            startActivityForResult(parametroIntent, PARAMETRO_REQUEST_CODE) */
        }
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Result_ok é como se fosse o return 0 da linguagem c, indica que a tela encerrou com sucesso
        if (requestCode == PARAMETRO_REQUEST_CODE && resultCode == RESULT_OK){
            data?.getStringExtra(PARAMETRO_EXTRA)?.let{ retorno ->
                amb.parametroTv.text = retorno
            }
        }
    }*/

    //criar o menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // eh chamada quando o usuario clica em uma das opções do item do menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.viewMi -> {
                val url: Uri = Uri.parse(amb.parametroTv.text.toString())
                val navegadorIntent = Intent(ACTION_VIEW, url)
                startActivity(navegadorIntent)
                true
            }
            R.id.callMi -> {
                val telUri = Uri.parse("tel: ${amb.parametroTv.text.toString()}")
                val discarIntent = Intent(ACTION_CALL)
                discarIntent.setData(telUri)
                startActivity(discarIntent)
                true
            }
            R.id.dialMi -> {
                    val telUri = Uri.parse("tel: ${amb.parametroTv.text.toString()}")
                    val discarIntent = Intent(ACTION_DIAL)
                    discarIntent.setData(telUri)
                    startActivity(discarIntent)
                    true
            }
            R.id.pickMi -> { true }
            R.id.chooserMi -> { true }
            else -> { false }
        }
    }

}