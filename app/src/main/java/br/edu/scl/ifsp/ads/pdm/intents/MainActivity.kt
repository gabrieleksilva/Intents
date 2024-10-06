package br.edu.scl.ifsp.ads.pdm.intents


import android.Manifest.permission.CALL_PHONE
import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.content.Intent.ACTION_CHOOSER
import android.content.Intent.ACTION_DIAL
import android.content.Intent.ACTION_PICK
import android.content.Intent.ACTION_VIEW
import android.content.Intent.EXTRA_INTENT
import android.content.Intent.EXTRA_TITLE
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
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
    //pcarl = Permissao Chamada Activity Result Laucher
    private lateinit var pcarl: ActivityResultLauncher<String>
    private lateinit var piarl: ActivityResultLauncher<Intent>

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

        piarl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            resultado ->
            //pra abrir a imagem pequena da galeria de forma que ocupa a tela toda (visualizador de imagem)
            if (resultado.resultCode == RESULT_OK){
                resultado.data?.data?.let {
                    startActivity(Intent(ACTION_VIEW, it))
                }
            }
        }

        pcarl = registerForActivityResult(ActivityResultContracts.RequestPermission())
        { permissaoConcedida ->
            //se o usuario permitir
            chamarOuDiscar(true)
            if (permissaoConcedida){
                //fazer a chamada
            } else{
                Toast.makeText(this, "Permissao necessaria!", Toast.LENGTH_SHORT).show()
            }
        }

            amb.entrarParametroBt.setOnClickListener {
            Intent("MINHA_ACTION_PARA_PROXIMA_TELA").apply {
                amb.parametroTv.text.toString().let {
                    putExtra(PARAMETRO_EXTRA,it)
                }
                parl.launch(this) //startActivityForResult(this, PARAMETRO_REQUEST_CODE)
            }
        }
    }

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
               if(checkSelfPermission(CALL_PHONE) == PERMISSION_GRANTED){
                   chamarOuDiscar(true)
               }else {
                   pcarl.launch(CALL_PHONE)
               }
                true
            }
            R.id.dialMi -> {
                chamarOuDiscar(false)
                true
            }
            //abrir a galeria
            R.id.pickMi -> {
                //pegando o caminho da galeria
                val caminho = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).path

                //na intent diz que vou pegar algum recurso
                val pegarImagemIntent = Intent(ACTION_PICK)
                pegarImagemIntent.setDataAndType(Uri.parse(caminho), "image/*")
                piarl.launch(pegarImagemIntent)
                true
            }
            R.id.chooserMi -> {
                Uri.parse(amb.parametroTv.text.toString()).let { url ->
                    Intent(ACTION_VIEW, url).let{
                        navegadorIntent ->
                        val escolherAppIntent = Intent(ACTION_CHOOSER)
                        escolherAppIntent.putExtra(EXTRA_TITLE, "escolha seu navegador")
                        escolherAppIntent.putExtra(EXTRA_INTENT, navegadorIntent)
                        startActivity(escolherAppIntent)
                    }
                }

                true
            }
            else -> { false }
        }
    }

    private fun chamarOuDiscar(chamar: Boolean){
        Uri.parse("tel: ${amb.parametroTv.text.toString()}").let{
            val discarIntent = Intent(if(chamar) ACTION_CALL else ACTION_CALL).apply {
                data = it
                startActivity(this)
            }
        }
    }
}