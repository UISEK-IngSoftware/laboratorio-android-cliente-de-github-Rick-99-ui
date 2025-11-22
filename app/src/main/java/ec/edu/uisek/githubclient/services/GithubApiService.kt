package ec.edu.uisek.githubclient.services

import ec.edu.uisek.githubclient.models.Repo
import ec.edu.uisek.githubclient.models.RepoRequest
import retrofit2.Call
import retrofit2.http.*

interface GithubApiService {
    // Obtiene la lista de repositorios del usuario autenticado
    @GET("user/repos")
    fun getRepos(
        @Query("sort") sort: String = "created",
        @Query("direction") direction: String = "desc",
    ): Call<List<Repo>>

    // Crea un nuevo repositorio para el usuario autenticado
    @POST("user/repos")
    fun addRepo(@Body repo: RepoRequest): Call<Repo>

    // Actualiza un repositorio existente
    @PATCH("repos/{owner}/{repo}")
    fun updateRepo(
        @Path("owner") owner: String,
        @Path("repo") repoName: String,
        @Body repo: RepoRequest
    ): Call<Repo>

    // Elimina un repositorio
    @DELETE("repos/{owner}/{repo}")
    fun deleteRepo(
        @Path("owner") owner: String,
        @Path("repo") repoName: String
    ): Call<Void>
}
