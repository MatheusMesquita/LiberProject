package matheus.liberproject.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import matheus.liberproject.R
import matheus.liberproject.adapter.CustomDividerItem
import matheus.liberproject.adapter.MovieListAdapter
import matheus.liberproject.model.Movie
import matheus.liberproject.model.MovieResponse
import matheus.liberproject.network.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val MY_KEY = "f34b3213"

class MainActivity : AppCompatActivity() {

    private lateinit var mAdapter: MovieListAdapter
    private lateinit var mMovies: List<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "IMDB"

        mMovies = emptyList()
        swipeRefresh.isRefreshing = true
        callMovies()

        mAdapter = MovieListAdapter(this@MainActivity)
        mAdapter.movies = mMovies
        rvMoviesList.adapter = mAdapter
        rvMoviesList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@MainActivity)
        rvMoviesList.addItemDecoration(
            CustomDividerItem(ContextCompat.getDrawable(this, R.drawable.divider)!!)
        )

        swipeRefresh.setOnRefreshListener { callMovies() }
    }

    private fun callMovies() {
        val call = RetrofitInitializer().omdbService().movies(MY_KEY,"Fast")
        call.enqueue(object: Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                response?.body()?.let {
                    mMovies = it.Search
                    mAdapter.movies = mMovies
                    if (swipeRefresh.isRefreshing)
                        swipeRefresh.isRefreshing = false
                }
            }

            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                Toast.makeText(this@MainActivity, "Problem receiving data",
                    Toast.LENGTH_LONG).show()
                if (swipeRefresh.isRefreshing)
                    swipeRefresh.isRefreshing = false
            }
        })
    }
}
