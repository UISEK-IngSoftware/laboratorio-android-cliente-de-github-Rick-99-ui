package ec.edu.uisek.githubclient.models

// Modelo de datos para un repositorio de GitHub
data class Repo (
    val id: Long,
    val name: String,
    val description: String,
    val lenguage: String?,
    val owner: RepoOwner,
    )

// Modelo de datos para la creación/actualización de un repositorio
data class RepoRequest(
    val name: String,
    val description: String,
)
