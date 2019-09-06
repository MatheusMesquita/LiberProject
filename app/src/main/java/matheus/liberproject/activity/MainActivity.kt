package matheus.liberproject.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import matheus.liberproject.R
import matheus.liberproject.adapter.MovieListAdapter
import matheus.liberproject.model.Movie
import matheus.liberproject.network.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var mAdapter: MovieListAdapter
    private lateinit var mMovies: List<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mMovies = emptyList()
        swipeRefresh.isRefreshing = true
        callMovies()

        mAdapter = MovieListAdapter(this@MainActivity)
        mAdapter.movies = mMovies
        rvMoviesList.adapter = mAdapter
        rvMoviesList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@MainActivity)

        swipeRefresh.setOnRefreshListener { callMovies() }
    }

    private fun callMovies() {
        val call = RetrofitInitializer().omdbService().movies("a")
        call.enqueue(object: Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>?>?, response: Response<List<Movie>?>?) {
                response?.body()?.let {
                    mMovies = it.subList(0, 99)
                    mAdapter.movies = mMovies
                    if (swipeRefresh.isRefreshing)
                        swipeRefresh.isRefreshing = false
                }
            }

            override fun onFailure(call: Call<List<Movie>?>?, t: Throwable?) {
                Toast.makeText(this@MainActivity, "Problem receiving data", Toast.LENGTH_LONG).show()
                if (swipeRefresh.isRefreshing)
                    swipeRefresh.isRefreshing = false
            }
        })
    }
}
