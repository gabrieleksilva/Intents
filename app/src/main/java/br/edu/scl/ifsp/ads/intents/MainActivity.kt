package br.edu.scl.ifsp.ads.intents

import android.Manifest.permission.CALL_PHONE
import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.content.Intent.ACTION_DIAL
import android.content.Intent.ACTION_PICK
import android.content.Intent.ACTION_VIEW
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.edu.scl.ifsp.ads.intents.databinding.ActivityMainBinding
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    // o obj só é instanciado quando ele é chamado
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater) // tem que ser executado em algum momento para que meu obj seja executado
    }
    private lateinit var parl: ActivityResultLauncher<Intent>

    private lateinit var permissaoChamadaArl: ActivityResultLauncher<String>

    private lateinit var pegarImagemArl: ActivityResultLauncher<Intent>
    companion object {
        const val PARAMETRO_EXTRA = "PARAMETRO_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        supportActionBar?.subtitle = "MainActivity"

        parl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == RESULT_OK) {
                result.data?.getStringExtra(PARAMETRO_EXTRA)?.let { parametro ->
                    amb.parametroTv.text = parametro
                }
            }
        }

        permissaoChamadaArl = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            permissaoConcedida ->
            if(permissaoConcedida){
                chamarNumero(true)
            } else{
                Toast.makeText(this, "Sem permissao, sem chamada", Toast.LENGTH_SHORT).show()
                finish()//como nao tem permissao, encerra o aplicativo
            }
        }

        pegarImagemArl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            if(result.resultCode == RESULT_OK){
                // Uma imagem foi seleciionada
                val imagemUri = result.data?.data
                // se for diferente de null entra no let
                imagemUri?.let {
                    amb.parametroTv.text = imagemUri.toString()
                    val visualizarIntent = Intent(ACTION_VIEW, imagemUri)
                    startActivity(visualizarIntent)
                }
            }
        }

        amb.entrarParametroBt.setOnClickListener {
            val parametroIntent = Intent("PARAMETRO_ACTIVITY_ACTION")
            parametroIntent.putExtra(PARAMETRO_EXTRA, amb.parametroTv.text.toString())

            parl.launch(parametroIntent)

        }
    }

    //chamando o menu na tela main
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Kotlin nao tem switch case, tem o when()
        return when (item.itemId){
            R.id.viewMi -> {
//                Toast.makeText(this, "clicou no viewMi", Toast.LENGTH_SHORT).show()
                // fazendo abrir o navegador com a Intent
                val url = Uri.parse(amb.parametroTv.text.toString())
                val navegadorIntent = Intent(ACTION_VIEW, url)
                startActivity(navegadorIntent)
                true
            }
            R.id.callMi -> {
                // verificar a versao do android
                // se for maior que a versão 23 precisa dar permissao
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // ver se a permissao existe
                    if(checkSelfPermission(CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                        chamarNumero(true)
                    }
                    // quando nao tem a permissao...
                    else{
                        // Solicitar a permissao
                        //precisa criar outro activity result launch
                        permissaoChamadaArl.launch(CALL_PHONE)
                    }
                }
                else{
                    //permissao ja foi dada durante a instalacao porque o android eh menor que M (23)
                    chamarNumero(true)
                }

                true
            }
            R.id.dialMi -> {
                chamarNumero(false)
                true
            }
            R.id.pickMi -> {
                val pegarImagemIntent = Intent(ACTION_PICK)
                val diretorioImagens = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .path
                pegarImagemIntent.setDataAndType(Uri.parse(diretorioImagens), "image/*")
                // lancar a Intent
                pegarImagemArl.launch(pegarImagemIntent)
                true
            }
            R.id.chooserMi -> true
            else -> true

        }
    }
    private fun chamarNumero(chamar: Boolean){

        val numeroUri = Uri.parse("tel: ${amb.parametroTv.text}")

        val chamarIntent = Intent( if (chamar) ACTION_CALL else ACTION_DIAL)
        chamarIntent.data = numeroUri
        startActivity(chamarIntent)



    }
}