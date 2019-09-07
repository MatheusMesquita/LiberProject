package matheus.liberproject.service

import matheus.liberproject.model.Movie
import matheus.liberproject.model.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OMDBService {
    @GET("/")
    fun movies(@Query("apikey") key: String, @Query("s") searchTerm: String): Call<MovieResponse>

    @GET("/")
    fun movie(@Query("apikey") key: String, @Query("i") imdbID: String): Call<Movie>
}