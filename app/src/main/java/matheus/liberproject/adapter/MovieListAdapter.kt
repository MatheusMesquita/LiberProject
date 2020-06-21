package matheus.liberproject.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_item.view.*
import matheus.liberproject.R
import matheus.liberproject.activity.MovieDetailsActivity
import matheus.liberproject.model.Movie
import kotlin.properties.Delegates

class MovieListAdapter(private val context: Context) :
    RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    var movies: List<Movie> by Delegates.observable(emptyList()) { _, oldList, newList ->
        autoNotify(oldList, newList) { (id), (id1) -> id == id1 }
    }

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty())
            onBindViewHolder(holder, position)
        else {
            val movie = movies[position]

            for (itemChangeList in payloads as MutableList<ArrayList<String>>) {
                if (itemChangeList.contains("img_update"))
                    setImage(movie, holder)

                if (itemChangeList.contains("name_update"))
                    holder.view.txtName.text = movie.Title

                if (itemChangeList.contains("release_update"))
                    holder.view.txtYear.text = movie.Year
            }
        }
    }

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

fun RecyclerView.Adapter<*>.autoNotify(oldList: List<Movie>, newList: List<Movie>, compare: (Movie, Movie) -> Boolean) {

    val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            compare(oldList[oldItemPosition], newList[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return (oldItem.Poster == newItem.Poster &&
                    oldItem.Title == newItem.Title &&
                    oldItem.Year == newItem.Year &&
                    oldItem.imdbID == newItem.imdbID)
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            val itemChangeList = ArrayList<String>()

            if (oldItem.Poster == newItem.Poster) itemChangeList.add("img_update")
            if (oldItem.Title == newItem.Title) itemChangeList.add("name_update")
            if (oldItem.Year == newItem.Year) itemChangeList.add("release_update")

            return itemChangeList
        }
    }, true)

    diff.dispatchUpdatesTo(this)
}