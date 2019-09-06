package matheus.liberproject.network

import matheus.liberproject.service.OMDBService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val MY_KEY = "f34b3213"

class RetrofitInitializer {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://www.omdbapi.com/?apikey=$MY_KEY")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun omdbService() = retrofit.create(OMDBService::class.java)
}