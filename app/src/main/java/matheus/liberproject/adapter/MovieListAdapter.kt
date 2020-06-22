package matheus.liberproject.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_item.view.*
import matheus.liberproject.R
import matheus.liberproject.activity.MovieDetailsActivity
import matheus.liberproject.model.Movie

class MovieListAdapter(private val context: Context, private val movies: List<Movie>) :
    RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    private fun setImage(movie: Movie, holder: ViewHolder) {
        Picasso
            .get()
            .load(movie.Poster)
            .placeholder(R.drawable.placeholder)
            .into(holder.view.imgMovie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]

        setImage(movie, holder)
        holder.view.txtName.text = movie.Title
        holder.view.txtYear.text = movie.Year

        holder.view.cardMovie.setOnClickListener {
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra("movie_id", movie.imdbID)

            context.startActivity(intent)
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}