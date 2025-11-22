package ec.edu.uisek.githubclient

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ec.edu.uisek.githubclient.databinding.ActivityMainBinding
import ec.edu.uisek.githubclient.models.Repo
import ec.edu.uisek.githubclient.models.RepoRequest
import ec.edu.uisek.githubclient.services.GithubApiService
import ec.edu.uisek.githubclient.services.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var reposAdapter: ReposAdapter
    private lateinit var apiService: GithubApiService

    // Método de ciclo de vida llamado al crear la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = RetrofitClient.gitHubApiService
        setupRecyclerView()

        binding.newRepoFab.setOnClickListener {
            displayNewRepoForm()
        }
    }

    // Método de ciclo de vida llamado cuando la actividad se reanuda
    override fun onResume() {
        super.onResume()
        fetchRepositories()
    }

    // Configura el RecyclerView y su adaptador
    private fun setupRecyclerView() {
        reposAdapter = ReposAdapter(
            onEditClick = { repo -> displayEditRepoForm(repo) },
            onDeleteClick = { repo -> showDeleteConfirmation(repo) }
        )
        binding.reposRecyclerView.adapter = reposAdapter
    }

    // Obtiene la lista de repositorios desde la API
    private fun fetchRepositories() {
        val call = apiService.getRepos()

        call.enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>?>, response: Response<List<Repo>?>) {
                if (response.isSuccessful) {
                    val repos = response.body()
                    if (repos != null && repos.isNotEmpty()) {
                        reposAdapter.updateRepositories(repos)
                    } else {
                        showMessage("No se encontraron repositorios")
                    }
                } else {
                    val errorMessage = when (response.code()) {
                        401 -> "No autorizado"
                        403 -> "Prohibido"
                        404 -> "No encontrado"
                        else -> "Error ${response.code()}"
                    }
                    showMessage("Error: $errorMessage")
                }
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                showMessage("No se pudieron cargar repositorios")
            }
        })
    }

    // Muestra el formulario para crear un nuevo repositorio
    private fun displayNewRepoForm() {
        val intent = Intent(this, RepoForm::class.java)
        startActivity(intent)
    }

    // Muestra el formulario para editar un repositorio existente
    private fun displayEditRepoForm(repo: Repo) {
        val intent = Intent(this, RepoForm::class.java).apply {
            putExtra("REPO_ID", repo.id)
            putExtra("REPO_NAME", repo.name)
            putExtra("REPO_DESCRIPTION", repo.description)
            putExtra("REPO_OWNER", repo.owner.login)
            putExtra("IS_EDIT", true)
        }
        startActivity(intent)
    }

    // Muestra un diálogo de confirmación antes de eliminar
    private fun showDeleteConfirmation(repo: Repo) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar repositorio")
            .setMessage("¿Estás seguro de que quieres eliminar \"${repo.name}\"?")
            .setPositiveButton("Sí") { dialog, which ->
                deleteRepository(repo)
            }
            .setNegativeButton("No", null)
            .show()
    }

    // Realiza la petición a la API para eliminar el repositorio
    private fun deleteRepository(repo: Repo) {
        val call = apiService.deleteRepo(repo.owner.login, repo.name)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    showMessage("Repositorio eliminado exitosamente")
                    fetchRepositories() // Refrescar la lista
                } else {
                    val errorMessage = when (response.code()) {
                        401 -> "No autorizado para eliminar"
                        403 -> "Prohibido eliminar este repositorio"
                        404 -> "Repositorio no encontrado"
                        else -> "Error ${response.code()} al eliminar"
                    }
                    showMessage("Error: $errorMessage")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showMessage("Error al eliminar el repositorio: ${t.message}")
            }
        })
    }

    // Muestra un mensaje Toast
    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}