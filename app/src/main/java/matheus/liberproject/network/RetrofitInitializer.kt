package matheus.liberproject.network

import matheus.liberproject.service.OMDBService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.omdbapi.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun omdbService(): OMDBService = retrofit.create(OMDBService::class.java)
}