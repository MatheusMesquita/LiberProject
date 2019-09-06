package matheus.liberproject.service

import matheus.liberproject.model.Movie
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface OMDBService {
    @GET("{s}")
    fun movies(@Path(value = "s", encoded = true) s: String): Call<List<Movie>>

    @GET("{i}")
    fun movie(@Path(value = "i", encoded = true) i: String): Call<Movie>
}