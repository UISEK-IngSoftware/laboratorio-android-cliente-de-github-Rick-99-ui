package ec.edu.uisek.githubclient

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ec.edu.uisek.githubclient.databinding.FragmentRepoitemBinding
import ec.edu.uisek.githubclient.models.Repo

class ReposViewHolder(
    private val binding: FragmentRepoitemBinding,
    private val onEditClick: (Repo) -> Unit,
    private val onDeleteClick: (Repo) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    // Vincula los datos del repositorio con las vistas
    fun bind(repo: Repo) {
        binding.repoName.text = repo.name
        binding.repoDescription.text = repo.description
        binding.repoLang.text = repo.lenguage ?: "No especificado"

        Glide.with(binding.root.context)
            .load(repo.owner.avatarUrl)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .circleCrop()
            .into(binding.repoOwnerImagen)

        // Configurar los listeners de los botones
        binding.editButton.setOnClickListener { onEditClick(repo) }
        binding.deleteButton.setOnClickListener { onDeleteClick(repo) }
    }
}

class ReposAdapter(
    private val onEditClick: (Repo) -> Unit = {},
    private val onDeleteClick: (Repo) -> Unit = {}
) : RecyclerView.Adapter<ReposViewHolder>() {

    private var repositories: List<Repo> = emptyList()

    // Devuelve el número de elementos en la lista
    override fun getItemCount(): Int = repositories.size

    // Crea una nueva instancia de ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposViewHolder {
        val binding = FragmentRepoitemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReposViewHolder(binding, onEditClick, onDeleteClick)
    }

    // Vincula los datos en una posición específica con el ViewHolder
    override fun onBindViewHolder(holder: ReposViewHolder, position: Int) {
        holder.bind(repositories[position])
    }

    // Actualiza la lista de repositorios y notifica los cambios
    fun updateRepositories(newRepositories: List<Repo>) {
        repositories = newRepositories
        notifyDataSetChanged()
    }
}