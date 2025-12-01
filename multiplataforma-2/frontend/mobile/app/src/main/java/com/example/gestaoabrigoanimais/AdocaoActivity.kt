// ... imports
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.gestaoabrigoanimais.api.RetrofitClient
import com.example.gestaoabrigoanimais.api.AnimalRequest

// Dentro do seu setOnClickListener do botão salvar:
binding.btnSalvar.setOnClickListener {
    // Pegar dados dos EditTexts
    val nome = binding.edtNomeAnimal.text.toString()
    val raca = binding.edtRacaAnimal.text.toString()
    // ... pegue os outros campos ...

    val novoAnimal = AnimalRequest(nome, raca, "Macho", false, "Obs do App")

    // Envio para a API (Nuvem)
    lifecycleScope.launch {
        try {
            val response = RetrofitClient.instance.salvarAnimal(novoAnimal)
            if (response.isSuccessful) {
                Toast.makeText(this@AdocaoActivity, "Salvo na API!", Toast.LENGTH_SHORT).show()
                // Aqui você pode manter o código que salva no Room (banco local) também, se quiser backup
            } else {
                Toast.makeText(this@AdocaoActivity, "Erro API: ${response.code()}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
             Toast.makeText(this@AdocaoActivity, "Sem conexão", Toast.LENGTH_SHORT).show()
        }
    }
}