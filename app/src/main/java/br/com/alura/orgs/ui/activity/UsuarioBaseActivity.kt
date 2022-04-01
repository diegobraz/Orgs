package br.com.alura.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.extensions.vaiPara
import br.com.alura.orgs.model.Usuario
import br.com.alura.orgs.preferences.dataStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class UsuarioBaseActivity: AppCompatActivity() {

    private val usuarioDao by lazy {
        AppDatabase.instancia(this).usarioDao()
    }

    private var _usuario : MutableStateFlow<Usuario?> = MutableStateFlow(null)
    protected var usuario : StateFlow<Usuario?> = _usuario

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        lifecycleScope.launch{
            verificaUsuarioLogado()
        }
    }

    private suspend fun buscaUsuario(userId: String) {
           _usuario.value = usuarioDao
               .buscaPorId(usuarioId = userId)
               .firstOrNull()
    }

    protected fun deslogaUsuario() {
        lifecycleScope.launch {
            dataStore.edit { preferences ->
                preferences.remove(stringPreferencesKey("userId"))
            }
        }
    }

    private fun vaiParaLogin() {
        vaiPara(LoginActivity::class.java){
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        finish()
    }

    private suspend fun verificaUsuarioLogado() {
        dataStore.data.collect { preferences ->
            preferences[stringPreferencesKey("userId")]?.let { userId ->
                buscaUsuario(userId)
            } ?: vaiParaLogin()
        }
    }

}