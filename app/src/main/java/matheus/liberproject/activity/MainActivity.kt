package matheus.liberproject.activity

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
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

        rvMoviesList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@MainActivity)
        rvMoviesList.addItemDecoration(
            CustomDividerItem(ContextCompat.getDrawable(this, R.drawable.divider)!!)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            isIconifiedByDefault = false
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    callMovies(query!!)
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    callMovies(query!!)
                    return false
                }
            })
        }

        return true
    }

    private fun callMovies(movie: String) {
        val call = RetrofitInitializer().omdbService().movies(MY_KEY, movie)
        call.enqueue(object: Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful &&
                    response.body() != null &&
                    response.body()!!.Search != null &&
                    response.body()!!.Search!!.isNotEmpty()) {
                    mMovies = response.body()!!.Search!!
                    mAdapter = MovieListAdapter(this@MainActivity, mMovies)
                    rvMoviesList.adapter = mAdapter
                    mAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable?) {
                Toast.makeText(this@MainActivity, "Problem receiving data",
                    Toast.LENGTH_LONG).show()
            }
        })
    }
}
