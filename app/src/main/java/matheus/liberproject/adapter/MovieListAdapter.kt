package matheus.liberproject.adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.DiffUtil
import com.squareup.picasso.Picasso
import matheus.liberproject.R
import matheus.liberproject.model.Movie
import kotlin.math.max
import kotlin.properties.Delegates

class MovieListAdapter(val context: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<MovieListAdapter.MovieHolder>() {

    var movies: List<Movie> by Delegates.observable(emptyList()) { _, oldList, newList ->
        autoNotify(oldList, newList) { (id), (id1) -> id == id1 }
    }

    private fun setImage(movie: Movie, holder: MovieHolder) {
        Picasso
            .get()
            .load(movie.cover)
            .placeholder(R.drawable.placeholder)
            .into(holder.imgUser, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    val imageBitmap = (holder.imgUser.drawable as BitmapDrawable).bitmap
                    val imageDrawable = RoundedBitmapDrawableFactory.create(Resources.getSystem(), imageBitmap)
                    imageDrawable.isCircular = true
                    imageDrawable.cornerRadius = max(imageBitmap.width, imageBitmap.height) / 2.0f
                    holder.imgUser.setImageDrawable(imageDrawable)
                }

                override fun onError(e: Exception?) {
                    holder.imgUser.setImageResource(R.drawable.placeholder)
                }
            })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false)
        return MovieHolder(view)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: MovieHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty())
            onBindViewHolder(holder, position)
        else {
            val movie = movies[position]

            for (itemChangeList in payloads as MutableList<ArrayList<String>>) {
                if (itemChangeList.contains("img_update"))
                    setImage(movie, holder)

                if (itemChangeList.contains("name_update"))
                    holder.txtName.text = movie.name

                if (itemChangeList.contains("release_update"))
                    holder.txtYear.text = movie.released

                if (itemChangeList.contains("rating_update"))
                    holder.txtRating.text = movie.imdbRating.toString()
            }
        }
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val movie = movies[position]

        setImage(movie, holder)
        holder.txtName.text = movie.name
        holder.txtYear.text = movie.released
        holder.txtRating.text = movie.imdbRating.toString()

        holder.card.setOnClickListener {
            //TODO Redirect to movie info
        }
    }

    inner class MovieHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var card: androidx.cardview.widget.CardView = itemView.findViewById(R.id.cardMovie)
        var imgUser: ImageView = itemView.findViewById(R.id.imgUser)
        var txtName: TextView = itemView.findViewById(R.id.txtName)
        var txtYear: TextView = itemView.findViewById(R.id.txtYear)
        var txtRating: TextView = itemView.findViewById(R.id.txtRating)
    }
}

fun androidx.recyclerview.widget.RecyclerView.Adapter<*>.autoNotify(oldList: List<Movie>, newList: List<Movie>, compare: (Movie, Movie) -> Boolean) {

    val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            compare(oldList[oldItemPosition], newList[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return (oldItem.cover == newItem.cover &&
                    oldItem.name == newItem.name &&
                    oldItem.released == newItem.released &&
                    oldItem.imdbRating == newItem.imdbRating)
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            val itemChangeList = ArrayList<String>()

            if (oldItem.cover == newItem.cover) itemChangeList.add("img_update")
            if (oldItem.name == newItem.name) itemChangeList.add("name_update")
            if (oldItem.released == newItem.released) itemChangeList.add("release_update")
            if (oldItem.imdbRating == newItem.imdbRating) itemChangeList.add("rating_update")

            return itemChangeList
        }
    }, true)

    diff.dispatchUpdatesTo(this)
}