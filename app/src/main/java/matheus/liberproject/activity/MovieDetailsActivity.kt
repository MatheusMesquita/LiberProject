package matheus.liberproject.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_movie_details.*
import matheus.liberproject.R
import matheus.liberproject.model.MovieDetails
import matheus.liberproject.network.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var movieID: String
    private lateinit var mMovie: MovieDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        swipeMovieDetails.isRefreshing = true

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        title = "Detalhes"

        if (intent.hasExtra("movie_id"))
        movieID = intent.getStringExtra("movie_id")!!

        callMovie()

        swipeMovieDetails.setOnRefreshListener { callMovie() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun callMovie() {
        val call = RetrofitInitializer().omdbService().movie(MY_KEY,movieID)
        call.enqueue(object: Callback<MovieDetails> {
            override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
                mMovie = response.body()!!

                initViews()

                if (swipeMovieDetails.isRefreshing)
                    swipeMovieDetails.isRefreshing = false
            }

            override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                Toast.makeText(this@MovieDetailsActivity, "Problem receiving movie",
                    Toast.LENGTH_LONG).show()
                if (swipeMovieDetails.isRefreshing)
                    swipeMovieDetails.isRefreshing = false
            }
        })
    }

    private fun initViews() {
        Picasso
            .get()
            .load(mMovie.Poster)
            .placeholder(R.drawable.placeholder)
            .into(imgMovieDetail)

        txtMovieTitle.text = mMovie.Title
        txtMovieYear.text = mMovie.Year
        txtImdbRating.text = mMovie.imdbRating
        txtGenre.text = mMovie.Genre
        txtDuration.text = mMovie.Runtime
        txtSinopsis.text = mMovie.Plot
    }
}